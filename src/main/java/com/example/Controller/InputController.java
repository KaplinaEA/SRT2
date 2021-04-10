package com.example.Controller;

import com.example.Entity.DispersionResponse;
import com.example.Entity.RateResponse;
import com.example.Service.RateService;
import com.example.Service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
public class InputController {

    @Autowired
    private RateService rateService;

    @GetMapping("/cbr/dispersion/{since}")
    public double dispersionCBR(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate since) {
        return rateService.getDispersion(since);
    }

    @GetMapping("/cbr/rate/{date}")
    public double rateCBR(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws Exception {
        return rateService.getRate(date);
    }

    @GetMapping("/flux/dispersion/{since}")
    public Mono<Double> dispersionWebFlux(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate since) {
        return RequestService.requestWebClientDispersion(since)
                .map(DispersionResponse::getRates)
                .map(RateService::getListRates)
                .map(RateService::dispersion);
    }

    @GetMapping("/flux/rate/{date}")
    public Mono<Double> rateWebFlux(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return RequestService.requestWebClientRate(date).map(value -> value.getRates().get("USD"));
    }


    @GetMapping("/dispersion/{since}")
    public double dispersion(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate since) throws Exception {
        return RateService.dispersion(RateService.getListRates(RequestService.requestHttpClientDispersion(since).getRates()));
    }

    @GetMapping("/rate/{date}")
    public double rate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws Exception {
        return RequestService.requestHttpClientRate(date).getRates().get("USD");
    }
}
