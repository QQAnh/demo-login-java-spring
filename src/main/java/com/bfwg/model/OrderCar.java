package com.bfwg.model;

import com.bfwg.dto.OrderCarDto;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "order_car")
public class OrderCar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @JoinColumn(name = "car_id", nullable = false)
    private Car carId;

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;
    private String season;
    private Date rental_day;
    private Date start_day;


    public OrderCar() {
    }

    public OrderCar(OrderCarDto orderCarDto) {
        this.id = orderCarDto.getId();
        this.rental_day = orderCarDto.getRental_day();
        this.season = orderCarDto.getSeason();
        this.start_day = orderCarDto.getStart_day();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Car getCarId() {
        return carId;
    }

    public void setCarId(Car carId) {
        this.carId = carId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
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
