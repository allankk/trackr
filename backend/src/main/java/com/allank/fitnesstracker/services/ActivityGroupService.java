package com.allank.fitnesstracker.services;

import com.allank.fitnesstracker.dto.ActivityGroupDto;
import com.allank.fitnesstracker.dto.ActivityGroupResponseDto;
import com.allank.fitnesstracker.dto.ActivityTypeDto;
import com.allank.fitnesstracker.models.ActivityGroup;
import com.allank.fitnesstracker.models.ActivityType;
import com.allank.fitnesstracker.models.Metric;
import com.allank.fitnesstracker.models.User;
import com.allank.fitnesstracker.repository.ActivityGroupRepository;
import com.allank.fitnesstracker.repository.ActivityTypeRepository;
import com.allank.fitnesstracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActivityGroupService {
    @Autowired
    ActivityTypeRepository activityTypeRepository;

    @Autowired
    ActivityGroupRepository activityGroupRepository;

    @Autowired
    UserRepository userRepository;

    public List<ActivityGroup> getUserActivityGroups(Long userId) {
        return activityGroupRepository.findByUserId(userId);
    }

    public List<ActivityGroupResponseDto> convertToDto(List<ActivityGroup> activityGroups) {
        return activityGroups.stream()
                .map(this::getGroupDto)
                .collect(Collectors.toList());
    }

    public void createActivityGroupForUser(Long userId, String groupName, String groupDescription, Set<Long> activityTypeIds ) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        ActivityGroup activityGroup = new ActivityGroup();
        activityGroup.setUser(user);
        activityGroup.setName(groupName);
        activityGroup.setDescription(groupDescription);

        Set<ActivityType> activityTypes = new HashSet<>(activityTypeRepository.findAllById(activityTypeIds));
        activityGroup.setActivityTypes(activityTypes);
        activityGroupRepository.save(activityGroup);
    }

    public void updateActivityGroup(Long activityGroupId, Long userId, String name, String description, Set<Long> activityTypeIds) {
        ActivityGroup activityGroup = activityGroupRepository.findById(activityGroupId).orElseThrow(() -> new IllegalArgumentException("Activity group not found"));

        if (!activityGroup.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized to update this activity type");
        }

        activityGroup.setName(name);
        activityGroup.setDescription(description);
        Set<ActivityType> activityTypes = new HashSet<>(activityTypeRepository.findAllById(activityTypeIds));
        activityGroup.setActivityTypes(activityTypes);

        activityGroupRepository.save(activityGroup);
    }

    public void deleteUserActivityGroup(Long userId, Long activityGroupId) {
        ActivityGroup activityGroup = activityGroupRepository.findById(activityGroupId).orElseThrow(() -> new IllegalArgumentException("activity group not found"));

        if (activityGroup.getUser().getId().equals(userId)) {
            System.out.println("deleting activitytype");
            activityGroupRepository.delete(activityGroup);
        } else {
            throw new RuntimeException("Activity group does not belong do the given user.");
        }
    }

    private ActivityGroupResponseDto getGroupDto(ActivityGroup activityGroup) {
        List<ActivityTypeDto> activityTypeList = activityGroup.getActivityTypes().stream()
                .map(activityType -> new ActivityTypeDto(
                        activityType.getId(),
                        activityType.getName(),
                        activityType.getDescription(),
                        activityType.getMetrics(),
                        activityType.isDefault()
                ))
                .collect(Collectors.toList());

        // Create and return ActivityGroupDto
        return new ActivityGroupResponseDto(
                activityGroup.getId(),
                activityGroup.getName(),
                activityGroup.getDescription(),
                activityTypeList
        );
    }
}
