package com.kirana.store.service;

import com.kirana.store.dto.UserDto;
import com.kirana.store.entity.LoginAudit;
import com.kirana.store.entity.User;
import com.kirana.store.exception.BadRequestException;
import com.kirana.store.exception.ResourceNotFoundException;
import com.kirana.store.repository.LoginAuditRepository;
import com.kirana.store.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    private final UserRepository userRepository;
    private final LoginAuditRepository loginAuditRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, LoginAuditRepository loginAuditRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.loginAuditRepository = loginAuditRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UserDto.Detail> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDetail)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto.Detail getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToDetail(user);
    }

    @Transactional
    public UserDto.Detail createUser(UserDto.CreateRequest request, String adminUsername) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username '" + request.getUsername() + "' is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email '" + request.getEmail() + "' is already in use");
        }

        validatePasswordPolicy(request.getPassword());

        User user = User.builder()
                .username(request.getUsername().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName().trim())
                .email(request.getEmail().trim())
                .role(request.getRole())
                .enabled(true)
                .accountLocked(false)
                .failedLoginAttempts(0)
                .mustChangePassword(true)
                .build();

        User savedUser = userRepository.save(user);

        recordAudit(savedUser.getId(), savedUser.getUsername(), "USER_CREATED", "By Admin: " + adminUsername, true);
        logger.info("Admin '{}' created new user '{}' with role {}", adminUsername, savedUser.getUsername(), savedUser.getRole());

        return mapToDetail(savedUser);
    }

    @Transactional
    public UserDto.Detail updateUser(Long id, UserDto.UpdateRequest request, String adminUsername) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setFullName(request.getFullName().trim());
        user.setEmail(request.getEmail().trim());
        user.setRole(request.getRole());

        if (request.getEnabled() != null) {
            if (user.getEnabled() && !request.getEnabled()) {
                recordAudit(user.getId(), user.getUsername(), "USER_DISABLED", "Disabled by Admin: " + adminUsername, true);
            }
            user.setEnabled(request.getEnabled());
        }

        if (request.getAccountLocked() != null) {
            user.setAccountLocked(request.getAccountLocked());
            if (!request.getAccountLocked()) {
                user.setFailedLoginAttempts(0);
            }
        }

        User updatedUser = userRepository.save(user);
        logger.info("Admin '{}' updated user '{}'", adminUsername, updatedUser.getUsername());

        return mapToDetail(updatedUser);
    }

    @Transactional
    public void changePassword(String username, UserDto.ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }

        validatePasswordPolicy(request.getNewPassword());

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setMustChangePassword(false);
        userRepository.save(user);

        recordAudit(user.getId(), username, "PASSWORD_CHANGED", "Self Password Change", true);
        logger.info("User '{}' successfully changed password", username);
    }

    @Transactional
    public void adminResetPassword(Long userId, UserDto.AdminResetPasswordRequest request, String adminUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        validatePasswordPolicy(request.getNewPassword());

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setMustChangePassword(true);
        user.setAccountLocked(false);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);

        recordAudit(user.getId(), user.getUsername(), "PASSWORD_RESET", "Reset by Admin: " + adminUsername, true);
        logger.info("Admin '{}' reset password for user '{}'", adminUsername, user.getUsername());
    }

    public void validatePasswordPolicy(String password) {
        if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
            throw new BadRequestException("Password does not meet complexity requirements. Must be at least 8 characters long, include at least one uppercase letter, one lowercase letter, and one number.");
        }
    }

    private UserDto.Detail mapToDetail(User user) {
        return UserDto.Detail.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .accountLocked(user.getAccountLocked())
                .failedLoginAttempts(user.getFailedLoginAttempts())
                .mustChangePassword(user.getMustChangePassword())
                .lastLogin(user.getLastLogin())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private void recordAudit(Long userId, String username, String eventType, String note, boolean success) {
        try {
            LoginAudit audit = LoginAudit.builder()
                    .userId(userId)
                    .username(username)
                    .eventType(eventType)
                    .ipAddress(note)
                    .success(success)
                    .build();
            loginAuditRepository.save(audit);
        } catch (Exception e) {
            logger.error("Failed to record audit event", e);
        }
    }
}
