package com.petqa.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyAPIController {

    @GetMapping("/my")
    public String myAPI() {
        return "my api";
    }
}
