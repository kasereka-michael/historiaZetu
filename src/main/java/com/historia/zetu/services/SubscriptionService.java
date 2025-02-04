package com.historia.zetu.services;

public interface SubscriptionService {

    boolean isSubscribed(String email);
    boolean subscribe(String email);

    boolean unSubscribe(String email);
}
