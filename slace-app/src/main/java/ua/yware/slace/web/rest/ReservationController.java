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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import ua.yware.slace.dao.ReservationRepository;
import ua.yware.slace.model.Premise;
import ua.yware.slace.model.Reservation;
import ua.yware.slace.model.User;
import ua.yware.slace.model.enums.ReservationStatus;
import ua.yware.slace.service.dto.ReservationDto;
import ua.yware.slace.service.premise.PremiseService;
import ua.yware.slace.service.user.CurrentUserService;
import ua.yware.slace.web.exception.ResourceNotFoundException;
import ua.yware.slace.web.rest.form.BookPremiseForm;

@RestController
@RequestMapping("${api.prefix}/premises")
@RequiredArgsConstructor
public class ReservationController {

    private final PremiseService premiseService;
    private final CurrentUserService currentUserService;
    private final ReservationRepository reservationRepository;

    @GetMapping("/{id}/reservations/nearest")
    public List<ReservationDto> getNearestReservations(@PathVariable("id") BigInteger id) {
        LocalDateTime now = LocalDateTime.now();
        return reservationRepository.findAllByPremiseAndFromAfterAndToBefore(
                premiseService.getById(id), now.minusMonths(1), now.plusMonths(1))
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/reservations")
    public List<ReservationDto> getReservations(@PathVariable("id") BigInteger id) {
        return premiseService.getById(id).getReservations().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional
    @PostMapping("/{id}/reserve")
    public ResponseEntity makeReservation(@RequestBody BookPremiseForm bookPremiseForm) {
        User currentUser = currentUserService.getCurrentUser();
        Premise premise = premiseService.getById(bookPremiseForm.getPremiseId());

        List<Reservation> reservations = reservationRepository.findAllByPremiseAndFromAfterAndToBefore(
                premise, bookPremiseForm.getFrom(), bookPremiseForm.getTo());
        if (!reservations.isEmpty()) {
            return new ResponseEntity<>("Already booked", HttpStatus.BAD_REQUEST);
        }

        Reservation premiseReservation = new Reservation();
        premiseReservation.setFrom(bookPremiseForm.getFrom());
        premiseReservation.setTo(bookPremiseForm.getTo());
        premiseReservation.setUser(currentUser);
        premiseReservation.setPremise(premise);
        premiseReservation.setPriceRate(premise.getPriceRate());

        premise.getReservations().add(premiseReservation);
        premiseService.save(premise);
        reservationRepository.save(premiseReservation);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional
    @PostMapping("/{premiseId}/reservations/{id}/cancel")
    public ResponseEntity cancelReservation(@PathVariable("id") BigInteger id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found!"));
        reservation.setReservationStatus(ReservationStatus.ARCHIVED);
        reservationRepository.save(reservation);

        return new ResponseEntity(HttpStatus.OK);
    }

    private ReservationDto mapToDto(Reservation reservation) {
        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId());
        dto.setFrom(reservation.getFrom());
        dto.setTo(reservation.getTo());
        dto.setPriceRate(reservation.getPriceRate());
        dto.setReservationStatus(reservation.getReservationStatus());
        return dto;
    }

}
