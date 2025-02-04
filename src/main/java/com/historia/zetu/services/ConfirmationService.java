package com.historia.zetu.services;

import com.historia.zetu.model.Confirmation;
import com.historia.zetu.model.Users;

public interface ConfirmationService {
    Confirmation createConfirmationForUser(Users user);
    boolean verifyOtp(long userId, String otp);
}
