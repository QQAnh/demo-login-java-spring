package com.bfwg.rest;

import com.bfwg.dto.CarDto;
import com.bfwg.mail.MailController;
import com.bfwg.model.Car;
import com.bfwg.model.ModelCar;
import com.bfwg.repository.CarRepository;
import com.bfwg.repository.ModelCarRepository;
import javafx.scene.control.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class CarController {

    @Autowired
    private MailController mailController;

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private ModelCarRepository modelCarRepository;


    @GetMapping("/car/model/{modelId}")
    public ResponseEntity<Object> getAllCarByModelId(@PathVariable(value = "modelId") Long modelId,
                                                     Pageable pageable) {
        Page<Car> car = carRepository.findByModelId(modelId, pageable);
        if (!modelCarRepository.findById(modelId).isPresent()) {
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("MODEL CAR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(car.stream().map(x -> new CarDto(x)).collect(Collectors.toList()))
                .build(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/car/create", method = RequestMethod.POST)
    public ResponseEntity<Object> createCar(@Valid @RequestBody CarDto carDto) {
        Optional<ModelCar> modelCar = modelCarRepository.findById(carDto.getModel());
        if (!modelCar.isPresent()) {
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

    //    @PreAuthorize("hasRole('USER')")
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
        if (!car.isPresent()) {
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

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/car/edit/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> editCar(@PathVariable Long id, @Valid @RequestBody CarDto carDto) {
        Optional<ModelCar> modelCar = modelCarRepository.findById(carDto.getModel());
        if (!modelCar.isPresent()) {
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("MODEL CAR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        Optional<Car> car = carRepository.findById(id);
        if (!car.isPresent()) {
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("CAR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        car.get().setName(carDto.getName());
        car.get().setPrice(carDto.getPrice());
        car.get().setSize(carDto.getSize());
        car.get().setImage(carDto.getImage());
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


    @RequestMapping(value = "/car/getAllss", method = RequestMethod.GET)
    public ResponseEntity<Object> getAll(Pageable pageable) {
        System.out.println("Sending Email...");

        try {
            //sendEmail();
            mailController.sendEmailWithAttachment("asdasdasd");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(carRepository.findAll().stream().map(x -> new CarDto(x)).collect(Collectors.toList()))
                .build(), HttpStatus.OK);
    }

    @RequestMapping(value = "/car/searchByName/{name}", method = RequestMethod.GET)
    public ResponseEntity<Object> searchByName(@PathVariable String name, Pageable pageable) {
        Page<Car> car = carRepository.findByName(name, pageable);
        CarDto carDto = new CarDto(car);
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(carDto)
                .setPagination(new RESTPagination(pageable.getPageNumber(),
                        pageable.getPageSize(),
                        car.getTotalPages(),
                        car.getNumberOfElements()))
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
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/car/{carId}/comments/{modelId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCar(@PathVariable (value = "carId") Long carId,
                                           @PathVariable (value = "modelId") Long modelId) {
        Optional<Car> car = carRepository.findById(carId);
        if (!car.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("CAR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        carRepository.delete(car.get());
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(null)
                .build(), HttpStatus.OK);
    }
}
