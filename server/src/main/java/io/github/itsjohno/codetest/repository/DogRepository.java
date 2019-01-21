package io.github.itsjohno.codetest.repository;

import io.github.itsjohno.codetest.models.Dog;
import org.springframework.data.repository.CrudRepository;

public interface DogRepository extends CrudRepository<Dog, String> {

}
