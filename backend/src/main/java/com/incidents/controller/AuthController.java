package com.incidents.controller;

import com.incidents.model.Role;
import com.incidents.model.User;
import com.incidents.repository.UserRepository;
import com.incidents.security.JwtUtil;
import com.incidents.security.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, 
                         UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            log.info("POST /api/auth/login - attempt for email={}", loginRequest.getEmail());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (Exception e) {
            log.warn("POST /api/auth/login - failed for email={}", loginRequest.getEmail());
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
        
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        
        log.info("POST /api/auth/login - success for email={}", loginRequest.getEmail());
        return ResponseEntity.ok(new LoginResponse(jwt));
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("POST /api/auth/register - Registration attempt for email: {}", registerRequest.getEmail());
        
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("POST /api/auth/register - Email already exists: {}", registerRequest.getEmail());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Email already exists"));
        }
        
        if (!isValidPassword(registerRequest.getPassword())) {
            log.warn("POST /api/auth/register - Password does not meet requirements for email={}", registerRequest.getEmail());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Password must be at least 8 characters long and contain at least one number, one uppercase letter, and one special character"));
        }
        
        try {
            User user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setName(registerRequest.getName());
            user.setRoles(Set.of(Role.ROLE_READ.name())); // Default roles
            
            userRepository.save(user);
            
            log.info("POST /api/auth/register - User registered successfully: {}", registerRequest.getEmail());
            
            return ResponseEntity.ok(new MessageResponse("User registered successfully"));
        } catch (Exception e) {
            log.error("POST /api/auth/register - Registration error for email={}: {}", registerRequest.getEmail(), e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred during registration"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            log.warn("GET /api/auth/me - unauthenticated request");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Unauthenticated"));
        }

        String email = auth.getName();
        log.info("GET /api/auth/me - fetch profile for email={}", email);

        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            log.warn("GET /api/auth/me - user not found: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not found"));
        }

        User user = opt.get();
        ProfileResponse resp = new ProfileResponse(user.getId(), user.getEmail(), user.getName(), user.getRoles());
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            log.warn("PUT /api/auth/me - unauthenticated request");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Unauthenticated"));
        }

        String email = auth.getName();
        log.info("PUT /api/auth/me - update attempt for email={}", email);

        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            log.warn("PUT /api/auth/me - user not found: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not found"));
        }

        try {
            User user = opt.get();
            if (request.getName() != null && !request.getName().isBlank()) {
                user.setName(request.getName().trim());
            }
            userRepository.save(user);

            log.info("PUT /api/auth/me - profile updated for email={}", email);
            ProfileResponse resp = new ProfileResponse(user.getId(), user.getEmail(), user.getName(), user.getRoles());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("PUT /api/auth/me - error updating profile email={}: {}", email, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Failed to update profile"));
        }
    }
    
    private boolean isValidPassword(String password) {
    if (password == null) return false;

    // At least 8 characters, at least one digit, at least one uppercase,
    // at least one special character,
    // and no whitespace characters allowed overall.
    String pattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[^A-Za-z0-9\\s])(?=\\S+$).{8,}$";

    return password.matches(pattern);
}

    public static class LoginRequest {
        private String email;
        private String password;
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    public static class LoginResponse {
        private String token;
        
        public LoginResponse(String token) {
            this.token = token;
        }
        
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
    
    public static class RegisterRequest {
        private String email;
        private String password;
        private String name;
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class ErrorResponse {
        private String message;
        
        public ErrorResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
    }

    public static class MessageResponse {
        private String message;
        
        public MessageResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
    }

    public static class ProfileResponse {
        private UUID id;
        private String email;
        private String name;
        private Set<String> roles;

        public ProfileResponse(UUID id, String email, String name, java.util.Set<String> roles) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.roles = roles;
        }

        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Set<String> getRoles() { return roles; }
        public void setRoles(Set<String> roles) { this.roles = roles; }
    }

    public static class UpdateProfileRequest {
        private String name;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
