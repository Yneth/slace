/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.yware.slace.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import ua.yware.slace.config.jwt.TokenService;
import ua.yware.slace.dao.UserRepository;
import ua.yware.slace.model.User;
import ua.yware.slace.model.UserRole;
import ua.yware.slace.service.dto.JwtTokenDto;
import ua.yware.slace.service.dto.LoginDto;
import ua.yware.slace.service.mail.MailService;
import ua.yware.slace.service.storage.StorageService;
import ua.yware.slace.service.user.CurrentUserService;
import ua.yware.slace.web.rest.form.ChangePasswordForm;
import ua.yware.slace.web.rest.form.CreateUserForm;
import ua.yware.slace.web.rest.form.UpdateUserForm;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final StorageService storageService;
    private final CurrentUserService currentUserService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;

    @PostMapping
    public ResponseEntity register(@RequestBody CreateUserForm createUserForm) {
        User user = new User();
        user.setAbout(createUserForm.getAbout());
        user.setCity(createUserForm.getCity());
        user.setLastName(createUserForm.getLastName());
        user.setFirstName(createUserForm.getFirstName());
        user.setLogin(createUserForm.getLogin());
        user.setPhone(createUserForm.getPhone());
        user.setEmail(createUserForm.getEmail());
        user.setPassword(passwordEncoder.encode(createUserForm.getPassword()));

        List<UserRole> userRoles = new ArrayList<>();
        userRoles.add(new UserRole("user"));
        user.setRoles(userRoles);

        userRepository.save(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@RequestBody LoginDto loginDTO, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getLogin(), loginDTO.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            boolean rememberMe = (loginDTO.getRememberMe() == null) ? false : loginDTO.getRememberMe();

            String token = tokenService.createToken(authentication, rememberMe);
            response.addHeader("Authorization", "Bearer " + token);

            return ResponseEntity.ok(new JwtTokenDto(token));
        } catch (AuthenticationException exception) {
            throw new RuntimeException(exception);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update")
    public ResponseEntity updateAccount(@RequestBody UpdateUserForm updateUserForm) {
        User currentUser = currentUserService.getCurrentUser();

        currentUser.setAbout(updateUserForm.getAbout());
        currentUser.setFirstName(updateUserForm.getFirstName());
        currentUser.setLastName(updateUserForm.getLastName());
        currentUser.setCity(updateUserForm.getCity());
        currentUser.setReceiveEmail(updateUserForm.isReceiveEmail());
        currentUser.setReceiveSms(updateUserForm.isReceiveSms());
        currentUser.setEmail(updateUserForm.getEmail());
        currentUser.setPhone(updateUserForm.getPhone());

        userRepository.save(currentUser);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/password")
    public ResponseEntity changePassword(@RequestBody ChangePasswordForm changePasswordForm) {
        String newPassword = changePasswordForm.getNewPassword();
        if (!newPassword.equals(changePasswordForm.getNewPasswordCopy())) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        // TODO send email
        User currentUser = currentUserService.getCurrentUser();
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/image")
    public ResponseEntity uploadImage(@RequestParam("file") MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(
                originalFilename.lastIndexOf('.'), originalFilename.length());

        if (Stream.of(".jpeg", ".jpg", ".png").noneMatch(s -> s.equals(extension))) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        User currentUser = currentUserService.getCurrentUser();
        storageService.store("profile-" + currentUser.getId() + extension, file);

        return new ResponseEntity(HttpStatus.OK);
    }

}
