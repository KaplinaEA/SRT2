package com.example.demo.Controller;

import com.example.demo.Entity.DispersionResponse;
import com.example.demo.Entity.RateResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class RateController {


    @GetMapping(path = "/rate/{date}")
    public RateResponse rate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return new RateResponse(date);
    }

    @GetMapping(path = "/dispersion/{since}")
    public DispersionResponse history(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate since) {
        return new DispersionResponse(since);
    }
}
