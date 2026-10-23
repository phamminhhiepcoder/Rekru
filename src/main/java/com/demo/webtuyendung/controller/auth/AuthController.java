package com.demo.webtuyendung.controller.auth;


import com.demo.webtuyendung.service.EmailSenderService;
import com.demo.webtuyendung.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSenderService emailSenderService;

    @GetMapping("/login")
    public String showLogin(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "auth/login";
    }

    @GetMapping("/404")
    public String notFound() {
        return "auth/404";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/?logout";
    }

}
