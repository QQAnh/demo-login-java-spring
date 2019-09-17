package com.bfwg.rest;


import com.bfwg.dto.CarDto;
import com.bfwg.dto.HotelDto;
import com.bfwg.dto.OrderCarDto;
import com.bfwg.model.Car;
import com.bfwg.model.ModelCar;
import com.bfwg.model.OrderCar;
import com.bfwg.model.User;
import com.bfwg.repository.CarRepository;
import com.bfwg.repository.OrderCarRepository;
import com.bfwg.repository.UserRepository;
import com.bfwg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping( value = "/api")
public class OrderCarController {
    @Autowired
    private OrderCarRepository orderCarRepository;

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/orderCar/getAll", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllOrderCar(Pageable pageable) {
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(orderCarRepository.findAll().stream().map(x -> new OrderCarDto(x)).collect(Collectors.toList()))
                .build(), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/orderCar/getOne/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getOneOrderCar(Principal user,@PathVariable Long id) {
        Optional<OrderCar> orderCar = orderCarRepository.findById(id);

        if (!orderCar.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }

        OrderCarDto orderCarDto = new OrderCarDto(orderCar.get());
        if (userRepository.findByUsername(user.getName()).getId() != userRepository.findById(orderCarDto.getUserId()).get().getId()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.UNAUTHORIZED.value())
                    .setMessage("UNAUTHORIZED!")
                    .setData(null)
                    .build(), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(orderCarDto)
                .build(), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/orderCar/create", method = RequestMethod.POST)
    public ResponseEntity<Object> createCar(Principal user, @Valid @RequestBody OrderCarDto orderCarDto) {
        Optional<Car> car = carRepository.findById(orderCarDto.getCarId());
        if (!car.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("MODEL CAR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }


        OrderCar orderCar = new OrderCar(orderCarDto);
        orderCar.setCarId(car.get());
        orderCar.setUserId(userRepository.findByUsername(user.getName()));

        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("CREATE SUCCESS!")
                .setData(new OrderCarDto(orderCarRepository.save(orderCar)))
                .build(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/orderCar/edit/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> editOrderCar(Principal user,@PathVariable Long id,@Valid @RequestBody OrderCarDto orderCarDto){
        Optional<Car> car = carRepository.findById(orderCarDto.getCarId());
        if (!car.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("CAR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        Optional<User> user2 = userRepository.findById(orderCarDto.getUserId());
        if (!user2.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("USER NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        Optional<OrderCar> orderCar = orderCarRepository.findById(id);
        if (!orderCar.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("ORDER CAR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }

        orderCar.get().setSeason(orderCarDto.getSeason());
        orderCar.get().setRental_day(orderCarDto.getRental_day());
        orderCar.get().setStart_day(orderCarDto.getStart_day());
        orderCar.get().setCarId(car.get());

        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("UPDATE SUCCESS!")
                .setData(new OrderCarDto(orderCarRepository.save(orderCar.get())))
                .build(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/orderCar/getOneAdmin/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getOneOrderCarAdmin(@PathVariable Long id) {
        Optional<OrderCar> orderCar = orderCarRepository.findById(id);
        if (!orderCar.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        OrderCarDto orderCarDto = new OrderCarDto(orderCar.get());
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(orderCarDto)
                .build(), HttpStatus.OK);
    }


}
