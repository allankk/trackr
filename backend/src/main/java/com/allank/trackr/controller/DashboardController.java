package com.allank.trackr.controller;

import com.allank.trackr.dto.response.dashboard.SessionDatesDto;
import com.allank.trackr.repository.ActivitySessionRepository;
import com.allank.trackr.security.services.UserDetailsImpl;
import com.allank.trackr.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.util.List;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "api/dashboard")
public class DashboardController {

    @Autowired
    DashboardService dashboardService;

    @ResponseBody
    @GetMapping("/sessiondates")
    public ResponseEntity<Object> getAllSessionDates() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        List<Instant> sessionDates = dashboardService.getAllSessionDates(userId);

        return ResponseEntity.ok(new SessionDatesDto(sessionDates));
    }
}
