package com.example.parking.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "parking_spot", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"address", "number"})})
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Basic.class)
    private Long id;

    @NotBlank
    @Size(max = 200)
    @JsonView(Views.Basic.class)
    private String address;

    @NotNull
    @Min(value = 1, message = "Number should be greater than 0")
    @JsonView(Views.Basic.class)
    private int number;

    @NotNull
    @DecimalMin(value = "0.0", message = "Price should be at least 0")
    @JsonView(Views.Basic.class)
    private Double pricePerHour;

    @OneToMany(mappedBy = "parkingSpot", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIdentityReference
    @JsonIdentityInfo(
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class
    )
    @JsonView(Views.FullClass.class)
    private Set<Reservation> reservations = new HashSet<>();

    public ParkingSpot() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(Double pricePerMinute) {
        this.pricePerHour = pricePerMinute;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
}
