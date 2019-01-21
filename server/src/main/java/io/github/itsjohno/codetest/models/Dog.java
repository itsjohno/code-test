package io.github.itsjohno.codetest.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dog {

    @Id
    private String breed;

    @ElementCollection
    private List<String> types;

    public Dog() {}

    public Dog(String breed) {
        this.breed = breed;
    }

    public void addType(String type) {
        if (null == this.types) {
            this.types = new ArrayList<>();
        }

        types.add(type);
    }
}
