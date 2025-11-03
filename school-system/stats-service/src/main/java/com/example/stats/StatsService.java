package com.example.stats;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatsService {

    private final RestTemplate restTemplate;

    public StatsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Stats> generateStats() {
        String url = "http://grade-service:8083/api/grades";
        List<Map<String, Object>> grades = Arrays.asList(
                restTemplate.getForObject(url, Map[].class)
        );

        Map<String, List<Map<String, Object>>> grouped = grades.stream()
                .collect(Collectors.groupingBy(
                        g -> g.get("studentId") + "-" + g.get("subject")
                ));

        List<Stats> statsList = new ArrayList<>();

        for (String key : grouped.keySet()) {
            List<Map<String, Object>> group = grouped.get(key);
            Long studentId = ((Number) group.get(0).get("studentId")).longValue();
            String subject = (String) group.get(0).get("subject");

            List<Double> values = group.stream()
                    .map(g -> ((Number) g.get("value")).doubleValue())
                    .collect(Collectors.toList());

            double highest = values.stream().max(Double::compare).orElse(0.0);
            double lowest = values.stream().min(Double::compare).orElse(0.0);
            double average = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

            statsList.add(new Stats(studentId, subject, highest, lowest, average));
        }

        return statsList;
    }
}
