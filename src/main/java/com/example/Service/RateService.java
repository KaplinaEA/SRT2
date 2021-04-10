package com.example.Service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RateService {

    public double getRate(LocalDate date) throws Exception {
        HttpResponse response = RequestService.requestHttpClientCRB(date);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity httpEntity = response.getEntity();
            String xml = EntityUtils.toString(httpEntity);
            return getRateParseXml(xml);
        }
        return 0.0;
    }

    public double getDispersion(LocalDate date){
        try {
            LocalDate currentDate = LocalDate.now();
            ArrayList<Double> course = new ArrayList<Double>();
            while (date.isBefore(currentDate)) {
                HttpResponse response = RequestService.requestHttpClientCRB(date);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity httpEntity = response.getEntity();
                    String xml = EntityUtils.toString(httpEntity);
                    double value = Math.pow(Math.tan(getRateParseXml(xml)), 7.5);
                    if (!Double.isNaN(value)) course.add(value);
                    date = date.plusDays(1);
                }
            }
            return dispersion(course);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public double getRateParseXml(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml)));
            NodeList elList = doc.getDocumentElement().getElementsByTagName("Valute");

            for (int i = 0; i < elList.getLength(); i++) {
                if (elList.item(i).getChildNodes().item(1).getChildNodes().item(0).getNodeValue().equals("USD"))
                    return Double.parseDouble(elList.item(i).getChildNodes().item(4).getChildNodes().item(0).getNodeValue().replace(",", "."));
            }
        }catch (Exception e){
            return 0.0;
        }
        return 0.0;
    }

    public static double dispersion(List<Double> list) {
        double mean = list.stream().mapToDouble(a -> a).sum() / list.size();
        double sum = list.stream().mapToDouble(a -> (a - mean) * (a - mean)).sum();
        return Math.sqrt(sum / (list.size() - 1));
    }


    public static List<Double> getListRates(Map<String, Map<String, Double>> r) {
        List<Double> list = new ArrayList<>();
        r.values().forEach(value -> list.add(value.get("USD")));
        return list;
    }
}
