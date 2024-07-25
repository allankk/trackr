package com.allank.trackr.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class ApiController {

    // Forwards all routes to FrontEnd except: '/', '/index.html', '/api', '/api/**'
    // Required because of 'mode: history' usage in frontend routing, see README for further details
    @GetMapping(value = "{_:^(?!index\\.html|api).$}")
    public String redirectApi() {
        return "forward:/";
    }

    @ResponseBody
    @GetMapping(path = "/hello")
    public String sayHello() {
        return "Hello from backend";
    }

    @ResponseBody
    @GetMapping(path = "/authhello")
    @PreAuthorize("hasRole('USER')")
    public String sayAuthHello() {
        return "Hello from User";
    }

}
