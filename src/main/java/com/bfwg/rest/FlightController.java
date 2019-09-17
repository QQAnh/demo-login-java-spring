package com.bfwg.rest;


import com.bfwg.dto.CarDto;
import com.bfwg.dto.FlightDto;
import com.bfwg.dto.TourDto;
import com.bfwg.model.Car;
import com.bfwg.model.Flight;
import com.bfwg.model.Tour;
import com.bfwg.model.TourType;
import com.bfwg.repository.FlightRepository;
import com.bfwg.repository.TourRepository;
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
public class FlightController {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private TourRepository tourRepository;

    @GetMapping("/flight/tour/{tourId}")
    public ResponseEntity<Object> getAllFlightByTour(@PathVariable(value = "tourId") Long tour,
                                                     Pageable pageable) {
        if (!tourRepository.findById(tour).isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("TOUR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        Page<Flight> flights = flightRepository.findByTourId(tour, pageable);
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("SUCCESS!")
                .setData(flights.stream().map(x->new FlightDto(x)).collect(Collectors.toList()))
                .build(), HttpStatus.OK);
    }

    @RequestMapping(value = "/flight/create", method = RequestMethod.POST)
    public ResponseEntity<Object> createFlight(@Valid @RequestBody FlightDto flightDto) {
        Optional<Tour> tour = tourRepository.findById(flightDto.getTour());
        if (!tour.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("TOUR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        Flight flight = new Flight(flightDto);
        flight.setTour(tour.get());
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("CREATE SUCCESS!")
                .setData(new FlightDto(flightRepository.save(flight)))
                .build(), HttpStatus.OK);
    }

    @RequestMapping(value = "/flight/getAll", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllFlight(Pageable pageable) {
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("SUCCESS!")
                .setData(flightRepository.findAll().stream().map(x -> new FlightDto(x)).collect(Collectors.toList()))
                .build(), HttpStatus.OK);
    }
    @RequestMapping(value = "/flight/getOne/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getOneFlight(@PathVariable Long id) {
        Optional<Flight> flight = flightRepository.findById(id);
        if (!flight.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        System.out.println(new FlightDto(flight.get()));
        FlightDto flightDto = new FlightDto(flight.get());
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("SUCCESS!")
                .setData(flightDto)
                .build(), HttpStatus.OK);
    }


    @RequestMapping(value = "/flight/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> editFlight(@PathVariable Long id,@Valid @RequestBody FlightDto flightDto){
        Optional<Tour> tour = tourRepository.findById(flightDto.getTour());
        if (!tour.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("TOUR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        Optional<Flight> flight = flightRepository.findById(id);
        if (!flight.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("FLIGHT NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
//        flight.get().setId(id);
        flight.get().setName(flightDto.getName());
        flight.get().setImage(flightDto.getImage());
        flight.get().setPrice(flightDto.getPrice());
        flight.get().setBrand(flightDto.getBrand());
        flight.get().setDescription(flightDto.getDescription());
        flight.get().setSchedule(flightDto.getSchedule());
        flight.get().setTour(tour.get());
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("UPDATE SUCCESS!")
                .setData(new FlightDto(flightRepository.save(flight.get())))
                .build(), HttpStatus.OK);
    }

}
