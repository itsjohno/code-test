package io.github.itsjohno.codetest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.itsjohno.codetest.model.Dog;
import io.github.itsjohno.codetest.repository.DogRepository;

import java.util.*;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "/dogs")
public class DogsController {

    private DogRepository dogRepository;

    @Autowired
    public DogsController(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    @GetMapping(value = "")
    public ResponseEntity<Iterable<Dog>> getDogs() {
        Iterable<Dog> dogCollection = dogRepository.findAll();
        long count = StreamSupport.stream(dogCollection.spliterator(), false).count();

        if (count > 0) {
            return ResponseEntity.ok(dogCollection);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{breed}")
    public ResponseEntity<Dog> getDog(@PathVariable String breed) {
        Optional<Dog> foundDog = dogRepository.findById(breed);

        if (foundDog.isPresent()) {
            return ResponseEntity.ok(foundDog.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity deleteAll() {
        dogRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{breed}")
    public ResponseEntity deleteDog(@PathVariable String breed) {
        try {
            dogRepository.deleteById(breed);
            return ResponseEntity.ok().build();
        }
        catch (EmptyResultDataAccessException emptyException) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{breed}/{type}")
    public ResponseEntity deleteDog(@PathVariable String breed, @PathVariable String type) {
        Optional<Dog> foundDog = dogRepository.findById(breed);

        if (foundDog.isPresent()) {
            Dog dog = foundDog.get();

            if (dog.getTypes().contains(type)) {
                dog.getTypes().remove(type);
                dogRepository.save(dog);
                return ResponseEntity.ok().build();
            }
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void uploadDogs(@RequestBody JsonNode jsonObject) {

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
