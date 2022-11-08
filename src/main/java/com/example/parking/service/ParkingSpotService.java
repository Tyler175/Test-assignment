package com.example.parking.service;

import com.example.parking.entity.ParkingSpot;
import com.example.parking.exception.NotFoundException;
import com.example.parking.repository.ParkingSpotRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;

    @Autowired
    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    public ParkingSpot findById(Long id){
        return parkingSpotRepository.findById(id).orElseThrow(() -> new NotFoundException("Parking spot with id = " + id + " not found"));
    }

    public List<ParkingSpot> findAll() {
        return parkingSpotRepository.findAll();
    }

    public List<ParkingSpot> findAll(String address) {
        return parkingSpotRepository.findAllByAddressStartsWithIgnoreCase(address);
    }

    public ParkingSpot create(ParkingSpot newParkingSpot) {
        newParkingSpot.setId(null);
        return parkingSpotRepository.saveAndFlush(newParkingSpot);
    }

    public ParkingSpot updateOrCreate(ParkingSpot newParkingSpot, Long id) {
        Optional<ParkingSpot> parkingSpotFromDB = parkingSpotRepository.findById(id);
        if (parkingSpotFromDB.isPresent()){
            BeanUtils.copyProperties(newParkingSpot, parkingSpotFromDB.get(), "id");
            return parkingSpotRepository.saveAndFlush(parkingSpotFromDB.get());
        }
        return create(newParkingSpot);
    }

    public void delete(ParkingSpot client) {
        parkingSpotRepository.delete(client);
    }
}
