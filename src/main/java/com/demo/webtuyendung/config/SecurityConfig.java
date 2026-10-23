package com.demo.webtuyendung.config;


import com.demo.webtuyendung.repository.UserRepository;
import com.demo.webtuyendung.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
            // Static mặc định của Spring Boot (/static, /public, /resources, /META-INF/resources, /webjars)
            PathRequest.toStaticResources().atCommonLocations(),
        
            // Static riêng của dự án
            new AntPathRequestMatcher("/assets/**"),
            new AntPathRequestMatcher("/assets_admin/**"),
            new AntPathRequestMatcher("/fonts/**"),
            new AntPathRequestMatcher("/css/**"),
            new AntPathRequestMatcher("/js/**"),
            new AntPathRequestMatcher("/images/**"),
            new AntPathRequestMatcher("/static/**"),
        
            // Cho chắc các font
            new AntPathRequestMatcher("/**/*.woff"),
            new AntPathRequestMatcher("/**/*.woff2"),
            new AntPathRequestMatcher("/**/*.ttf"),
            new AntPathRequestMatcher("/**/*.eot"),
            new AntPathRequestMatcher("/**/*.svg")
        );
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    ApplicationListener<AuthenticationSuccessEvent> successEvent() {
        return event -> {
            System.out.println("Success Login " + event.getAuthentication().getClass().getSimpleName() + " - " + event.getAuthentication().getName());
        };
    }

    @Bean
    ApplicationListener<AuthenticationFailureBadCredentialsEvent> failureEvent() {
        return event -> {
            System.err.println("Bad Credentials Login " + event.getAuthentication().getClass().getSimpleName() + " - " + event.getAuthentication().getName());
        };
    }

    @Bean
    public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver);
        engine.addDialect(new SpringSecurityDialect());
        return engine;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        String cspConnectSrc = "connect-src 'self' http://localhost:8080 " +
                               "https://cdnjs.cloudflare.com https://cdn.jsdelivr.net https://code.jquery.com " +
                               "https://cdn.jsdelivr.net/npm/pypdide/full/pyodide.js https://cdn.jsdelivr.net/gh/python-visualization/ " +
                               "https://www.claudeusercontent.com";

        // Định nghĩa chính sách chung cho script và style để tránh các lỗi load tài nguyên khác
        String cspDefaultSrc = "default-src 'self'; ";
        String cspScriptSrc = "script-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com https://cdn.jsdelivr.net https://code.jquery.com; ";
        String cspStyleSrc = "style-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com https://cdn.jsdelivr.net; ";


        // Kết hợp tất cả các chỉ thị
        String finalCspPolicy = cspDefaultSrc + cspScriptSrc + cspStyleSrc + cspConnectSrc;

        http
            // Thêm cấu hình Headers để định nghĩa Content Security Policy
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives(finalCspPolicy)
                )
            )
            // Cho phép tất cả các API endpoint trong /api/**
            .authorizeHttpRequests(at -> at.requestMatchers("/api/**").permitAll())

            // --- KẾT THÚC PHẦN CẬP NHẬT ĐỂ SỬA LỖI CSP ---
            
            .csrf().disable()
            
            .formLogin(f -> f.loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .failureHandler(new CustomAuthenticationFailureHandler())
            )
            .authorizeHttpRequests(at -> at.requestMatchers("/", "/home", "/appointment/**", "/login/**", "/loginError/**", "re-send", "/image/**", "/js/**", "/css/**", "/lib/**", "/style/**", "/slider/**", "/assets/**",
                "/api/storage/**", "recover", "send-otp-recover", "otp-check", "confirm-otp", "send-otp-recover", "confirm-otp-recover", "/sendRequest/**", "/generate_response/**", "/api/**",
                "save-new-password", "detail", "change-password", "save-change-password", "/users/**", "/forgot/**", "/forgotPass/**", "/otp-check-pass/**", "/confirm-otp-pass/**", "/resend-otp-pass/**", "/change-pass/**",
                "/assets/**", "/assets_admin/**", "/about/**", "/index", "/index/**", "/fonts/**", "/guest/**", "/404").permitAll()
                .requestMatchers("/changePass/**", "/change-password/**").hasAnyRole("ADMIN", "HR", "DEP")
                .requestMatchers("/auth/first-login", "/auth/send-otp", "/auth/verify-otp", "/auth/resend-otp").hasAnyRole("ADMIN", "HR", "DEP")
                .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                .requestMatchers("/hr/**").hasAnyRole("HR")
                .requestMatchers("/dep/**").hasAnyRole("DEP")
                .anyRequest().authenticated());
        return http.build();
    }
}