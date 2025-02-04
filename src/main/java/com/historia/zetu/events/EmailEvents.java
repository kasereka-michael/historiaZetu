package com.historia.zetu.events;


import com.historia.zetu.emailUtils.SignUpEmailEvents;
import com.historia.zetu.model.Users;
import com.historia.zetu.services.serviceImp.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class EmailEvents {

    private final EmailService emailService;

    @EventListener
    public void sendEmailOtpToNewUser(SignUpEmailEvents event) {
        Users user = (Users) event.getSource();
        emailService.confirmAdminAccountEmail(getNameFromEmail(user.getEmail()), user.getEmail(),event.getOtp());
    }

    public static String getNameFromEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "Unknown"; // Handle invalid or null email cases
        }

        // Extract the part before the '@' symbol
        String localPart = email.split("@")[0];

        // Split the local part by common delimiters such as '.' or '_'
        String[] nameParts = localPart.split("[._]");

        // Capitalize each part and join them with a space
        StringBuilder nameBuilder = new StringBuilder();
        for (String part : nameParts) {
            if (!part.isEmpty()) {
                nameBuilder.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        // Trim and return the final name
        return nameBuilder.toString().trim();
    }
}
