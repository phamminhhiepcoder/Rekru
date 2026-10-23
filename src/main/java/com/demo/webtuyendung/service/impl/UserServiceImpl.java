package com.demo.webtuyendung.service.impl;

import com.demo.webtuyendung.config.SecurityUser;
import com.demo.webtuyendung.entity.UserEntity;
import com.demo.webtuyendung.exception.UserBlockedException;
import com.demo.webtuyendung.repository.UserRepository;
import com.demo.webtuyendung.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean validateCredentials(String username, String password) {
        return userRepository.existsByEmailAndPassword(username, passwordEncoder.encode(password));
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            System.out.println(username);
            Optional<UserEntity> userByUsername = userRepository.findByEmail(username);
            if (userByUsername.isEmpty()) {
                throw new UsernameNotFoundException("Invalid credentials!");
            }
            UserEntity user = userByUsername.get();

            if (user.getStatus() == null || user.getStatus() != 1) {
                throw new UserBlockedException("Tài khoản của bạn đã bị khóa!");
            }

            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            String role = "ROLE_";
            Integer roleUser = user.getRole();
            if (roleUser == 0) {
                role += "ADMIN";
            } else if (roleUser == 1) {
                role += "HR";
            } else if (roleUser == 2) {
                role += "DEP";
            }

            grantedAuthorities.add(new SimpleGrantedAuthority(role));

            return new SecurityUser(user.getEmail(), user.getPassword(), true, true, true, true, grantedAuthorities,
                    user.getEmail());
        } catch (UserBlockedException | UsernameNotFoundException e) {
            System.out.println("Lỗi: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("Lỗi không xác định: " + e.getMessage());
            throw new UsernameNotFoundException("Invalid credentials!");
        }
    }

}
