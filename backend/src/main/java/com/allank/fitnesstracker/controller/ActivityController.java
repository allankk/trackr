package com.allank.fitnesstracker.controller;

import com.allank.fitnesstracker.dto.ActivityTypeDto;
import com.allank.fitnesstracker.dto.MetricDto;
import com.allank.fitnesstracker.dto.UserActivityTypeDto;
import com.allank.fitnesstracker.models.ActivityType;
import com.allank.fitnesstracker.models.Metric;
import com.allank.fitnesstracker.models.UserActivityType;
import com.allank.fitnesstracker.security.services.UserDetailsImpl;
import com.allank.fitnesstracker.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api")
public class ActivityController {

    @Autowired
    ActivityService activityService;

    @ResponseBody
    @GetMapping(path = "/activitytype/all")
    public ResponseEntity<Object> getAllActivities() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        List<ActivityType> defaultActivities = activityService.getAllDefaultActivityTypes();
        List<ActivityType> userActivities = activityService.getUserActivityTypes(userId);

        List<ActivityType> combinedActivities = new ArrayList<>(defaultActivities);
        combinedActivities.addAll(userActivities);

        List<ActivityTypeDto> response = combinedActivities.stream()
                .map(item -> new ActivityTypeDto(
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getMetrics(),
                        item.isDefault()
                ))
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ResponseBody
    @GetMapping(path = "/metrics/all")
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

    @ResponseBody
    @PostMapping(path = "/activitytype/add")
    public ResponseEntity<Object> addActivityType(@RequestBody UserActivityTypeDto userActivityTypeDto, @RequestHeader("Authorization") String token) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        activityService.addCustomActivityTypeForUser(userId, userActivityTypeDto.name(), userActivityTypeDto.description(), userActivityTypeDto.metrics());

        System.out.println("Received activity type:");
        System.out.println("Token: " + token);
        System.out.println("Name: " + userActivityTypeDto.name());
        System.out.println("Description: " + userActivityTypeDto.description());
        System.out.println("Metrics: " + userActivityTypeDto.metrics());

        return new ResponseEntity<>("Activity type added successfully!", HttpStatus.OK);
    }
}

