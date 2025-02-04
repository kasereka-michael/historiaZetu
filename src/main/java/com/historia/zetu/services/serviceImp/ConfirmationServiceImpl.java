package com.historia.zetu.services.serviceImp;

import com.historia.zetu.Repository.ConfirmationRepo;
import com.historia.zetu.Repository.UserRepo;
import com.historia.zetu.model.Confirmation;
import com.historia.zetu.model.Users;
import com.historia.zetu.services.ConfirmationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {

    private final ConfirmationRepo confirmationRepo;
    private final UserRepo userRepo;

    @Override
    public Confirmation createConfirmationForUser(Users user) {
        return confirmationRepo.save(new Confirmation(user));
    }

    @Override
    public boolean verifyOtp(long userId, String otp) {
        // Retrieve the confirmation record
        Confirmation confirmation = confirmationRepo.findByUsersIdAndOtp(userId, otp);

        // Check if the confirmation record exists
        if (confirmation == null) {
            log.warn("Invalid OTP or user ID: userId={}, otp={}", userId, otp);
            return false; // Return false if no record is found
        }

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime otpExpiryTime = confirmation.getCreatedDate().plusMinutes(3);

        // Check if the OTP is expired
        if (currentTime.isAfter(otpExpiryTime)) {
            log.warn("OTP expired for userId={}, otp={}", userId, otp);
            return false;
        }

        // Attempt to activate the user account
        int response = userRepo.setActiveTrue(userId);

        if (response == 1) {
            log.info("User activated successfully: userId={}", userId);
            return true; // Return true if activation was successful
        } else {
            log.error("Failed to activate user account: userId={}", userId);
            return false; // Return false if activation failed
        }
    }

}
