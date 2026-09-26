package com.kirana.store.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    public static class Request {
        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;

        public Request() {}

        public Request(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class Response {
        private String token;
        private String username;
        private String role;
        private String fullName;
        private Long expiresIn;
        private Boolean mustChangePassword;

        public Response() {}

        public Response(String token, String username, String role, String fullName, Long expiresIn, Boolean mustChangePassword) {
            this.token = token;
            this.username = username;
            this.role = role;
            this.fullName = fullName;
            this.expiresIn = expiresIn;
            this.mustChangePassword = mustChangePassword;
        }

        public static ResponseBuilder builder() { return new ResponseBuilder(); }

        public static class ResponseBuilder {
            private String token;
            private String username;
            private String role;
            private String fullName;
            private Long expiresIn;
            private Boolean mustChangePassword;

            public ResponseBuilder token(String token) { this.token = token; return this; }
            public ResponseBuilder username(String username) { this.username = username; return this; }
            public ResponseBuilder role(String role) { this.role = role; return this; }
            public ResponseBuilder fullName(String fullName) { this.fullName = fullName; return this; }
            public ResponseBuilder expiresIn(Long expiresIn) { this.expiresIn = expiresIn; return this; }
            public ResponseBuilder mustChangePassword(Boolean mustChangePassword) { this.mustChangePassword = mustChangePassword; return this; }

            public Response build() {
                return new Response(token, username, role, fullName, expiresIn, mustChangePassword);
            }
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public Long getExpiresIn() { return expiresIn; }
        public void setExpiresIn(Long expiresIn) { this.expiresIn = expiresIn; }

        public Boolean getMustChangePassword() { return mustChangePassword; }
        public void setMustChangePassword(Boolean mustChangePassword) { this.mustChangePassword = mustChangePassword; }
    }
}
