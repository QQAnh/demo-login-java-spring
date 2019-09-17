package com.bfwg.rest;

import com.bfwg.dto.CarDto;
import com.bfwg.dto.HotelDto;
import com.bfwg.model.Car;
import com.bfwg.model.Hotel;
import com.bfwg.model.ModelCar;
import com.bfwg.model.Tour;
import com.bfwg.repository.CarRepository;
import com.bfwg.repository.HotelRepository;
import com.bfwg.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping( value = "/api")
public class HotelController {
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private TourRepository tourRepository;

    @RequestMapping(value = "/hotel/getAll", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllHotel(Pageable pageable) {
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(hotelRepository.findAll().stream().map(x -> new HotelDto(x)).collect(Collectors.toList()))
                .build(), HttpStatus.OK);
    }

    @RequestMapping(value = "/hotel/getOne/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getOneHotel(@PathVariable Long id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        if (!hotel.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        HotelDto hotelDto = new HotelDto(hotel.get());

        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(hotelDto)
                .build(), HttpStatus.OK);
    }

    @RequestMapping(value = "/hotel/create", method = RequestMethod.POST)
    public ResponseEntity<Object> createHotel(@Valid @RequestBody HotelDto hotelDto) {
        Optional<Tour> tour = tourRepository.findById(hotelDto.getTourId());
        if (!tour.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("TOUR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        Hotel hotel = new Hotel(hotelDto);
        hotel.setTourId(tour.get());
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("CREATE SUCCESS!")
                .setData(new HotelDto(hotelRepository.save(hotel)))
                .build(), HttpStatus.OK);
    }

    @RequestMapping(value = "/hotel/edit/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> editHotel(@PathVariable Long id,@Valid @RequestBody HotelDto hotelDto){
        Optional<Tour> tour = tourRepository.findById(hotelDto.getTourId());
        if (!tour.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("TOUR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        Optional<Hotel> hotel = hotelRepository.findById(id);
        if (!hotel.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("HOTEL NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        hotel.get().setName(hotelDto.getName());
        hotel.get().setPrice(hotelDto.getPrice());
        hotel.get().setImage(hotelDto.getImage());
        hotel.get().setService(hotelDto.getService());
        hotel.get().setTourId(tour.get());
        hotelRepository.save(hotel.get());
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("UPDATE SUCCESS!")
                .setData(new HotelDto((hotel.get())))
                .build(), HttpStatus.OK);
    }


}
