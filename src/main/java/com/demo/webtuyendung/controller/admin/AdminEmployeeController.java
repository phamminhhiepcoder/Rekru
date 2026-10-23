package com.demo.webtuyendung.controller.admin;


import com.demo.webtuyendung.entity.EmployeeEntity;
import com.demo.webtuyendung.entity.UserEntity;
import com.demo.webtuyendung.enums.Position;
import com.demo.webtuyendung.mapper.EmployeeMapper;
import com.demo.webtuyendung.payload.EmployeeDto;
import com.demo.webtuyendung.repository.EmployeeRepository;
import com.demo.webtuyendung.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/employee")
public class AdminEmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public String getAll(Model model) {
        List<EmployeeDto> employees = employeeRepository.findAll().stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("employees", employees);
        return "admin/employee/view";
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("employeeDto", new EmployeeDto());
        model.addAttribute("positions", Position.values());

        return "admin/employee/add";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("employeeDto") @Valid EmployeeDto employeeDto,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "admin/employee/add";
        }

        if (userRepository.findByEmail(employeeDto.getEmail()).isPresent()) {
            model.addAttribute("emailError", "Email đã tồn tại rồi!");
            return "admin/employee/add";
        }

        Position position = Position.fromPositionName(employeeDto.getPosition());

        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setFullName(employeeDto.getFullName());
        employeeEntity.setGender(employeeDto.getGender());
        employeeEntity.setDepartment(employeeDto.getDepartment());


        employeeEntity.setPhoneNumber(employeeDto.getPhoneNumber());

        UserEntity user = new UserEntity();
        user.setStatus(1);
        user.setPassword(passwordEncoder.encode("123456a@A"));
        user.setEmail(employeeDto.getEmail());
        if (employeeDto.getDepartment().equalsIgnoreCase("Nhân sự")) {
            user.setRole(1);
        } else user.setRole(2);
        user = userRepository.save(user);

        employeeEntity.setUser(user);
        employeeEntity.setPosition(position);

        employeeRepository.save(employeeEntity);

        return "redirect:/admin/employee?add=true";
    }


    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable("id") Integer id, Model model) {
        EmployeeEntity employeeEntity = employeeRepository.findById(id).orElse(null);

        if (employeeEntity != null) {
            EmployeeDto dto = EmployeeMapper.toDto(employeeEntity);
            model.addAttribute("employeeDto", dto);
            model.addAttribute("positions", Position.values());

            return "admin/employee/edit";
        }

        return "redirect:/admin/employee";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("employeeDto") @Valid EmployeeDto employeeDto,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            return "admin/employee/edit";
        }
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID"));
        UserEntity user = employeeEntity.getUser();

        if (userRepository.findByEmail(employeeDto.getEmail()).isPresent()) {
            UserEntity check = userRepository.findByEmail(employeeDto.getEmail()).get();
            if (!Objects.equals(check.getId(), user.getId())) {
                model.addAttribute("emailError", "Email đã tồn tại rồi!");
                return "admin/employee/edit";
            }
        }

        employeeEntity.setFullName(employeeDto.getFullName());
        employeeEntity.setGender(employeeDto.getGender());
        employeeEntity.setPhoneNumber(employeeDto.getPhoneNumber());

        user.setEmail(employeeDto.getEmail());
        if (employeeDto.getDepartment().equalsIgnoreCase("Nhân sự")) {
            user.setRole(1);
        } else user.setRole(2);

        user = userRepository.save(user);

        employeeEntity.setUser(user);

        Position position = Position.fromPositionName(employeeDto.getPosition());

        employeeEntity.setDepartment(employeeDto.getDepartment());
        employeeEntity.setPosition(position);
        employeeEntity.setStatus(employeeDto.getStatus());

        employeeRepository.save(employeeEntity);

        return "redirect:/admin/employee?update=true";
    }


    @GetMapping("/update-status/{id}/{newStatus}")
    public String toggleStatus(@PathVariable("id") Integer id,
                               @PathVariable("newStatus") Integer newStatus) {
        EmployeeEntity employeeEntity = employeeRepository.findById(id).orElse(null);
        if (employeeEntity != null) {
            UserEntity user = employeeEntity.getUser();
            user.setStatus(newStatus);
            userRepository.save(user);

            employeeEntity.setStatus(newStatus);
            employeeRepository.save(employeeEntity);
            return "redirect:/admin/employee?updateStatus=true";
        }

        return "redirect:/admin/employee";
    }
}
