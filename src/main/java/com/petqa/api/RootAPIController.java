package com.petqa.api;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootAPIController {

    @GetMapping("/health")
    public String healthCheck() {
        return "PetQA is running";
    }
}
