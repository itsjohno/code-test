package io.github.itsjohno.codetest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dog {

    @Id
    @NonNull
    private String breed;

    @ElementCollection
    private List<String> types;

    public void addType(String type) {
        if (null == this.types) {
            this.types = new ArrayList<>();
        }

        types.add(type);
    }
}
