package com.incidents.controller;

import com.incidents.model.User;
import com.incidents.repository.UserRepository;
import com.incidents.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, 
                         JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
        
        final UserDetails userDetails = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        final String jwt = jwtUtil.generateToken(userDetails);
        
        return ResponseEntity.ok(new LoginResponse(jwt));
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setName(registerRequest.getName());
        user.setRoles(Set.of(Role.ROLE_READ, Role.ROLE_WRITE)); // Default
        
        userRepository.save(user);
        
        return ResponseEntity.ok("User registered successfully");
    }

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
    	log.info("Registration attempt for email: {}", registerRequest.getEmail());
    	
    	if (userRepository.existsByEmail(registerRequest.getEmail())) {
    	    log.warn("Registration failed: Email already exists - {}", registerRequest.getEmail());
    	    return ResponseEntity
    	            .status(HttpStatus.CONFLICT)
    	            .body(new ErrorResponse("Email already exists"));
    	}
    	
    	if (!isValidPassword(registerRequest.getPassword())) {
    	    log.warn("Registration failed: Password does not meet requirements");
    	    return ResponseEntity
    	            .status(HttpStatus.BAD_REQUEST)
    	            .body(new ErrorResponse("Password must be at least 8 characters long and contain at least one number, one uppercase letter, and one special character"));
    	}
    	
    	try {
    	    User user = new User();
    	    user.setEmail(registerRequest.getEmail());
    	    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    	    user.setName(registerRequest.getName());
    	    user.setRoles(Set.of(Role.ROLE_READ)); // Default role for new users
    	    
    	    userRepository.save(user);
    	    
    	    log.info("User registered successfully: {}", registerRequest.getEmail());
    	    
    	    return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    	} catch (Exception e) {
    	    log.error("Registration error: {}", e.getMessage(), e);
        	return ResponseEntity
        	        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        	        .body(new ErrorResponse("An error occurred during registration"));
    	}
	}
    
	private boolean isValidPassword(String password) {
    	// At least 8 characters, one number, one uppercase, one special character
    	String pattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    	return password != null && password.matches(pattern);
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
}
