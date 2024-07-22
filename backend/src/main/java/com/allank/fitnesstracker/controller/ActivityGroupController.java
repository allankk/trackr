package com.allank.fitnesstracker.controller;

import com.allank.fitnesstracker.dto.*;
import com.allank.fitnesstracker.dto.response.ActivityGroupResponseDto;
import com.allank.fitnesstracker.dto.response.MessageDto;
import com.allank.fitnesstracker.models.ActivityGroup;
import com.allank.fitnesstracker.security.services.UserDetailsImpl;
import com.allank.fitnesstracker.services.ActivityGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/activitygroup")
public class ActivityGroupController {

    @Autowired
    ActivityGroupService activityGroupService;

    @ResponseBody
    @GetMapping(path = "/all")
    public ResponseEntity<Object> getAllActivityGroups() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        List<ActivityGroup> userActivityGroups = activityGroupService.getUserActivityGroups(userId);

        List<ActivityGroupResponseDto> response = activityGroupService.convertToDto(userActivityGroups);

        return ResponseEntity.ok(response);
    }

    @ResponseBody
    @PostMapping(path = "/add")
    public ResponseEntity<Object> addActivityGroup(@RequestBody ActivityGroupDto activityGroupDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        activityGroupService.createActivityGroupForUser(userId, activityGroupDto.name(), activityGroupDto.description(), activityGroupDto.activityTypes());

        return ResponseEntity.ok(new MessageDto("Activity group added successfully!"));
    }

    @ResponseBody
    @PostMapping(path = "/edit/{activityGroupId}")
    public ResponseEntity<Object> editActivityGroup(
            @PathVariable Long activityGroupId,
            @RequestBody ActivityGroupDto activityGroupDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        activityGroupService.updateActivityGroup(activityGroupId, userId, activityGroupDto.name(), activityGroupDto.description(), activityGroupDto.activityTypes());

        return new ResponseEntity<>("Activity group updated successfully!", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(path = "/delete/{activityGroupId}")
    public ResponseEntity<Object> deleteActivityGroup(@PathVariable Long activityGroupId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        activityGroupService.deleteUserActivityGroup(userId, activityGroupId);

        return new ResponseEntity<>("Activity group deleted successfully!", HttpStatus.OK);
    }
}
