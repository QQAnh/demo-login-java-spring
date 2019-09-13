package com.bfwg.dto;

import com.bfwg.model.Tour;

public class TourDto {
    private long id;
    private String title;
    private String arrangements;
    private String food;
    private long tourType;
    private Double price;

    public TourDto(Tour tour) {
        this.id = tour.getId();
        this.title = tour.getTitle();
        this.arrangements = tour.getArrangements();
        this.food = tour.getFood();
        this.price = tour.getPrice();
        this.tourType = tour.getTourType().getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArrangements() {
        return arrangements;
    }

    public void setArrangements(String arrangements) {
        this.arrangements = arrangements;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public long getTourType() {
        return tourType;
    }

    public void setTourType(long tourType) {
        this.tourType = tourType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public TourDto() {
    }
}
