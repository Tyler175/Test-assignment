package com.example.parking.controller;

import com.example.parking.entity.ParkingSpot;
import com.example.parking.entity.Views;
import com.example.parking.service.ParkingSpotService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("parking-spots")
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    @Autowired
    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @GetMapping("/{id}")
    @JsonView(Views.FullClass.class)
    ParkingSpot findById(@PathVariable("id") Long id) {
        return parkingSpotService.findById(id);
    }

    @GetMapping
    @JsonView(Views.Basic.class)
    List<ParkingSpot> findAll() {
        return parkingSpotService.findAll();
    }

    @GetMapping(params = "address")
    @JsonView(Views.FullClass.class)
    List<ParkingSpot> findByAddress(@RequestParam("address") String address){
        return parkingSpotService.findAll(address);
    }

    @PostMapping
    ParkingSpot create(@RequestBody ParkingSpot newParkingSpot) {
        return parkingSpotService.create(newParkingSpot);
    }

    @PutMapping("/{id}")
    ParkingSpot update(@RequestBody ParkingSpot newParkingSpot, @PathVariable("id") Long id) {
        return parkingSpotService.updateOrCreate(newParkingSpot, id);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") ParkingSpot client) {
        parkingSpotService.delete(client);
    }
}
