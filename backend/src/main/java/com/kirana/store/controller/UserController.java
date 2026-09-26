package com.kirana.store.controller;

import com.kirana.store.dto.UserDto;
import com.kirana.store.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto.Detail>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto.Detail> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto.Detail> createUser(
            @Valid @RequestBody UserDto.CreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        UserDto.Detail created = userService.createUser(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto.Detail> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDto.UpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        UserDto.Detail updated = userService.updateUser(id, request, userDetails.getUsername());
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @Valid @RequestBody UserDto.ChangePasswordRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        userService.changePassword(userDetails.getUsername(), request);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }

    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> adminResetPassword(
            @PathVariable Long id,
            @Valid @RequestBody UserDto.AdminResetPasswordRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        userService.adminResetPassword(id, request, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "User password reset successfully"));
    }
}
