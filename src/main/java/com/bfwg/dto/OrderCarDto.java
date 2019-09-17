package com.bfwg.dto;
import com.bfwg.model.OrderCar;

import java.util.Date;

public class OrderCarDto {
    private long id;
    private long carId;
    private long userId;
    private String season;
    private Date rental_day;
    private Date start_day;

    public OrderCarDto() {
    }

    public OrderCarDto(OrderCar orderCar) {
        this.id = orderCar.getId();
        this.carId = orderCar.getCarId().getId();
        this.userId = orderCar.getUserId().getId();
        this.season = orderCar.getSeason();
        this.rental_day = orderCar.getRental_day();
        this.start_day = orderCar.getStart_day();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public Date getRental_day() {
        return rental_day;
    }

    public void setRental_day(Date rental_day) {
        this.rental_day = rental_day;
    }

    public Date getStart_day() {
        return start_day;
    }

    public void setStart_day(Date start_day) {
        this.start_day = start_day;
    }
}
