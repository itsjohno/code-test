package io.github.itsjohno.codetest.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.itsjohno.codetest.models.Dog;
import io.github.itsjohno.codetest.repository.DogRepository;

import java.util.*;

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

    @GetMapping(value = "/{dogBreed}")
    public ResponseEntity<Dog> getDog(@PathVariable String dogBreed) {
        Optional<Dog> foundDog = dogRepository.findById(dogBreed);

        if (foundDog.isPresent()) {
            return ResponseEntity.ok(foundDog.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{breed}")
    public ResponseEntity deleteDog(@PathVariable String breed) {
        dogRepository.deleteById(breed);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{breed}/{type}")
    public ResponseEntity deleteDog(@PathVariable String breed, @PathVariable String type) {
        Optional<Dog> foundDog = dogRepository.findById(breed);

        if (foundDog.isPresent()) {
            Dog dog = foundDog.get();
            if (dog.getTypes().contains(type)) {
                dog.getTypes().remove(type);
            }
            dogRepository.save(dog);
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void createDogs(@RequestBody JsonNode jsonObject) {

        List<Dog> dogsToCreate = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            dogsToCreate.addAll(Arrays.asList(objectMapper.convertValue(jsonObject, Dog[].class)));
        }
        catch (Exception exception) {
            // We've blown up during deserialisation. Let's just move on to see if it's other format.
        }

        if (0 == dogsToCreate.size()) {
            // No dogs have been created, let's see if we've been passed in a non-API format.

            Iterator<Map.Entry<String, JsonNode>> fields = jsonObject.fields();
            fields.forEachRemaining(entry -> {
                Dog tempDog = new Dog();

                // The KEY is the breed name
                tempDog.setBreed(entry.getKey());

                // The VALUE is an array of strings for each type
                entry.getValue().elements().forEachRemaining(value -> tempDog.addType(value.asText()));

                dogsToCreate.add(tempDog);
            });
        }

        dogRepository.saveAll(dogsToCreate);
    }
}
