package com.demo.webtuyendung.controller.auth;


import com.demo.webtuyendung.entity.UserEntity;
import com.demo.webtuyendung.repository.UserRepository;
import com.demo.webtuyendung.service.EmailSenderService;
import com.demo.webtuyendung.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

public class OtpController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "forgot", method = RequestMethod.GET)
    public String forgot() {
        return "auth/forgot";
    }

    @RequestMapping(value = "forgotPass", method = RequestMethod.POST)
    public String forgotPass(@RequestParam String email, Model model, HttpSession session) {
        if (userService.findByEmail(email).isEmpty()) {
            model.addAttribute("mess", "Email này không tồn tại!");
            return "auth/forgot";
        }

        session.setAttribute("otp-pass", otpCode());
        session.setMaxInactiveInterval(360); // Session timeout set to 6 minutes

        String subject = "OTP xác nhận quên mật khẩu";
        String mess = "Xin chào,\n" + email + "\nĐây là OTP của bạn: " + session.getAttribute("otp-pass")
                + "\nHãy nhập nó vào form.\nXin cảm ơn!";

        this.emailSenderService.sendEmail(email, subject, mess);
        session.setAttribute("email", email);

        return "redirect:/otp-check-pass";
    }

    @RequestMapping(value = "otp-check-pass", method = RequestMethod.GET)
    public String indexOtpPass() {
        return "auth/otpConfirmPass";
    }

    @RequestMapping(value = "confirm-otp-pass", method = RequestMethod.POST)
    public String checkOtpPass(HttpSession session,
                               @RequestParam("otp-pass") String otp,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        String otpRegister = (String) session.getAttribute("otp-pass");

        if (otp.equals(otpRegister)) {
            redirectAttributes.addFlashAttribute("mess", "OTP chính xác. Hãy đổi lại mật khẩu");
            return "redirect:/change-pass";
        }

        model.addAttribute("mess", "Sai OTP! Hãy thử lại");
        return "auth/otpConfirmPass";
    }

    @RequestMapping(value = "change-pass", method = RequestMethod.GET)
    public String indexResetPass() {
        return "auth/changePass";
    }

    @RequestMapping(value = "change-pass", method = RequestMethod.POST)
    public String reset(HttpSession session,
                        @RequestParam("password") String pass,
                        RedirectAttributes redirectAttributes) {

        String email = (String) session.getAttribute("email");
        UserEntity userEntity = userRepository.findByEmail(email).get();

        userEntity.setPassword(passwordEncoder.encode(pass));
        userService.saveUser(userEntity);

        redirectAttributes.addFlashAttribute("mess", "Mật khẩu được reset thành công!");
        return "redirect:/login";
    }


    public String otpCode() {
        int code = (int) Math.floor(((Math.random() * 899999) + 100000));
        return String.valueOf(code);
    }
}