package com.historia.zetu.services;


import com.historia.zetu.model.Users;

import java.util.List;

public interface UserService {

    long saveUser(Users user);

    Users getUserById(Long userId);

    boolean updateUser(Users user);

    boolean deleteUser(Long userId);

    boolean isEnabled(Users user);

    boolean sendEmailToSubscriber(String email);

    void sendEmailToAllSubscribers();

    List<Users> getAllSubscribers();
}
