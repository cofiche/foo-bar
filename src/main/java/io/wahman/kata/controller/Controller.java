package io.wahman.kata.controller;

import io.wahman.kata.Constants.Constants;
import io.wahman.kata.service.TransformationService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class Controller {

    // transform the number in path through the algorithm
    @GetMapping("/transform/{number}")
    public String transformNumber(@PathVariable Integer number) {
        return TransformationService.processNumber(number);
    }


}
