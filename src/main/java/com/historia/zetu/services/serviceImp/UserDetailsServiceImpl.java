package com.historia.zetu.services.serviceImp;

import com.historia.zetu.Repository.UserRepo;
import com.historia.zetu.model.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("UserDetailsServiceImpl loadUserByUsername start: {}", username);
        Users user = userRepo.findByEmailAndActiveIsTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        log.info("UserDetailsServiceImpl loadUserByUsername: {}", user.getEmail());
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole())))
                .build();

    }
}
