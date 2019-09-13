package com.bfwg.rest;

import com.bfwg.exceptions.ResourceNotFoundException;
import com.bfwg.model.Car;
import com.bfwg.model.ModelCar;
import com.bfwg.repository.CarRepository;
import com.bfwg.repository.ModelCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping( value = "/api")
public class CarController {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private ModelCarRepository modelCarRepository;
    @GetMapping("/car/{modelId}/model")
    public Page<Car> getAllCarByModelId(@PathVariable(value = "modelId") Long modelId,
                                            Pageable pageable) {
        return carRepository.findByModelId(modelId, pageable);
    }

    @RequestMapping(value = "/car/create", method = RequestMethod.POST)
    public Car createCar(@Valid @RequestBody Car car) {
        return carRepository.save(car);
    }

    @RequestMapping(value = "/car/getAll", method = RequestMethod.GET)
    public Page<Car> getAllCar(Pageable pageable) {
        return carRepository.findAll(pageable);
    }



    @RequestMapping(value = "/car/getOne/{id}", method = RequestMethod.GET)
    public Car getCarById(@PathVariable Long id){
        return this.carRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Car Id ", id)
        );
    }

    @RequestMapping(value = "/car/{id}", method = RequestMethod.PUT)
    public Car get(@PathVariable Long id,@Valid @RequestBody Car carRequest){
//        System.out.println(carRequest.getModel().getName());
        System.out.println(this.carRepository.findById(id).get().getModel().getName());
        return carRepository.findById(id).map(car -> {
//            car.setName(carRequest.getName());
//            car.setSize(carRequest.getSize());
//            car.setBrand(carRequest.getBrand());
//            car.setPrice(carRequest.getPrice());
            return carRepository.save(car);
        }).orElseThrow(() -> new ResourceNotFoundException("car " , id));
    }

    @RequestMapping(value = "/car/model/{modelId}", method = RequestMethod.POST)
    public Car createCar(@PathVariable (value = "modelId") Long modelId,
                                 @Valid @RequestBody Car car) {
        return modelCarRepository.findById(modelId).map(model -> {
            car.setModel(model);
            return carRepository.save(car);
        }).orElseThrow(() -> new ResourceNotFoundException("Model Id",modelId));

    }

    @RequestMapping(value = "/car/{carId}/model/{modelId}", method = RequestMethod.PUT)
    public Car updateCar(@PathVariable (value = "carId") Long carId,
                                 @PathVariable (value = "modelId") Long modelId,
                                 @Valid @RequestBody Car carRequest) {
        if(!modelCarRepository.existsById(carId)) {
            throw new ResourceNotFoundException("Model Car  ", modelId );
        }
        return carRepository.findById(carId).map(car -> {
            car.setName(carRequest.getName());
            car.setSize(carRequest.getSize());
            car.setPrice(carRequest.getPrice());
            return carRepository.save(car);
        }).orElseThrow(() -> new ResourceNotFoundException("car " , carId));

    }
    @RequestMapping(value = "/car/{carId}/comments/{modelId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCar(@PathVariable (value = "carId") Long carId,
                                           @PathVariable (value = "modelId") Long modelId) {
        return carRepository.findByIdAndModelId(carId, modelId).map(car -> {
            carRepository.delete(car);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " , carId));
    }
}
