package com.historia.zetu.services.serviceImp;

import com.historia.zetu.Repository.UserRepo;
import com.historia.zetu.execptions.AlreadySubscribed;
import com.historia.zetu.model.Users;
import com.historia.zetu.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final UserRepo userRepo;

    @Override
    public boolean isSubscribed(String email) {
       Optional<Users> user = userRepo.findByEmail(email);
        return user.isPresent();
    }

    @Override
    public boolean subscribe(String email) {
            Optional<Users> user = userRepo.findByEmail(email);
            if (user.isPresent()) {
                return false;
            }
            Users newSubscriber = new Users();
            newSubscriber.setEmail(email);
            newSubscriber.setActive(false);
            newSubscriber.setPassword(null);
            newSubscriber.setRole("ROLE_USER");
            userRepo.save(newSubscriber);
            return true;
    }

    @Override
    public boolean unSubscribe(String email) {
        Optional<Users> user = userRepo.findByEmail(email);
        if (user.isPresent()) {
            userRepo.delete(user.get());
            return true;
        }
        return false;
    }
}
