package com.bfwg.rest;


import com.bfwg.dto.OrderTourDto;
import com.bfwg.model.GroupType;
import com.bfwg.model.OrderTour;
import com.bfwg.model.Tour;
import com.bfwg.model.User;
import com.bfwg.repository.GroupTypeRepository;
import com.bfwg.repository.OrderTourRepository;
import com.bfwg.repository.TourRepository;
import com.bfwg.service.UserService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class OrderTourController {
    @Autowired
    private OrderTourRepository orderTourRepository;
    @Autowired
    private GroupTypeRepository groupTypeRepository;
    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/orderTour/create", method = RequestMethod.POST)
    public ResponseEntity<Object> createTour(Principal user, @Valid @RequestBody OrderTourDto orderTourDto) {
        Optional<GroupType> groupType = groupTypeRepository.findById(orderTourDto.getGroupTypeId());
        if (!groupType.isPresent()) {
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("GROUP TYPE NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        Optional<Tour> tour = tourRepository.findById(orderTourDto.getTourId());
        if (!tour.isPresent()) {
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("TOUR NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }

        User userId = this.userService.findByUsername(user.getName());

        OrderTour orderTour = new OrderTour(orderTourDto);
        orderTour.setTourId(tour.get());
        orderTour.setUserId(userId);
        orderTour.setGroupTypeId(groupType.get());
        orderTour.setSeason("PENDING");

        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("CREATE SUCCESS!")
                .setData(new OrderTourDto(orderTourRepository.save(orderTour)))
                .build(), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/orderTour/getOne/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getOneTour(Principal user,@PathVariable Long id) {
        Optional<OrderTour> orderTour = orderTourRepository.findById(id);
        User userId = this.userService.findByUsername(user.getName());

        if (!orderTour.isPresent() || orderTour.get().getUserId().getId() != userId.getId()) {
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        OrderTourDto orderTourDto = new OrderTourDto(orderTour.get());
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(orderTourDto)
                .build(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/orderTour/getAll", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllTour(
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "5") String limit) {

        List<OrderTour> orderTours = orderTourRepository.findAll();
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(orderTourRepository.findAll(PageRequest.of(Integer.parseInt(page), Integer.parseInt(limit))).stream().map(x -> new OrderTourDto(x)).collect(Collectors.toList()))
                .setPagination(new RESTPagination(Integer.parseInt(page),
                        Integer.parseInt(limit),
                        orderTours.size(),
                        orderTours.size()))
                .build(), HttpStatus.OK);
    }


}
