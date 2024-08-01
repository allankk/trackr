package com.allank.trackr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {

    @RequestMapping(value = {"/{path:[^\\\\.]*}", "activity/**"})
    public String redirect() {
        // Forward to `index.html` so frontend can handle routing.
        return "forward:/index.html";
    }
}
