package com.allank.fitnesstracker.controller;

import com.allank.fitnesstracker.dto.response.MessageDto;
import com.allank.fitnesstracker.dto.request.session.SessionRequestDto;
import com.allank.fitnesstracker.dto.response.session.GroupedSessionResponseDto;
import com.allank.fitnesstracker.dto.response.session.SessionResponseDto;
import com.allank.fitnesstracker.security.services.UserDetailsImpl;
import com.allank.fitnesstracker.services.ActivitySessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/activitysession")
public class ActivitySessionController {

    @Autowired
    ActivitySessionService activitySessionService;

    @ResponseBody
    @PostMapping(path = "/add")
    public ResponseEntity<Object> addActivitySession(@RequestBody SessionRequestDto sessionDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        activitySessionService.addSession(userId, sessionDto);

        return ResponseEntity.ok(new MessageDto("Activity group added successfully!"));
    }

    @ResponseBody
    @GetMapping(path = "/all")
    public ResponseEntity<List<GroupedSessionResponseDto>> getAllSessions() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        List<GroupedSessionResponseDto> sessions = activitySessionService.getAllSessions(userId);

        return ResponseEntity.ok(sessions);
    }

    @ResponseBody
    @GetMapping(path = "/{activitySessionId}")
    public ResponseEntity<SessionResponseDto> getActivitySession(@PathVariable Long activitySessionId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        SessionResponseDto sessions = activitySessionService.getSession(userId, activitySessionId);

        return ResponseEntity.ok(sessions);
    }

    @ResponseBody
    @PostMapping(path = "/edit/{activitySessionId}")
    public ResponseEntity<Object> editActivitySession(
            @PathVariable Long activitySessionId,
            @RequestBody SessionRequestDto sessionRequestDto) {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        activitySessionService.updateSession(userId, activitySessionId, sessionRequestDto);

        return new ResponseEntity<>("Activity session updated successfully!", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(path = "/delete/{activitySessionId}")
    public ResponseEntity<Object> deleteActivitySession(@PathVariable Long activitySessionId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        activitySessionService.deleteSession(userId, activitySessionId);

        return new ResponseEntity<>("Activity Session deleted successfully!", HttpStatus.OK);
    }
}
