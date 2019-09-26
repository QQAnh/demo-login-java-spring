package com.bfwg.rest;

import com.bfwg.dto.CarDto;
import com.bfwg.dto.HotelDto;
import com.bfwg.dto.TourDto;
import com.bfwg.model.*;
import com.bfwg.repository.TourRepository;
import com.bfwg.repository.TourTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
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
    public ResponseEntity<Object> getAllTourByTourType(@PathVariable(value = "tourTypeId") Long tourType,
                                                       @RequestParam(defaultValue = "0") String page,
                                                       @RequestParam(defaultValue = "5") String limit) {
        if (!tourTypeRepository.findById(tourType).isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("TOUR TYPE NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        Page<Tour> tours = tourRepository.findByTourTypeId(tourType, PageRequest.of(Integer.parseInt(page), Integer.parseInt(limit)));
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(tours.stream().map(x->new TourDto(x)).collect(Collectors.toList()))
                .setPagination(new RESTPagination(Integer.parseInt(page),
                        Integer.parseInt(limit),
                        tours.getTotalPages(),
                        tours.getNumberOfElements()))
                .build(), HttpStatus.OK);
    }
    @RequestMapping(value = "/tour/create", method = RequestMethod.POST)
    public ResponseEntity<Object> createTour(@Valid @RequestBody TourDto tourDto) {
        Optional<TourType> tourType = tourTypeRepository.findById(tourDto.getTourType());
        if (!tourType.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("TOUR TYPE NOT FOUND!")
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
    public ResponseEntity<Object> getAllTour(
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "5") String limit) {

        List<Tour> tours = tourRepository.findAll();
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(tourRepository.findAll(PageRequest.of(Integer.parseInt(page), Integer.parseInt(limit))).stream().map(x -> new TourDto(x)).collect(Collectors.toList()))
                .setPagination(new RESTPagination(Integer.parseInt(page),
                        Integer.parseInt(limit),
                        tours.size(),
                        tours.size()))
                .build(), HttpStatus.OK);
    }
    @RequestMapping(value = "/tour/getOne/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getOneTour(@PathVariable Long id) {
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
    public ResponseEntity<Object> editTour(@PathVariable Long id,@Valid @RequestBody TourDto tourDto){
        Optional<TourType> tourType = tourTypeRepository.findById(tourDto.getTourType());
        if (!tourType.isPresent()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("TOUR TYPE NOT FOUND!")
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
        tour.get().setImage(tourDto.getImage());
        tour.get().setPrice(tourDto.getPrice());
        tour.get().setArrangements(tourDto.getArrangements());
        tour.get().setFood(tourDto.getFood());
        tour.get().setTourType(tourType.get());
        tour.get().setDuration(tourDto.getDuration());
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("UPDATE SUCCESS!")
                .setData(new TourDto(tourRepository.save(tour.get())))
                .build(), HttpStatus.OK);
    }

    @RequestMapping(value = "/tour/searchByTitle/{title}", method = RequestMethod.GET)
    public ResponseEntity<Object> searchByName(@PathVariable String title,
                                               @RequestParam(defaultValue = "0") String page,
                                               @RequestParam(defaultValue = "5") String limit) {
        Page<Tour> tours = tourRepository.findByTitle(title,PageRequest.of(Integer.parseInt(page), Integer.parseInt(limit)));
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(tours.stream().map(x->new TourDto(x)).collect(Collectors.toList()))
                .setPagination(new RESTPagination(Integer.parseInt(page),
                        Integer.parseInt(limit),
                        tours.getTotalPages(),
                        tours.getTotalElements()))
                .build(), HttpStatus.OK);
    }

    @RequestMapping(value = "/tour/searchByPrice/{price}", method = RequestMethod.GET)
    public ResponseEntity<Object> searchByPrice(@PathVariable Double price,
                                                @RequestParam(defaultValue = "0") String page,
                                                @RequestParam(defaultValue = "5") String limit) {
        Page<Tour> tours = tourRepository.findByPrice(price,PageRequest.of(Integer.parseInt(page), Integer.parseInt(limit)));
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(tours.stream().map(x->new TourDto(x)).collect(Collectors.toList()))
                .setPagination(new RESTPagination(Integer.parseInt(page),
                        Integer.parseInt(limit),
                        tours.getTotalPages(),
                        tours.getTotalElements()))
                .build(), HttpStatus.OK);
    }

    @RequestMapping(value = "/tour/searchByLocation/{location}", method = RequestMethod.GET)
    public ResponseEntity<Object> searchByLocation(@PathVariable String location,
                                                   @RequestParam(defaultValue = "0") String page,
                                                   @RequestParam(defaultValue = "5") String limit) {
        Page<Tour> tours = tourRepository.findByLocation(location,PageRequest.of(Integer.parseInt(page), Integer.parseInt(limit)));
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(tours.stream().map(x->new TourDto(x)).collect(Collectors.toList()))
                .setPagination(new RESTPagination(Integer.parseInt(page),
                        Integer.parseInt(limit),
                        tours.getTotalPages(),
                        tours.getTotalElements()))
                .build(), HttpStatus.OK);
    }
    @RequestMapping(value = "/tour/searchByTourTypeName/{name}", method = RequestMethod.GET)
    public ResponseEntity<Object> searchByTourTypeName(@PathVariable(value = "name") String name,
                                                       @RequestParam(defaultValue = "0") String page,
                                                       @RequestParam(defaultValue = "5") String limit) {
        if (tourTypeRepository.findByName(name).isEmpty()){
            return new ResponseEntity<>(new RESTResponse.Success()
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .setMessage("TOUR TYPE NOT FOUND!")
                    .setData(null)
                    .build(), HttpStatus.NOT_FOUND);
        }


        Page<Tour> tours = tourRepository.findByTourTypeName(name,PageRequest.of(Integer.parseInt(page), Integer.parseInt(limit)));
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(tours.stream().map(x->new TourDto(x)).collect(Collectors.toList()))
                .setPagination(new RESTPagination(Integer.parseInt(page),
                        Integer.parseInt(limit),
                        tours.getTotalPages(),
                        tours.getTotalElements()))
                .build(), HttpStatus.OK);
    }
    @RequestMapping(value = "/tour/search", method = RequestMethod.GET)
    public ResponseEntity<Object> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") String priceMin,
            @RequestParam(defaultValue = "10000000000000") String priceMax) {

        List<Tour> tours = tourRepository.findAllByTitleAndLocationAndPriceBetween(title,location,Double.valueOf(priceMin),Double.valueOf(priceMax));
        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(tours.stream().map(x -> new TourDto(x)).collect(Collectors.toList()))

                .build(), HttpStatus.OK);
    }


    @RequestMapping(value = "/tour/getAll-test", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllTourTest() {

        return new ResponseEntity<>(new RESTResponse.Success()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Success!")
                .setData(tourRepository.findAll().stream().map(x -> new TourDto(x.getId(),x.getTitle(),x.getTourType().getId())).collect(Collectors.toList()))
                .build(), HttpStatus.OK);
    }



}
