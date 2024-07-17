package com.allank.fitnesstracker.controller;

import com.allank.fitnesstracker.dto.ActivityTypeDto;
import com.allank.fitnesstracker.dto.UserActivityTypeDto;
import com.allank.fitnesstracker.models.ActivityType;
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

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/activitytype")
public class ActivityTypeController {

    @Autowired
    ActivityService activityService;

    @ResponseBody
    @GetMapping(path = "/all")
    public ResponseEntity<Object> getAllActivities() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        List<ActivityType> userActivities = activityService.getUserActivityTypes(userId);

        List<ActivityTypeDto> response = userActivities.stream()
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
    @PostMapping(path = "/add")
    public ResponseEntity<Object> addActivityType(@RequestBody UserActivityTypeDto userActivityTypeDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        activityService.addCustomActivityTypeForUser(userId, userActivityTypeDto.name(), userActivityTypeDto.description(), userActivityTypeDto.metrics());

        return new ResponseEntity<>("Activity type added successfully!", HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(path = "/edit/{activityTypeId}")
    public ResponseEntity<Object> editActivityType(
            @PathVariable Long activityTypeId,
            @RequestBody UserActivityTypeDto userActivityTypeDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        activityService.updateActivityType(activityTypeId, userId, userActivityTypeDto.name(), userActivityTypeDto.description(), userActivityTypeDto.metrics());

        return new ResponseEntity<>("Activity type updated successfully!", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(path = "/delete/{activityTypeId}")
    public ResponseEntity<Object> deleteActivityType(@PathVariable Long activityTypeId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        activityService.deleteUserActivityType(userId, activityTypeId);

        return new ResponseEntity<>("Activity type deleted successfully!", HttpStatus.OK);
    }
}

