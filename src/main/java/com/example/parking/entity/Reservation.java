package com.example.parking.entity;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Basic.class)
    private Long id;

    @DateTimeFormat(pattern="dd-MM-yyyy hh:mm:ss")
    @JsonView(Views.Basic.class)
    private LocalDateTime startTime;

    @DateTimeFormat(pattern="dd-MM-yyyy hh:mm:ss")
    @JsonView(Views.Basic.class)
    private LocalDateTime endTime;

    @JsonView(Views.Basic.class)
    private double price;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @JsonView(Views.Basic.class)
    private Client client;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "parking_spot_id", nullable = false)
    @JsonView(Views.Basic.class)
    private ParkingSpot parkingSpot;

    public Reservation() {
    }

    public void copyNotNullPropertiesFrom(Reservation reservation){
        id = reservation.getId() != null ? reservation.getId() : id;
        startTime = reservation.getStartTime() != null ? reservation.getStartTime() : startTime;
        endTime = reservation.getEndTime() != null ? reservation.getEndTime() : endTime;
        price = reservation.getPrice();
        client = reservation.getClient() != null ? reservation.getClient() : client;
        parkingSpot = reservation.getParkingSpot() != null ? reservation.getParkingSpot() : parkingSpot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }


}
