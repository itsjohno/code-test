package io.github.itsjohno.codetest.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.github.itsjohno.codetest.models.Dog;
import io.github.itsjohno.codetest.repository.DogRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/dogs")
public class DogsController {

    private DogRepository dogRepository;

    public DogsController(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    @GetMapping
    public Iterable<Dog> getDogs() {
        return dogRepository.findAll();
    }

    @PostMapping
    @RequestMapping(value = "/upload")
    public void createDogs(@RequestBody JsonNode jsonObject) {

        List<Dog> dogsToCreate = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> fields = jsonObject.fields();
        fields.forEachRemaining(entry -> {
            Dog tempDog = new Dog();

            // The KEY is the breed name
            tempDog.setBreed(entry.getKey());

            // The VALUE is an array of strings for each type
            entry.getValue().elements().forEachRemaining(value -> tempDog.addType(value.asText()));

            dogsToCreate.add(tempDog);
        });

        dogRepository.saveAll(dogsToCreate);
    }
}
