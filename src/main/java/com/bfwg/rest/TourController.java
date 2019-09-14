package com.bfwg.rest;

import com.bfwg.dto.CarDto;
import com.bfwg.dto.TourDto;
import com.bfwg.model.Car;
import com.bfwg.model.ModelCar;
import com.bfwg.model.Tour;
import com.bfwg.model.TourType;
import com.bfwg.repository.TourRepository;
import com.bfwg.repository.TourTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping( value = "/api")
public class TourController {
    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private TourTypeRepository tourTypeRepository;
    @GetMapping("/tour/tourType/{tourTypeId}")
    public ResponseEntity<Object> getAllCarByModelId(@PathVariable(value = "tourTypeId") Long tourType,
                                                     Pageable pageable) {
        if (!tourTypeRepository.findById(tourType).isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("TOUR TYPE NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        Page<Tour> tours = tourRepository.findByTourTypeId(tourType, pageable);
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(tours.stream().map(x->new TourDto(x)).collect(Collectors.toList()))
                .build(), HttpStatus.OK);
    }
    @RequestMapping(value = "/tour/create", method = RequestMethod.POST)
    public ResponseEntity<Object> createCar(@Valid @RequestBody TourDto tourDto) {
        Optional<TourType> tourType = tourTypeRepository.findById(tourDto.getTourType());
        if (!tourType.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("MODEL CAR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        Tour tour = new Tour(tourDto);
        tour.setTourType(tourType.get());
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("CREATE SUCCESS!")
                .setData(new TourDto(tourRepository.save(tour)))
                .build(), HttpStatus.OK);
    }
    @RequestMapping(value = "/tour/getAll", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllCar(Pageable pageable) {
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(tourRepository.findAll().stream().map(x -> new TourDto(x)).collect(Collectors.toList()))
                .build(), HttpStatus.OK);
    }
    @RequestMapping(value = "/tour/getOne/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getOneCar(@PathVariable Long id) {
        Optional<Tour> tour = tourRepository.findById(id);
        if (!tour.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        TourDto tourDto = new TourDto(tour.get());
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(tourDto)
                .build(), HttpStatus.OK);
    }

    @RequestMapping(value = "/tour/edit/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> get(@PathVariable Long id,@Valid @RequestBody TourDto tourDto){
        Optional<TourType> tourType = tourTypeRepository.findById(tourDto.getTourType());
        if (!tourType.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("MODEL CAR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        Optional<Tour> tour = tourRepository.findById(id);
        if (!tour.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("TOUR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
//        tour.get().setId(id);
        tour.get().setTitle(tourDto.getTitle());
        tour.get().setPrice(tourDto.getPrice());
        tour.get().setArrangements(tourDto.getArrangements());
        tour.get().setFood(tourDto.getFood());
        tour.get().setTourType(tourType.get());
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("UPDATE SUCCESS!")
                .setData(new TourDto(tourRepository.save(tour.get())))
                .build(), HttpStatus.OK);
    }


}
