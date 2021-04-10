package com.example.demo.Entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DispersionResponse {

    private Map<String, Map<String, Double>> rates;

    public DispersionResponse() {

    }

    public DispersionResponse(LocalDate since) {
        Random random = new Random();
        Map<String, Map<String, Double>> map = new HashMap<>();
        LocalDate endDate = LocalDate.now();
        while (since.isBefore(endDate)) {
            Map<String, Double> rate = new HashMap<>();
            rate.put("USD", random.nextDouble());
            map.put(since.format(DateTimeFormatter.ISO_DATE), rate);
            since = since.plusDays(1);
        }

        this.rates = map;
    }

    public Map<String, Map<String, Double>> getRates() {
        return rates;
    }

    public void setRates(Map<String, Map<String, Double>> rates) {
        this.rates = rates;
    }
}
