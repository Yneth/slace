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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import ua.yware.slace.config.jwt.TokenService;
import ua.yware.slace.dao.UserRepository;
import ua.yware.slace.model.User;
import ua.yware.slace.model.UserRole;
import ua.yware.slace.service.dto.JwtTokenDto;
import ua.yware.slace.service.dto.LoginDto;
import ua.yware.slace.service.mail.MailService;
import ua.yware.slace.web.rest.form.ChangePasswordForm;
import ua.yware.slace.web.rest.form.CreateUserForm;
import ua.yware.slace.web.rest.form.UpdateUserForm;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AuthenticationController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;

    @PostMapping
    public ResponseEntity register(CreateUserForm createUserForm) {
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
        userRoles.add(new UserRole("admin"));
        user.setRoles(userRoles);

        userRepository.save(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@Valid @RequestBody LoginDto loginDTO, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getLogin(), loginDTO.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            boolean rememberMe = (loginDTO.getRememberMe() == null) ? false : loginDTO.getRememberMe();

            String token = tokenService.createToken(authentication, rememberMe);
            response.addHeader("Authentication", "Bearer " + token);

            return ResponseEntity.ok(new JwtTokenDto(token));
        } catch (AuthenticationException exception) {
            return new ResponseEntity<>(Collections.singletonMap(
                    "AuthenticationException", exception.getLocalizedMessage()
            ), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update")
    public ResponseEntity updateAccount(@RequestBody UpdateUserForm updateUserForm) {

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/password")
    public ResponseEntity changePassword(@RequestBody ChangePasswordForm changePasswordForm) {

        return new ResponseEntity(HttpStatus.OK);
    }

}
