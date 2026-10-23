package com.demo.webtuyendung.controller.auth;

import com.demo.webtuyendung.entity.EmployeeEntity;
import com.demo.webtuyendung.entity.UserEntity;
import com.demo.webtuyendung.repository.EmployeeRepository;
import com.demo.webtuyendung.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping()
public class ProfileController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @GetMapping("/dep/profile")
    public String profile(@RequestParam(value = "passwordError", required = false) String passwordError,
                          @RequestParam(value = "passwordSuccess", required = false) String passwordSuccess,
                          Model model) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        UserEntity user = userOpt.get();
        EmployeeEntity employeeEntity = employeeRepository.findByUser(user).get();
        model.addAttribute("employee", employeeEntity);
        if (passwordError != null) {
            model.addAttribute("passwordError", passwordError);
        }
        if (passwordSuccess != null) {
            model.addAttribute("passwordSuccess", passwordSuccess);
        }
        return "dep/profile";
    }


    @PostMapping("/dep/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return "redirect:/dep/profile?passwordError=User+not+found";
        }

        UserEntity user = userOptional.get();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return "redirect:/dep/profile?passwordError=Current+password+is+incorrect";
        }

        if (newPassword.equals(currentPassword)) {
            return "redirect:/dep/profile?passwordError=New+password+must+be+different+from+current";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "redirect:/dep/profile?passwordSuccess=Password+changed+successfully";
    }

    @GetMapping("/hr/profile")
    public String profileHuman(@RequestParam(value = "passwordError", required = false) String passwordError,
                          @RequestParam(value = "passwordSuccess", required = false) String passwordSuccess,
                          Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        UserEntity user = userOpt.get();
        EmployeeEntity entity = employeeRepository.findByUser(user).get();
        model.addAttribute("staff", entity);
        if (passwordError != null) {
            model.addAttribute("passwordError", passwordError);
        }
        if (passwordSuccess != null) {
            model.addAttribute("passwordSuccess", passwordSuccess);
        }
        return "hr/profile";
    }

    @PostMapping("/hr/change-password")
    public String changePasswordHr(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return "redirect:/hr/profile?passwordError=User+not+found";
        }

        UserEntity user = userOptional.get();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return "redirect:/hr/profile?passwordError=Current+password+is+incorrect";
        }

        if (newPassword.equals(currentPassword)) {
            return "redirect:/hr/profile?passwordError=New+password+must+be+different+from+current";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "redirect:/hr/profile?passwordSuccess=Password+changed+successfully";
    }

}