package com.example.coworkinghub.controller;

import com.example.coworkinghub.dto.RegisterRequest;
import com.example.coworkinghub.model.User;
import com.example.coworkinghub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterRequest req) {
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(req.getPassword());
        u.setRole(req.getRole());
        return userService.register(u);
    }
}
