package io.github.itsjohno.codetest.controller;

import io.github.itsjohno.codetest.model.Dog;
import io.github.itsjohno.codetest.service.DogService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = DogsController.class)
public class DogsControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private DogService dogServiceMock;

    @InjectMocks
    private DogsController dogsController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc =  MockMvcBuilders.standaloneSetup(dogsController).build();
    }

    @Test
    public void shouldReturnNotFoundWhenNoDogsPresent() throws Exception {
        Mockito.when(dogServiceMock.findDogs(any(String.class)))
                .thenReturn(Collections.EMPTY_LIST);

        this.mockMvc.perform(get("/dogs")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnOKWhenAtLeastOneDogFound() throws Exception {
        Mockito.when(dogServiceMock.findDogs(any(String.class)))
                .thenReturn(Collections.singletonList(new Dog("testBreed")));

        this.mockMvc.perform(get("/dogs")).andDo(print()).andExpect(status().isOk());
    }
}
