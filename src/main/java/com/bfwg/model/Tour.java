package com.bfwg.model;


import com.bfwg.dto.TourDto;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tours")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String arrangements;
    private String food;

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @JoinColumn(name = "tourType", nullable = false)
    private TourType tourType;
    private Double price;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tour")
    private Set<Flight> flights;

    public Tour() {
    }

    public Tour(TourDto tourDto) {
        this.id = tourDto.getId();
        this.title = tourDto.getTitle();
        this.arrangements = tourDto.getArrangements();
        this.food = tourDto.getFood();
        this.price = tourDto.getPrice();
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


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public TourType getTourType() {
        return tourType;
    }

    public void setTourType(TourType tourType) {
        this.tourType = tourType;
    }
}
