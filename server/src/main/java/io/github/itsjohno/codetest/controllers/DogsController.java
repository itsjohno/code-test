package io.github.itsjohno.codetest.controllers;

import io.github.itsjohno.codetest.models.Dog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/dogs")
public class DogsController {

    @GetMapping
    public List<Dog> getDogs() {
        List<Dog> tempDogList = new ArrayList<>();
        tempDogList.add(new Dog("big"));
        tempDogList.add(new Dog("wee"));

        return tempDogList;
    }

}
