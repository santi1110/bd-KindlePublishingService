package com.amazon.ata.kindlepublishingservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping(value = "/books", produces = {"application/json"})
    public ResponseEntity<?> sayHello() {
        String message = "Hello! You've accessed the /test endpoint with GET!";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
