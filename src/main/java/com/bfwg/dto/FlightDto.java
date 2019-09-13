package com.bfwg.dto;

import com.bfwg.model.Flight;

public class FlightDto {
    private long id;

    private String name;
    private double price;
    private String brand;
    private String schedule;
    private String description;
    private long tour;

    public FlightDto(Flight flight) {
        this.id = flight.getId();
        this.name = flight.getName();
        this.price = flight.getPrice();
        this.brand = flight.getBrand();
        this.schedule = flight.getSchedule();
        this.description = flight.getDescription();
        this.tour = flight.getTour().getId();
    }

    public FlightDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTour() {
        return tour;
    }

    public void setTour(long tour) {
        this.tour = tour;
    }
}
