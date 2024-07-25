package com.allank.trackr.controller;

import com.allank.trackr.dto.MetricDto;
import com.allank.trackr.dto.UnitDto;
import com.allank.trackr.models.Metric;
import com.allank.trackr.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
                        item.getStandardUnit(),
                        item.getUnits().stream()
                                .map(unit -> new UnitDto(
                                        unit.getId(),
                                        unit.getName(),
                                        unit.getUnit(),
                                        unit.getConversionFactor()
                                ))
                                .toList()
                ))
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

