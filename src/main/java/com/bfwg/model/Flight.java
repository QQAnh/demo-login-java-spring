//package com.bfwg.model;
//
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "flight")
//public class Flight {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Integer id;
//
//    private String name;
//    private double price;
//    private String brand;
//    private String schedule;
//    private String description;
//    private Tour tourId;
//
//
//    public Flight() {
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    public String getBrand() {
//        return brand;
//    }
//
//    public void setBrand(String brand) {
//        this.brand = brand;
//    }
//
//    public String getSchedule() {
//        return schedule;
//    }
//
//    public void setSchedule(String schedule) {
//        this.schedule = schedule;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public Tour getTourId() {
//        return tourId;
//    }
//
//    public void setTourId(Tour tourId) {
//        this.tourId = tourId;
//    }
//}
