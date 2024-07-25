package com.allank.trackr.services;

import com.allank.trackr.dto.response.ActivityGroupResponseDto;
import com.allank.trackr.dto.ActivityTypeDto;
import com.allank.trackr.mapper.ActivityTypeRequestMapper;
import com.allank.trackr.models.ActivityGroup;
import com.allank.trackr.models.ActivityType;
import com.allank.trackr.models.User;
import com.allank.trackr.repository.ActivityGroupRepository;
import com.allank.trackr.repository.ActivityTypeRepository;
import com.allank.trackr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
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

    @Autowired
    ActivityTypeRequestMapper activityTypeMapper;

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
                .map(activityTypeMapper::toDto)
                .toList();

        return new ActivityGroupResponseDto(
                activityGroup.getId(),
                activityGroup.getName(),
                activityGroup.getDescription(),
                activityTypeList
        );
    }
}
