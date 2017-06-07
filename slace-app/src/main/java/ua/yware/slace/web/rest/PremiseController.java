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

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lombok.RequiredArgsConstructor;
import ua.yware.slace.dao.CategoryRepository;
import ua.yware.slace.dao.PremiseRepository;
import ua.yware.slace.dao.PremiseReservationRepository;
import ua.yware.slace.model.Comment;
import ua.yware.slace.model.Premise;
import ua.yware.slace.model.PremiseReservation;
import ua.yware.slace.model.User;
import ua.yware.slace.service.dto.CategoryDto;
import ua.yware.slace.service.user.CurrentUserService;
import ua.yware.slace.web.rest.form.BookPremiseForm;
import ua.yware.slace.web.rest.form.CommentForm;
import ua.yware.slace.web.rest.form.PremiseForm;
import ua.yware.slace.web.rest.form.UpdatePremiseForm;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/premises")
@RequiredArgsConstructor
public class PremiseController {

    private final PremiseRepository premiseRepository;
    private final CurrentUserService currentUserService;
    private final PremiseReservationRepository premiseReservationRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public Iterable<CategoryDto> getPremiseCategories() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/popular")
    public Iterable<Premise> getPopularPremises() {
        return premiseRepository.findAll();
    }

    @GetMapping("/{id}")
    public Premise getPremiseById(@PathVariable("id") BigInteger id) {
        return premiseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No such premise"));
    }

    @GetMapping("/user")
    public Iterable<Premise> getUserPremises() {
        return premiseRepository.findAllByReservationsOwnerId(currentUserService.getCurrentUser().getId());
    }

    @GetMapping("/{id}/reservations/nearest")
    public Iterable<PremiseReservation> getNearestReservations(@PathVariable("id") BigInteger id) {
        LocalDateTime now = LocalDateTime.now();
        return premiseReservationRepository.findAllByPremiseIdAndFromAfterAndToBefore(
                id, now.minusMonths(1), now.plusMonths(1));
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional
    @PostMapping("{id}/reservations")
    public ResponseEntity bookPremise(@RequestBody BookPremiseForm bookPremiseForm) {
        // TODO if owner
        // TODO else other users
        User currentUser = currentUserService.getCurrentUser();
        Premise premise = premiseRepository.findById(bookPremiseForm.getPremiseId())
                .orElseThrow(() -> new RuntimeException("No such premise"));

        List<PremiseReservation> reservations = premiseReservationRepository.findAllByPremiseIdAndFromAfterAndToBefore(
                bookPremiseForm.getPremiseId(), bookPremiseForm.getFrom(), bookPremiseForm.getTo());
        if (!reservations.isEmpty()) {
            throw new RuntimeException("Already booked!");
        }

        PremiseReservation premiseReservation = new PremiseReservation();
        premiseReservation.setId(bookPremiseForm.getPremiseId());
        premiseReservation.setFrom(bookPremiseForm.getFrom());
        premiseReservation.setTo(bookPremiseForm.getTo());
        premiseReservation.setUser(currentUser);
        premiseReservation.setPremise(premise);
        premiseReservation.setPriceRate(premise.getPriceRate());

        premiseReservationRepository.save(premiseReservation);
        return new ResponseEntity(HttpStatus.OK);
    }


    @PreAuthorize("isAuthenticated()")
    @Transactional
    @DeleteMapping("/{premiseId}/reservations/{id}")
    public ResponseEntity cancelReservation(@PathVariable("id") BigInteger id) {
        premiseReservationRepository.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/reserved")
    public Iterable<Premise> listReserved() {
        return premiseRepository.findAllByReservationsOwnerId(currentUserService.getCurrentUser().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity create(@RequestBody PremiseForm createPremiseForm) {
        Premise premise = new Premise();

        mapToEntity(createPremiseForm, premise);

        premiseRepository.save(premise);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity update(@RequestBody UpdatePremiseForm premiseForm) {
        Premise premise = premiseRepository.findById(premiseForm.getId())
                .orElseThrow(() -> new RuntimeException("not found"));
        mapToEntity(premiseForm, premise);

        premiseRepository.save(premise);

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
                .orElseThrow(() -> new RuntimeException("not found"));

        Comment comment = new Comment();
        comment.setCreationDate(LocalDateTime.now());
        comment.setOwner(currentUserService.getCurrentUser());
        comment.setMessage(commentForm.getMessage());
        premise.getComments().add(comment);

        premiseRepository.save(premise);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{premiseId}/comment/{id}")
    public ResponseEntity removeComment(@PathVariable("premiseId") BigInteger premiseId,
                                        @PathVariable("id") BigInteger commentId) {
        Premise premise = premiseRepository.findById(premiseId)
                .orElseThrow(() -> new RuntimeException("no such premise"));

        premise.setComments(premise.getComments()
                .stream()
                .filter(c -> c.getId().equals(commentId))
                .collect(Collectors.toList()));
        return new ResponseEntity(HttpStatus.OK);
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
        premise.setCategories(premiseForm.getCategories());
        premise.setPriceRate(premiseForm.getPriceRate());
    }

}
