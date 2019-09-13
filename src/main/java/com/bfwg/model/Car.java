package com.bfwg.model;

import com.bfwg.dto.CarDto;

import javax.persistence.*;
import java.util.stream.Stream;

@Entity
@Table(name="CAR")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
//    @NotFound(
//            action = NotFoundAction.IGNORE)
    @JoinColumn(name = "model_id", nullable = false)
    private ModelCar model;
    private String size;
    private int seatingCapacity;
    private int driver;
    private int airConditioner;
    private double price;



    public Car() {
    }

    public Car(CarDto carDto) {
      this.name = carDto.getName();
      this.airConditioner = carDto.getAirConditioner();
      this.driver = carDto.getDriver();
      this.price = carDto.getPrice();
      this.seatingCapacity = carDto.getSeatingCapacity();
      this.size = carDto.getSize();
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

    public ModelCar getModel() {
        return model;
    }

    public void setModel(ModelCar model) {
        this.model = model;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public int getDriver() {
        return driver;
    }

    public void setDriver(int driver) {
        this.driver = driver;
    }

    public int getAirConditioner() {
        return airConditioner;
    }

    public void setAirConditioner(int airConditioner) {
        this.airConditioner = airConditioner;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
