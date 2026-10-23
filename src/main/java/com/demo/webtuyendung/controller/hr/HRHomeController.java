package com.demo.webtuyendung.controller.hr;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hr")
public class HRHomeController {

    @GetMapping("/dashboard")
    public String employee(Model model, HttpSession session) {
        return "hr/dashboard";
    }
}