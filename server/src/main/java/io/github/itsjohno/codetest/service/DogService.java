package io.github.itsjohno.codetest.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.itsjohno.codetest.model.Dog;
import io.github.itsjohno.codetest.repository.DogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.*;

public class DogService {

    private DogRepository dogRepository;

    @Autowired
    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    public Collection<Dog> findDogs(String breed) {
        Collection<Dog> dogCollection = new ArrayList<>();

        if (breed.isBlank()) {
            // We want to get all dogs, so lets move the iterable into a collection.
            dogRepository.findAll().forEach(dogCollection::add);
        } else {
            // We're after a specific dog, so lets find by type, and if the optional is filled then add to the collection
            dogRepository.findById(breed).ifPresent(dogCollection::add);
        }

        return dogCollection;
    }

    public void deleteAllDogs() {
        dogRepository.deleteAll();
    }

    public boolean deleteDog(String breed) {
        boolean deleted = true;

        try {
            dogRepository.deleteById(breed);
        } catch (EmptyResultDataAccessException emptyException) {
            deleted = false;
        }

        return deleted;
    }

    public boolean deleteTypeFromDog(String breed, String type) {
        boolean deleted = false;
        Optional<Dog> foundDog = dogRepository.findById(breed);

        if (foundDog.isPresent()) {
            Dog dog = foundDog.get();

            if (dog.getTypes().contains(type)) {
                dog.getTypes().remove(type);
                dogRepository.save(dog);
                deleted = true;
            }
        }

        return deleted;
    }


    public void uploadDogs(JsonNode jsonObject) {
        List<Dog> dogsToCreate = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            dogsToCreate.addAll(Arrays.asList(objectMapper.convertValue(jsonObject, Dog[].class)));
        } catch (Exception exception) {
            // We've blown up during de-serialisation. Let's just move on to see if it's other format.
        }

        if (0 == dogsToCreate.size()) {
            // No dogs have been added to the list, let's see if we've been passed in a non-API format.

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
