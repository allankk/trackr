package com.allank.fitnesstracker.controller;

import com.allank.fitnesstracker.dto.ActivityTypeDto;
import com.allank.fitnesstracker.models.ActivityType;
import com.allank.fitnesstracker.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/activity")
public class ActivityController {

    @Autowired
    ActivityService activityService;

    @ResponseBody
    @GetMapping(path = "/all")
    public ResponseEntity<Object> getAllActivities() {

        List<ActivityType> defaultActivities = activityService.getAllDefaultActivityTypes();

        List<ActivityTypeDto> response = defaultActivities.stream()
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
}

