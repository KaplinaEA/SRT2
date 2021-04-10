package com.example.Service;


import com.example.Entity.DispersionResponse;
import com.example.Entity.RateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestService {
    private static final String uri = "http://www.cbr.ru/scripts/XML_daily.asp/";
    private static final String paramDate = "date_req";
    protected static ObjectMapper objectMapper = new ObjectMapper();

    public static HttpResponse requestHttpClientCRB(LocalDate date) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);
        List<NameValuePair> urlParameters = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        urlParameters.add(new BasicNameValuePair(paramDate, date.format(formatter)));
        URI uri = new URIBuilder(request.getURI()).addParameters(urlParameters).build();
        request.setURI(uri);
        return client.execute(request);
    }
    
    public static RateResponse requestHttpClientRate(LocalDate date) throws Exception {
        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8081/rate/" + date.format(DateTimeFormatter.ISO_DATE));

        HttpResponse response = client.execute(request);
        return objectMapper.readValue(response.getEntity().getContent(), RateResponse.class);
    }

    public static DispersionResponse requestHttpClientDispersion(LocalDate date) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://localhost:8081/dispersion/" + date.format(DateTimeFormatter.ISO_DATE));
        return  objectMapper.readValue(client.execute(request).getEntity().getContent(), DispersionResponse.class);
    }

    public static Mono<RateResponse> requestWebClientRate(LocalDate date) {
         return WebClient.create().get().uri(uriBuilder -> uriBuilder
                .scheme("http")
                 .port(8081)
                .host("localhost")
                .path("/rate/" + date.format(DateTimeFormatter.ISO_DATE))
                 .build())
                 .retrieve()
                 .bodyToMono(RateResponse.class);
    }

    public static Mono<DispersionResponse> requestWebClientDispersion(LocalDate date) {
        return WebClient.create().get().uri(uriBuilder -> uriBuilder
                .port(8081)
                .scheme("http")
                .host("localhost")
                .path("/dispersion/" + date.format(DateTimeFormatter.ISO_DATE))
                .build())
                .retrieve()
                .bodyToMono(DispersionResponse.class);
    }
}




