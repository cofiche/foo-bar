package io.wahman.kata.controller;

import io.wahman.kata.service.TransformationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Controller {

    // transform the number in path through the algorithm
    @GetMapping("/transform/{number}")
    public String transformNumber(@PathVariable Integer number) {
        return TransformationService.processNumber(number);
    }


}
