package com.demo.webtuyendung.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;


@Controller
public class HomeController {

    @GetMapping("/")
    private String indexHome(Model model, HttpServletRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        model.addAttribute("currentUri", request.getRequestURI());

        if (authorities.stream().anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()))) {
            model.addAttribute("email", username);
            return "redirect:/admin/dashboard";
        } else if (authorities.stream().anyMatch(authority -> "ROLE_HR".equals(authority.getAuthority()))) {
            model.addAttribute("email", username);
            return "redirect:/hr/dashboard";
        } else if (authorities.stream().anyMatch(authority -> "ROLE_DEP".equals(authority.getAuthority()))) {
            model.addAttribute("email", username);
            return "redirect:/dep/dashboard";
        } else {
            return "index";
        }
    }


    @GetMapping("/home")
    public String homePage(Model model, HttpServletRequest request) {
        return indexHome(model, request);
    }


    @GetMapping("/index")
    public String index(Model model, HttpServletRequest request) {
        return indexHome(model, request);
    }


}