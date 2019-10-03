package com.bfwg.model;


import com.bfwg.dto.OrderTourDto;

import javax.persistence.*;

@Entity
@Table(name = "order_tour")
public class OrderTour {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @JoinColumn(name = "userId", nullable = false)
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @JoinColumn(name = "tourId", nullable = false)
    private Tour tourId;

    private String season;
    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @JoinColumn(name = "groupTypeId", nullable = false)
    private GroupType groupTypeId;

    public OrderTour(OrderTourDto orderTourDto) {
        this.id = orderTourDto.getId();
        this.season = orderTourDto.getSeason();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Tour getTourId() {
        return tourId;
    }

    public void setTourId(Tour tourId) {
        this.tourId = tourId;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public GroupType getGroupTypeId() {
        return groupTypeId;
    }

    public void setGroupTypeId(GroupType groupTypeId) {
        this.groupTypeId = groupTypeId;
    }
}
