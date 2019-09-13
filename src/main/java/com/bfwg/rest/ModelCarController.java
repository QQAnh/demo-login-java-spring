package com.bfwg.rest;

import com.bfwg.exceptions.ResourceNotFoundException;
import com.bfwg.model.ModelCar;
import com.bfwg.repository.ModelCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping( value = "/api")
public class ModelCarController {
    @Autowired
    private ModelCarRepository modelCarRepository;
    @RequestMapping(value = "/model-car/getAll", method = RequestMethod.GET)
    public Page<ModelCar> getAllPosts(Pageable pageable) {
        return modelCarRepository.findAll(pageable);
    }

    @RequestMapping(value = "/model-car/create", method = RequestMethod.POST)
    public ModelCar createPost(@Valid @RequestBody ModelCar modelCar) {
        return modelCarRepository.save(modelCar);
    }

    @RequestMapping(value = "/model-car/getOne/{id}", method = RequestMethod.GET)
    public ModelCar getModelCarById(@PathVariable Long id){
        return this.modelCarRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Model Car", id)
        );
    }

    @RequestMapping(value = "/model-car/edit/{id}", method = RequestMethod.PUT)
    public ModelCar updateModelCar(@PathVariable Long id, @Valid @RequestBody ModelCar modelCarRequest) {
        return modelCarRepository.findById(id).map(modelCar -> {
            modelCar.setName(modelCarRequest.getName());

            return modelCarRepository.save(modelCar);
        }).orElseThrow(() -> new ResourceNotFoundException("Model Car", id));

    }

    @DeleteMapping("/model-car/delete/{id}")
    public ResponseEntity deleteModelCar(@PathVariable Long id){
        return this.modelCarRepository.findById(id).map((toDelete) -> {
            this.modelCarRepository.delete(toDelete);
            return ResponseEntity.ok("Model Car id " + id + " deleted");
        }).orElseThrow(() -> new ResourceNotFoundException("Model Car", id));
    }
}
