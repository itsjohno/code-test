package io.github.itsjohno.codetest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class DefaultController {

    @GetMapping(value = "")
    public ResponseEntity<String> getRequest() {
        return ResponseEntity.ok("Make requests through /dogs endpoint");
    }

}
