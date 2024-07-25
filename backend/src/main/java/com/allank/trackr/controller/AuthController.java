package com.allank.trackr.controller;

import com.allank.trackr.models.Erole;
import com.allank.trackr.models.Role;
import com.allank.trackr.models.User;
import com.allank.trackr.dto.auth.LoginRequest;
import com.allank.trackr.dto.auth.RegisterRequest;
import com.allank.trackr.dto.auth.JwtResponse;
import com.allank.trackr.dto.response.MessageDto;
import com.allank.trackr.repository.RoleRepository;
import com.allank.trackr.repository.UserRepository;
import com.allank.trackr.security.jwt.JwtUtils;
import com.allank.trackr.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getEmail(), roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            return ResponseEntity.badRequest().body(new MessageDto("Error: email already taken"));
        }

        User user = new User(registerRequest.email(), encoder.encode(registerRequest.password()));

        Set<String> strRoles = registerRequest.role();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(Erole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch(role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(Erole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(("Error: admin Role is not found")));

                        roles.add(adminRole);
                        break;

                    default:
                        Role userRole = roleRepository.findByName(Erole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: user Role is not found"));

                        roles.add(userRole);
                        break;
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageDto("User Registered successfully"));
    }


}
