package com.kirana.store.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_audit")
public class LoginAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    @Column(nullable = false)
    private Boolean success;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public LoginAudit() {}

    public LoginAudit(Long id, Long userId, String username, String eventType, String ipAddress, String userAgent, Boolean success, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.eventType = eventType;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.success = success;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static LoginAuditBuilder builder() { return new LoginAuditBuilder(); }

    public static class LoginAuditBuilder {
        private Long id;
        private Long userId;
        private String username;
        private String eventType;
        private String ipAddress;
        private String userAgent;
        private Boolean success;
        private LocalDateTime createdAt;

        public LoginAuditBuilder id(Long id) { this.id = id; return this; }
        public LoginAuditBuilder userId(Long userId) { this.userId = userId; return this; }
        public LoginAuditBuilder username(String username) { this.username = username; return this; }
        public LoginAuditBuilder eventType(String eventType) { this.eventType = eventType; return this; }
        public LoginAuditBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public LoginAuditBuilder userAgent(String userAgent) { this.userAgent = userAgent; return this; }
        public LoginAuditBuilder success(Boolean success) { this.success = success; return this; }
        public LoginAuditBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public LoginAudit build() {
            return new LoginAudit(id, userId, username, eventType, ipAddress, userAgent, success, createdAt);
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
