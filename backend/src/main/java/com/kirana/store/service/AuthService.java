package com.kirana.store.service;

import com.kirana.store.dto.LoginRequest;
import com.kirana.store.entity.LoginAudit;
import com.kirana.store.entity.User;
import com.kirana.store.exception.BadRequestException;
import com.kirana.store.repository.LoginAuditRepository;
import com.kirana.store.repository.UserRepository;
import com.kirana.store.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final int MAX_FAILED_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final LoginAuditRepository loginAuditRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, LoginAuditRepository loginAuditRepository, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.loginAuditRepository = loginAuditRepository;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public LoginRequest.Response login(LoginRequest.Request request, HttpServletRequest httpRequest) {
        String username = request.getUsername().trim();
        String ipAddress = getClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            recordAudit(null, username, "LOGIN_FAILED", ipAddress, userAgent, false);
            logger.warn("Login failed: Username '{}' not found from IP: {}", username, ipAddress);
            throw new BadCredentialsException("Invalid username or password");
        }

        User user = userOptional.get();

        if (Boolean.TRUE.equals(user.getAccountLocked())) {
            recordAudit(user.getId(), username, "LOGIN_FAILED", ipAddress, userAgent, false);
            logger.warn("Login attempt on locked account '{}' from IP: {}", username, ipAddress);
            throw new LockedException("Account is locked due to multiple failed login attempts. Please contact Admin.");
        }

        if (Boolean.FALSE.equals(user.getEnabled())) {
            recordAudit(user.getId(), username, "LOGIN_FAILED", ipAddress, userAgent, false);
            logger.warn("Login attempt on disabled account '{}' from IP: {}", username, ipAddress);
            throw new BadRequestException("Account is disabled. Please contact Admin.");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword())
            );

            user.setFailedLoginAttempts(0);
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            String token = tokenProvider.generateToken(authentication);

            recordAudit(user.getId(), username, "LOGIN_SUCCESS", ipAddress, userAgent, true);
            logger.info("User '{}' successfully logged in from IP: {}", username, ipAddress);

            return LoginRequest.Response.builder()
                    .token(token)
                    .username(user.getUsername())
                    .role(user.getRole().name())
                    .fullName(user.getFullName())
                    .expiresIn(tokenProvider.getExpirationInSeconds())
                    .mustChangePassword(user.getMustChangePassword())
                    .build();

        } catch (BadCredentialsException ex) {
            int newFailedCount = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(newFailedCount);

            if (newFailedCount >= MAX_FAILED_ATTEMPTS) {
                user.setAccountLocked(true);
                userRepository.save(user);
                recordAudit(user.getId(), username, "ACCOUNT_LOCKED", ipAddress, userAgent, false);
                logger.warn("Account '{}' locked after {} failed login attempts from IP: {}", username, newFailedCount, ipAddress);
                throw new LockedException("Account has been locked due to 5 consecutive failed login attempts.");
            } else {
                userRepository.save(user);
                recordAudit(user.getId(), username, "LOGIN_FAILED", ipAddress, userAgent, false);
                logger.warn("Invalid password attempt ({}/{}) for user '{}' from IP: {}", newFailedCount, MAX_FAILED_ATTEMPTS, username, ipAddress);
                throw new BadCredentialsException("Invalid username or password");
            }
        }
    }

    private void recordAudit(Long userId, String username, String eventType, String ipAddress, String userAgent, boolean success) {
        try {
            LoginAudit audit = LoginAudit.builder()
                    .userId(userId)
                    .username(username)
                    .eventType(eventType)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent != null && userAgent.length() > 255 ? userAgent.substring(0, 255) : userAgent)
                    .success(success)
                    .build();
            loginAuditRepository.save(audit);
        } catch (Exception e) {
            logger.error("Failed to record login audit event", e);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
