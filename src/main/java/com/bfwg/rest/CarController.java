package com.bfwg.rest;

import com.bfwg.dto.CarDto;
import com.bfwg.exceptions.ResourceNotFoundException;
import com.bfwg.model.Car;
import com.bfwg.model.ModelCar;
import com.bfwg.repository.CarRepository;
import com.bfwg.repository.ModelCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping( value = "/api")
public class CarController {


    @Autowired
    private CarRepository carRepository;
    @Autowired
    private ModelCarRepository modelCarRepository;


    @GetMapping("/car/model/{modelId}")
    public ResponseEntity<Object> getAllCarByModelId(@PathVariable(value = "modelId") Long modelId,
                                            Pageable pageable) {
        Page<Car> car = carRepository.findByModelId(modelId, pageable);
        if (!modelCarRepository.findById(modelId).isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("MODEL CAR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(car.stream().map(x->new CarDto(x)).collect(Collectors.toList()))
                .build(), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/car/create", method = RequestMethod.POST)
    public ResponseEntity<Object> createCar(@Valid @RequestBody CarDto carDto) {
        Optional<ModelCar> modelCar = modelCarRepository.findById(carDto.getModel());
        if (!modelCar.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("MODEL CAR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        Car car = new Car(carDto);
        car.setModel(modelCar.get());
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("CREATE SUCCESS!")
                .setData(new CarDto(carRepository.save(car)))
                .build(), HttpStatus.OK);
    }

    @RequestMapping(value = "/car/getAll", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllCar(Pageable pageable) {
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(carRepository.findAll().stream().map(x -> new CarDto(x)).collect(Collectors.toList()))
                .build(), HttpStatus.OK);
    }

    @RequestMapping(value = "/car/getOne/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getOneCar(@PathVariable Long id) {
        Optional<Car> car = carRepository.findById(id);
        if (!car.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        System.out.println(new CarDto(car.get()));
        CarDto carDto = new CarDto(car.get());

        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(carDto)
                .build(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/car/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> editCar(@PathVariable Long id,@Valid @RequestBody CarDto carDto){
        Optional<ModelCar> modelCar = modelCarRepository.findById(carDto.getModel());
        if (!modelCar.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("MODEL CAR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        Optional<Car> car = carRepository.findById(id);
        if (!car.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("CAR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
//        car.get().setId(id);
        car.get().setName(carDto.getName());
        car.get().setPrice(carDto.getPrice());
        car.get().setSize(carDto.getSize());
        car.get().setAirConditioner(carDto.getAirConditioner());
        car.get().setDriver(carDto.getDriver());
        car.get().setSeatingCapacity(carDto.getSeatingCapacity());
        car.get().setModel(modelCar.get());
        carRepository.save(car.get());

        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("UPDATE SUCCESS!")
                .setData(new CarDto((car.get())))
                .build(), HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('USER')")
//    @RequestMapping(value = "/car/model/{modelId}", method = RequestMethod.POST)
//    public Car createCar(@PathVariable (value = "modelId") Long modelId,
//                                 @Valid @RequestBody Car car) {
//        return modelCarRepository.findById(modelId).map(model -> {
//            car.setModel(model);
//            return carRepository.save(car);
//        }).orElseThrow(() -> new ResourceNotFoundException("Model Id",modelId));
//
////    }
//    @PreAuthorize("hasRole('USER')")
//    @RequestMapping(value = "/car/{carId}/model/{modelId}", method = RequestMethod.PUT)
//    public Car updateCar(@PathVariable (value = "carId") Long carId,
//                                 @PathVariable (value = "modelId") Long modelId,
//                                 @Valid @RequestBody Car carRequest) {
//        if(!modelCarRepository.existsById(carId)) {
//            throw new ResourceNotFoundException("Model Car  ", modelId );
//        }
//        return carRepository.findById(carId).map(car -> {
//            car.setName(carRequest.getName());
//            car.setSize(carRequest.getSize());
//            car.setPrice(carRequest.getPrice());
//            return carRepository.save(car);
//        }).orElseThrow(() -> new ResourceNotFoundException("car " , carId));
//
//    }
//    @PreAuthorize("hasRole('USER')")
//    @RequestMapping(value = "/car/{carId}/comments/{modelId}", method = RequestMethod.DELETE)
//    public ResponseEntity<?> deleteCar(@PathVariable (value = "carId") Long carId,
//                                           @PathVariable (value = "modelId") Long modelId) {
//        return carRepository.findByIdAndModelId(carId, modelId).map(car -> {
//            carRepository.delete(car);
//            return ResponseEntity.ok().build();
//        }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " , carId));
//    }
}
