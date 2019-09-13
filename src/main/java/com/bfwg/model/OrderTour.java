//package com.bfwg.model;
//
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "order_tour")
//public class OrderTour {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Integer id;
//
//    private User userId;
//    private Tour tourId;
//    private String season;
//    private GroupType groupTypeId;
//
//    public OrderTour() {
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
//    public User getUserId() {
//        return userId;
//    }
//
//    public void setUserId(User userId) {
//        this.userId = userId;
//    }
//
//    public Tour getTourId() {
//        return tourId;
//    }
//
//    public void setTourId(Tour tourId) {
//        this.tourId = tourId;
//    }
//
//    public String getSeason() {
//        return season;
//    }
//
//    public void setSeason(String season) {
//        this.season = season;
//    }
//
//    public GroupType getGroupTypeId() {
//        return groupTypeId;
//    }
//
//    public void setGroupTypeId(GroupType groupTypeId) {
//        this.groupTypeId = groupTypeId;
//    }
//}
