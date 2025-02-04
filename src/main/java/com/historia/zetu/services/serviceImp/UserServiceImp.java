package com.historia.zetu.services.serviceImp;

import com.historia.zetu.Repository.UserRepo;
import com.historia.zetu.emailUtils.SignUpEmailEvents;
import com.historia.zetu.model.Confirmation;
import com.historia.zetu.model.Users;
import com.historia.zetu.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImp implements UserService {

    public static final String NEW_ACCOUNT_CREATION = "NEW ACCOUNT CREATION";
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final ConfirmationServiceImpl confirmationService;

    @Override
    public long saveUser(Users user) {
        Optional<Users> existingUser = userRepo.findByEmail(user.getEmail());

        log.info(user.getEmail());
        log.info(user.getPassword());
        log.info(user.getPhoneNumber());

        if (existingUser.isPresent()) {
            return 0;
        }
        Users newUser = new Users();

        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole("ROLE_ADMIN");
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setActive(false);

        Users savedUser = userRepo.save(newUser);

        Confirmation confirmation = confirmationService.createConfirmationForUser(savedUser);

        eventPublisher.publishEvent(new SignUpEmailEvents(newUser, NEW_ACCOUNT_CREATION, confirmation.getOtp()));

        return savedUser.getId();
    }

    @Override
    public Users getUserById(Long userId) {
        return null;
    }

    @Override
    public boolean updateUser(Users user) {
        return false;
    }

    @Override
    public boolean deleteUser(Long userId) {
        return false;
    }

    @Override
    public boolean isEnabled(Users user) {
        return false;
    }

    @Override
    public boolean sendEmailToSubscriber(String email) {
        return false;
    }

    @Override
    public void sendEmailToAllSubscribers() {

    }

    @Override
    public List<Users> getAllSubscribers() {
        return List.of();
    }
}
