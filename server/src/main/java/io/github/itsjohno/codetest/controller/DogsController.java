package io.github.itsjohno.codetest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.itsjohno.codetest.model.Dog;
import io.github.itsjohno.codetest.service.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(value = "/dogs")
public class DogsController {

    private DogService dogService;

    @Autowired
    public DogsController(DogService dogService) {
        this.dogService = dogService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Dog>> getDogs() {
        Collection<Dog> dogCollection = dogService.findDogs("");
        if (dogCollection.size() > 0) {
            return ResponseEntity.ok(dogCollection);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{breed}", method = RequestMethod.GET)
    public ResponseEntity<Dog> getDog(@PathVariable String breed) {
        Optional<Dog> foundDog = dogService.findDogs(breed).stream().findFirst();

        if (foundDog.isPresent()) {
            return ResponseEntity.ok(foundDog.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity deleteAll() {
        dogService.deleteAllDogs();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{breed}", method = RequestMethod.DELETE)
    public ResponseEntity deleteDog(@PathVariable String breed) {
        if (dogService.deleteDog(breed)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{breed}/{type}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTypeFromDog(@PathVariable String breed, @PathVariable String type) {
        if (dogService.deleteTypeFromDog(breed, type)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void uploadDogs(@RequestBody JsonNode jsonObject) {
        dogService.uploadDogs(jsonObject);
    }
}
