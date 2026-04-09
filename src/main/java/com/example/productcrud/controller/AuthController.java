package com.example.productcrud.controller;

import com.example.productcrud.dto.RegisterRequest;
import com.example.productcrud.model.User;
import com.example.productcrud.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= LOGIN =================
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // ================= REGISTER PAGE =================
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    // ================= REGISTER PROCESS =================
    @PostMapping("/register")
    public String processRegister(
            @ModelAttribute RegisterRequest registerRequest,
            Model model) {

        // cek password sama
        if (!registerRequest.getPassword()
                .equals(registerRequest.getConfirmPassword())) {

            model.addAttribute("error", "Password tidak sama");
            return "register";
        }

        // cek username sudah ada
        if (userRepository.findByUsername(
                registerRequest.getUsername()).isPresent()) {

            model.addAttribute("error", "Username sudah digunakan");
            return "register";
        }

        // simpan user baru
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(
                passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);

        return "redirect:/login";
    }
}