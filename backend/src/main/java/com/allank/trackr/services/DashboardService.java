package com.allank.trackr.services;

import com.allank.trackr.models.ActivitySession;
import com.allank.trackr.models.User;
import com.allank.trackr.repository.ActivitySessionRepository;
import com.allank.trackr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivitySessionRepository activitySessionRepository;

    public List<Instant> getAllSessionDates(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<ActivitySession> activitySessions = activitySessionRepository.findByUser(user);

        return activitySessions.stream().map(ActivitySession::getDate).toList();
    }
}
