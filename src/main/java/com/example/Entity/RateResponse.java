package com.example.Entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RateResponse{
    private Map<String, Double> valute;

    public RateResponse() {
    }

    public RateResponse(LocalDate date) {
        Random random = new Random();
        Map<String, Double> map = new HashMap<>();
        map.put("USD", random.nextDouble());
        this.valute = map;
    }


    public Map<String, Double> getRates() {
        return valute;
    }

    public void setRates(Map<String, Double> rates) {
        this.valute = rates;
    }
}
