package com.brigada.is.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {
    @GetMapping("/{path:[^.]*}")
    public String forward() {
        return "forward:/index.html";
    }
}
