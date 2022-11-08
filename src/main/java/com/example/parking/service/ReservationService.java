package com.example.parking.service;

import com.example.parking.entity.Client;
import com.example.parking.entity.ParkingSpot;
import com.example.parking.entity.Reservation;
import com.example.parking.exception.BadRequestException;
import com.example.parking.exception.ForbiddenException;
import com.example.parking.exception.NotFoundException;
import com.example.parking.repository.ClientRepository;
import com.example.parking.repository.ParkingSpotRepository;
import com.example.parking.repository.ReservationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, ClientRepository clientRepository, ParkingSpotRepository parkingSpotRepository) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.parkingSpotRepository = parkingSpotRepository;
    }

    private void validateAndNormalize(Reservation reservation, Reservation reservationFromDB){

        if (reservation.getStartTime() == null || reservation.getEndTime() == null){
            if (reservationFromDB != null) {
                reservation.setStartTime(reservationFromDB.getStartTime());
                reservation.setEndTime(reservationFromDB.getEndTime());
            } else throw new BadRequestException("Time interval doesn't set");
        }
        if (!reservation.getStartTime().isBefore(reservation.getEndTime()))
            throw new ForbiddenException("Impossible time period");


        if (reservation.getClient() != null && reservation.getClient().getId() != null){
            Optional<Client> clientFromDB = clientRepository.findById(reservation.getClient().getId());
            reservation.setClient(clientFromDB.orElseThrow(()-> new ForbiddenException("Reservation client doesn't exist")));
        } else {
            if (reservationFromDB != null) reservation.setClient(reservationFromDB.getClient());
            else throw new BadRequestException("Reservation client or its id equals null");
        }

        if (reservation.getParkingSpot() != null && reservation.getParkingSpot().getId() != null){
            Optional<ParkingSpot> parkingSpotFromDB = parkingSpotRepository.findById(reservation.getParkingSpot().getId());
            reservation.setParkingSpot(parkingSpotFromDB.orElseThrow(()-> new ForbiddenException("Reservation parking spot doesn't exist")));

        } else {
            if (reservationFromDB != null) reservation.setParkingSpot(reservationFromDB.getParkingSpot());
            else throw new BadRequestException("Reservation parking spot or its id equals null");
        }


        List<Reservation> intersections = reservationRepository.findAllByParkingSpotAndEndTimeAfterAndStartTimeBefore(reservation.getParkingSpot(),reservation.getStartTime(), reservation.getEndTime());
        if (intersections.stream().anyMatch((res) -> reservationFromDB == null || !res.getId().equals(reservationFromDB.getId())))
            throw new ForbiddenException("This time is already reserved");

    }
    private void validateAndNormalize(Reservation reservation){
        validateAndNormalize(reservation, null);
    }



    private Double calculatePrice(Reservation reservation){
        reservation.setParkingSpot(parkingSpotRepository.getById(reservation.getParkingSpot().getId()));
        return Math.ceil(
                (float)(ChronoUnit.MINUTES.between(reservation.getStartTime(), reservation.getEndTime())) / 60)
                * reservation.getParkingSpot().getPricePerHour();
    }

    public Reservation findById(Long id){
        return reservationRepository.findById(id).orElseThrow(() -> new NotFoundException("Reservation with id = " + id + " not found"));
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findAll(Client client){return reservationRepository.findAllByClient(client);}

    public List<Reservation> findAll(ParkingSpot parkingSpot){return reservationRepository.findAllByParkingSpot(parkingSpot);}

    public Reservation create(Reservation newReservation) {
        newReservation.setId(null);
        validateAndNormalize(newReservation);

        newReservation.setPrice(calculatePrice(newReservation));
        return reservationRepository.saveAndFlush(newReservation);

    }

    public Reservation updateOrCreate(Reservation newReservation, Long id) {

        Optional<Reservation> reservationFromDB = reservationRepository.findById(id);

        if (reservationFromDB.isPresent()){
            validateAndNormalize(newReservation, reservationFromDB.get());
            newReservation.setPrice(calculatePrice(newReservation));
            BeanUtils.copyProperties(newReservation, reservationFromDB.get(), "id");
            return reservationRepository.saveAndFlush(reservationFromDB.get());

        }
        return create(newReservation);

    }

    public void delete(Reservation reservation) {
        reservationRepository.delete(reservation);
    }
}
