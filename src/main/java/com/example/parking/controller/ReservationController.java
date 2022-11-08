package com.example.parking.controller;

import com.example.parking.entity.Client;
import com.example.parking.entity.ParkingSpot;
import com.example.parking.entity.Reservation;
import com.example.parking.entity.Views;
import com.example.parking.service.ReservationService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    @JsonView(Views.Basic.class)
    Reservation findById(@Valid @PathVariable("id") Long id) {
        return reservationService.findById(id);
    }

    @GetMapping
    @JsonView(Views.Basic.class)
    List<Reservation> findAll() {
        return reservationService.findAll();
    }

    @GetMapping(params = "client")
    @JsonView(Views.Basic.class)
    List<Reservation> findAll(@RequestParam("client") Client client) {
        return reservationService.findAll(client);
    }

    @GetMapping(params = "parkingSpot")
    @JsonView(Views.Basic.class)
    List<Reservation> findAll(@RequestParam("parkingSpot")ParkingSpot parkingSpot) {
        return reservationService.findAll(parkingSpot);
    }


    @PostMapping
    @JsonView(Views.Basic.class)
    Reservation create(@Valid @RequestBody Reservation newReservation) {
        return reservationService.create(newReservation);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Basic.class)
    Reservation put(@RequestBody Reservation newReservation, @PathVariable("id") Long id) {
        return reservationService.updateOrCreate(newReservation, id);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Reservation reservation) {
        reservationService.delete(reservation);
    }
}
