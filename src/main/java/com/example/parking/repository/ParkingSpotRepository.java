package com.example.parking.repository;

import com.example.parking.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    @Override
    Optional<ParkingSpot> findById(Long id);

    @Override
    List<ParkingSpot> findAll();

    List<ParkingSpot> findAllByAddressStartsWithIgnoreCase(String address);

}
