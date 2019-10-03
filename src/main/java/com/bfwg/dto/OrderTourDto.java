package com.bfwg.dto;

import com.bfwg.model.OrderTour;

public class OrderTourDto {

    private long id;
    private long userId;
    private long tourId;
    private String season;
    private long groupTypeId;


    public OrderTourDto(OrderTour orderTour) {
        this.id = orderTour.getId();
        this.season = orderTour.getSeason();
        this.userId = orderTour.getUserId().getId();
        this.tourId = orderTour.getTourId().getId();
        this.groupTypeId = orderTour.getGroupTypeId().getId();
    }

    public OrderTourDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTourId() {
        return tourId;
    }

    public void setTourId(long tourId) {
        this.tourId = tourId;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public long getGroupTypeId() {
        return groupTypeId;
    }

    public void setGroupTypeId(long groupTypeId) {
        this.groupTypeId = groupTypeId;
    }
}
