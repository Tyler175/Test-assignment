package com.example.parking.repository;

import com.example.parking.entity.Client;
import com.example.parking.entity.ParkingSpot;
import com.example.parking.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.lang.NonNull;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @NonNull
    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Reservation> findById(@NonNull Long id);

    @NonNull
    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    List<Reservation> findAll();

    /**
     * Return {@code List} object that contains {@code Reservation}
     * that intersect with given time interval for given {@code ParkingSpot}
     */
    @Lock(LockModeType.PESSIMISTIC_READ)
    List<Reservation> findAllByParkingSpotAndEndTimeAfterAndStartTimeBefore(ParkingSpot parkingSpot, LocalDateTime start, LocalDateTime end);

    @Lock(LockModeType.PESSIMISTIC_READ)
    List<Reservation> findAllByClient(Client client);

    @Lock(LockModeType.PESSIMISTIC_READ)
    List<Reservation> findAllByParkingSpot(ParkingSpot parkingSpot);

    @NonNull
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    <S extends Reservation> S saveAndFlush(@NonNull S s);
}
