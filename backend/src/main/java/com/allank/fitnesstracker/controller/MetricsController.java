package com.allank.fitnesstracker.controller;

import com.allank.fitnesstracker.dto.MetricDto;
import com.allank.fitnesstracker.models.Metric;
import com.allank.fitnesstracker.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/metrics")
public class MetricsController {

    @Autowired
    ActivityService activityService;

    @ResponseBody
    @GetMapping(path = "/all")
    public ResponseEntity<Object> getAllMetrics() {
        List<Metric> metrics = activityService.getAllMetrics();

        List<MetricDto> response = metrics.stream()
                .map(item -> new MetricDto(
                        item.getId(),
                        item.getName(),
                        item.getUnit()
                ))
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

