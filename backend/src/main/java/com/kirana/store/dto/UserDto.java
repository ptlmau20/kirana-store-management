package com.kirana.store.dto;

import com.kirana.store.entity.Role;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class UserDto {

    public static class Detail {
        private Long id;
        private String username;
        private String fullName;
        private String email;
        private Role role;
        private Boolean enabled;
        private Boolean accountLocked;
        private Integer failedLoginAttempts;
        private Boolean mustChangePassword;
        private LocalDateTime lastLogin;
        private LocalDateTime createdAt;

        public Detail() {}

        public Detail(Long id, String username, String fullName, String email, Role role, Boolean enabled, Boolean accountLocked, Integer failedLoginAttempts, Boolean mustChangePassword, LocalDateTime lastLogin, LocalDateTime createdAt) {
            this.id = id;
            this.username = username;
            this.fullName = fullName;
            this.email = email;
            this.role = role;
            this.enabled = enabled;
            this.accountLocked = accountLocked;
            this.failedLoginAttempts = failedLoginAttempts;
            this.mustChangePassword = mustChangePassword;
            this.lastLogin = lastLogin;
            this.createdAt = createdAt;
        }

        public static DetailBuilder builder() { return new DetailBuilder(); }

        public static class DetailBuilder {
            private Long id;
            private String username;
            private String fullName;
            private String email;
            private Role role;
            private Boolean enabled;
            private Boolean accountLocked;
            private Integer failedLoginAttempts;
            private Boolean mustChangePassword;
            private LocalDateTime lastLogin;
            private LocalDateTime createdAt;

            public DetailBuilder id(Long id) { this.id = id; return this; }
            public DetailBuilder username(String username) { this.username = username; return this; }
            public DetailBuilder fullName(String fullName) { this.fullName = fullName; return this; }
            public DetailBuilder email(String email) { this.email = email; return this; }
            public DetailBuilder role(Role role) { this.role = role; return this; }
            public DetailBuilder enabled(Boolean enabled) { this.enabled = enabled; return this; }
            public DetailBuilder accountLocked(Boolean accountLocked) { this.accountLocked = accountLocked; return this; }
            public DetailBuilder failedLoginAttempts(Integer failedLoginAttempts) { this.failedLoginAttempts = failedLoginAttempts; return this; }
            public DetailBuilder mustChangePassword(Boolean mustChangePassword) { this.mustChangePassword = mustChangePassword; return this; }
            public DetailBuilder lastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; return this; }
            public DetailBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

            public Detail build() {
                return new Detail(id, username, fullName, email, role, enabled, accountLocked, failedLoginAttempts, mustChangePassword, lastLogin, createdAt);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Role getRole() { return role; }
        public void setRole(Role role) { this.role = role; }
        public Boolean getEnabled() { return enabled; }
        public void setEnabled(Boolean enabled) { this.enabled = enabled; }
        public Boolean getAccountLocked() { return accountLocked; }
        public void setAccountLocked(Boolean accountLocked) { this.accountLocked = accountLocked; }
        public Integer getFailedLoginAttempts() { return failedLoginAttempts; }
        public void setFailedLoginAttempts(Integer failedLoginAttempts) { this.failedLoginAttempts = failedLoginAttempts; }
        public Boolean getMustChangePassword() { return mustChangePassword; }
        public void setMustChangePassword(Boolean mustChangePassword) { this.mustChangePassword = mustChangePassword; }
        public LocalDateTime getLastLogin() { return lastLogin; }
        public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

    public static class CreateRequest {
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        private String username;

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        private String password;

        @NotBlank(message = "Full name is required")
        private String fullName;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotNull(message = "Role is required")
        private Role role;

        public CreateRequest() {}

        public CreateRequest(String username, String password, String fullName, String email, Role role) {
            this.username = username;
            this.password = password;
            this.fullName = fullName;
            this.email = email;
            this.role = role;
        }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Role getRole() { return role; }
        public void setRole(Role role) { this.role = role; }
    }

    public static class UpdateRequest {
        @NotBlank(message = "Full name is required")
        private String fullName;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotNull(message = "Role is required")
        private Role role;

        private Boolean enabled;
        private Boolean accountLocked;

        public UpdateRequest() {}

        public UpdateRequest(String fullName, String email, Role role, Boolean enabled, Boolean accountLocked) {
            this.fullName = fullName;
            this.email = email;
            this.role = role;
            this.enabled = enabled;
            this.accountLocked = accountLocked;
        }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Role getRole() { return role; }
        public void setRole(Role role) { this.role = role; }
        public Boolean getEnabled() { return enabled; }
        public void setEnabled(Boolean enabled) { this.enabled = enabled; }
        public Boolean getAccountLocked() { return accountLocked; }
        public void setAccountLocked(Boolean accountLocked) { this.accountLocked = accountLocked; }
    }

    public static class ChangePasswordRequest {
        @NotBlank(message = "Current password is required")
        private String currentPassword;

        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        private String newPassword;

        @NotBlank(message = "Password confirmation is required")
        private String confirmPassword;

        public ChangePasswordRequest() {}

        public ChangePasswordRequest(String currentPassword, String newPassword, String confirmPassword) {
            this.currentPassword = currentPassword;
            this.newPassword = newPassword;
            this.confirmPassword = confirmPassword;
        }

        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
        public String getConfirmPassword() { return confirmPassword; }
        public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    }

    public static class AdminResetPasswordRequest {
        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        private String newPassword;

        public AdminResetPasswordRequest() {}

        public AdminResetPasswordRequest(String newPassword) {
            this.newPassword = newPassword;
        }

        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    public static class AuditLog {
        private Long id;
        private Long userId;
        private String username;
        private String eventType;
        private String ipAddress;
        private String userAgent;
        private Boolean success;
        private LocalDateTime createdAt;

        public AuditLog() {}

        public AuditLog(Long id, Long userId, String username, String eventType, String ipAddress, String userAgent, Boolean success, LocalDateTime createdAt) {
            this.id = id;
            this.userId = userId;
            this.username = username;
            this.eventType = eventType;
            this.ipAddress = ipAddress;
            this.userAgent = userAgent;
            this.success = success;
            this.createdAt = createdAt;
        }

        public static AuditLogBuilder builder() { return new AuditLogBuilder(); }

        public static class AuditLogBuilder {
            private Long id;
            private Long userId;
            private String username;
            private String eventType;
            private String ipAddress;
            private String userAgent;
            private Boolean success;
            private LocalDateTime createdAt;

            public AuditLogBuilder id(Long id) { this.id = id; return this; }
            public AuditLogBuilder userId(Long userId) { this.userId = userId; return this; }
            public AuditLogBuilder username(String username) { this.username = username; return this; }
            public AuditLogBuilder eventType(String eventType) { this.eventType = eventType; return this; }
            public AuditLogBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
            public AuditLogBuilder userAgent(String userAgent) { this.userAgent = userAgent; return this; }
            public AuditLogBuilder success(Boolean success) { this.success = success; return this; }
            public AuditLogBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

            public AuditLog build() {
                return new AuditLog(id, userId, username, eventType, ipAddress, userAgent, success, createdAt);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }
        public String getIpAddress() { return ipAddress; }
        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
        public String getUserAgent() { return userAgent; }
        public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
        public Boolean getSuccess() { return success; }
        public void setSuccess(Boolean success) { this.success = success; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }
}
