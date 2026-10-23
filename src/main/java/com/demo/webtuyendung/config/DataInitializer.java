package com.demo.webtuyendung.config;

import com.demo.webtuyendung.entity.UserEntity;
import com.demo.webtuyendung.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            UserEntity admin = new UserEntity();
            admin.setEmail("admin@gmail.com");
            admin.setPassword("123");
            admin.setStatus(1);
            admin.setRole(0);

            UserEntity hr = new UserEntity();
            hr.setEmail("hr@gmail.com");
            hr.setPassword("123");
            hr.setStatus(1);
            hr.setRole(1);

            UserEntity dep = new UserEntity();
            dep.setEmail("dep@gmail.com");
            dep.setPassword("123");
            dep.setStatus(1);
            dep.setRole(2);

            userRepository.save(admin);
            userRepository.save(hr);
            userRepository.save(dep);

            System.out.println("Data seeded thành công!");
        } else {
            System.out.println("Users already exist, skipping seed");
        }
    }
}
