package com.kirana.store.controller;

import com.kirana.store.dto.LoginRequest;
import com.kirana.store.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRequest.Response> login(
            @Valid @RequestBody LoginRequest.Request request,
            HttpServletRequest httpRequest) {
        LoginRequest.Response response = authService.login(request, httpRequest);
        return ResponseEntity.ok(response);
    }
}
