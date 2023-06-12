package com.demosecure.demosecure.controller;

import com.demosecure.demosecure.model.dto.BearerToken;
import com.demosecure.demosecure.model.dto.LoginDto;
import com.demosecure.demosecure.model.dto.SignUpDto;
import com.demosecure.demosecure.service.IDemoUserSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private IDemoUserSecurityService userSecurityService;

    @PostMapping("/signin")
    public BearerToken authenticateUser(@RequestBody LoginDto rLoginDto) {
        return userSecurityService.authenticate(rLoginDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto rSignUpDto) {
        return userSecurityService.register(rSignUpDto);
    }
}
