package com.historia.zetu.resources;

import com.historia.zetu.model.Confirmation;
import com.historia.zetu.model.Users;
import com.historia.zetu.services.ConfirmationService;
import com.historia.zetu.services.serviceImp.UserServiceImp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final UserServiceImp userServiceImp;
    private final ConfirmationService confirmationService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Users signupRequest) {
        try {
            // Save the user and get their ID
            long userId = userServiceImp.saveUser(signupRequest);
            log.info("User created successfully with email: {}", signupRequest.getEmail());
            log.info("Phone number: {}", signupRequest.getPhoneNumber());

            // Respond with the user ID
            return ResponseEntity.ok(Math.toIntExact(userId));
        } catch (Exception e) {
            // Log the error
            log.error("Error occurred during signup for email: {}: {}", signupRequest.getEmail(), e.getMessage());

            // Return appropriate error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create account.");
        }
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Confirmation otpRequest) {
        log.info("Verifying OTP for User ID: {}", otpRequest.getUsers().getId());

        boolean isVerified = confirmationService.verifyOtp(otpRequest.getUsers().getId(), otpRequest.getOtp());
        if (isVerified) {
            return ResponseEntity.ok("OTP verified successfully.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP.");
    }

}