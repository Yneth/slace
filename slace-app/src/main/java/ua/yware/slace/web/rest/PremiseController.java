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

import com.google.common.collect.Lists;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import lombok.RequiredArgsConstructor;
import ua.yware.slace.dao.CategoryRepository;
import ua.yware.slace.dao.CommentRepository;
import ua.yware.slace.dao.PremiseRepository;
import ua.yware.slace.model.Comment;
import ua.yware.slace.model.Premise;
import ua.yware.slace.model.User;
import ua.yware.slace.service.dto.CategoryDto;
import ua.yware.slace.service.dto.CommentDto;
import ua.yware.slace.service.dto.PremiseDto;
import ua.yware.slace.service.premise.PremiseSearchService;
import ua.yware.slace.service.premise.PremiseService;
import ua.yware.slace.service.storage.StorageService;
import ua.yware.slace.service.user.CurrentUserService;
import ua.yware.slace.web.exception.ResourceNotFoundException;
import ua.yware.slace.web.rest.form.CommentForm;
import ua.yware.slace.web.rest.form.PremiseForm;
import ua.yware.slace.web.rest.form.PremiseSearchForm;
import ua.yware.slace.web.rest.form.UpdatePremiseForm;

@RestController
@RequestMapping("${api.prefix}/premises")
@RequiredArgsConstructor
public class PremiseController {

    private final PremiseSearchService premiseSearchService;
    private final PremiseService premiseService;
    private final StorageService storageService;
    private final PremiseRepository premiseRepository;
    private final CurrentUserService currentUserService;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/search")
    public Iterable<PremiseDto> searchPremises(PremiseSearchForm premiseSearchFrom) {
        return premiseSearchService.findAll(premiseSearchFrom).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/categories")
    public Iterable<CategoryDto> getPremiseCategories() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/popular")
    public Iterable<PremiseDto> getPopularPremises() {
        return StreamSupport.stream(premiseRepository.findAll().spliterator(), false)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PremiseDto getPremiseById(@PathVariable("id") BigInteger id) {
        return premiseRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("No such premise"));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user")
    public Iterable<PremiseDto> getUserPremises() {
        return premiseRepository.findAllByOwner(currentUserService.getCurrentUser())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/reserved")
    public Iterable<PremiseDto> listReserved() {
        return premiseRepository.findPremisesReservedBy(currentUserService.getCurrentUser())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity create(@RequestBody PremiseForm createPremiseForm) {
        Premise premise = new Premise();
        mapToEntity(createPremiseForm, premise);

        premiseService.save(premise);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity update(@RequestBody UpdatePremiseForm premiseForm) {
        Premise premise = premiseRepository.findById(premiseForm.getId())
                .orElseThrow(() -> new RuntimeException("not found"));
        mapToEntity(premiseForm, premise);

        premiseService.save(premise);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/image")
    public ResponseEntity uploadImage(@PathVariable("id") BigInteger premiseId,
                                      @RequestParam("file") MultipartFile file) {
        Premise premise = premiseRepository.findById(premiseId)
                .orElseThrow(() -> new RuntimeException("no such premise."));
//         TODO: investigate how to get lazily loaded object
//        if (!currentUserService.getCurrentUser().equals(premise.getOwner())) {
        if (!currentUserService.getCurrentUser().getId().equals(premise.getOwner().getId())) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(
                originalFilename.lastIndexOf('.'), originalFilename.length());

        if (Stream.of(".jpeg", ".jpg", ".png").noneMatch(s -> s.equals(extension))) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        storageService.store("premise-" + premise.getId() + extension, file);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") BigInteger id) {
        premiseRepository.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/comment")
    public ResponseEntity leaveComment(@RequestBody CommentForm commentForm) {
        Premise premise = premiseRepository.findById(commentForm.getPremiseId())
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        Comment comment = new Comment();
        comment.setCreationDate(LocalDateTime.now());
        comment.setOwner(currentUserService.getCurrentUser());
        comment.setMessage(commentForm.getMessage());

        premise.getComments().add(commentRepository.save(comment));

        premiseRepository.save(premise);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{premiseId}/comment/{id}")
    public ResponseEntity removeComment(@PathVariable("premiseId") BigInteger premiseId,
                                        @PathVariable("id") BigInteger commentId) {
        Premise premise = premiseRepository.findById(premiseId)
                .orElseThrow(() -> new RuntimeException("no such premise"));

        premise.removeComment(commentId);
        premiseService.save(premise);

        return new ResponseEntity(HttpStatus.OK);
    }

    private PremiseDto mapToDto(Premise premise) {
        PremiseDto dto = new PremiseDto();
        dto.setAbout(premise.getAbout());
        dto.setAddress(premise.getAddress());
        dto.setName(premise.getName());
        dto.setArea(premise.getArea());
        dto.setSpace(premise.getSpace());
        dto.setPriceRate(premise.getPriceRate());
        dto.setId(premise.getId());
        dto.setTotalEstimation(premise.getTotalEstimation());
        dto.setEquipment(premise.getEquipment());
        dto.setComments(premise.getComments()
                .stream()
                .map(c -> {
                    CommentDto commentDto = new CommentDto();
                    User owner = c.getOwner();
                    commentDto.setAuthorId(owner.getId());
                    commentDto.setAuthorName(owner.getFirstName());
                    commentDto.setMessage(c.getMessage());
                    return commentDto;
                }).collect(Collectors.toList()));
        return dto;
    }

    private void mapToEntity(PremiseForm premiseForm, Premise premise) {
        premise.setSchedule(premiseForm.getSchedule());
        premise.setAbout(premiseForm.getAbout());
        premise.setName(premiseForm.getName());
        premise.setDescription(premiseForm.getDescription());
        premise.setAddress(premiseForm.getAddress());
        premise.setEquipment(premiseForm.getEquipment());
        premise.setArea(premiseForm.getArea());
        premise.setSpace(premiseForm.getSpace());
        premise.setCategories(
                Lists.newArrayList(categoryRepository.findAllById(premiseForm.getCategories())));
        premise.setPriceRate(premiseForm.getPriceRate());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> premiseNotFound() {
        return new ResponseEntity<>("Premise not found", HttpStatus.NOT_FOUND);
    }

}
