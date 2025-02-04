package com.historia.zetu.execptions;

public class AlreadySubscribed extends RuntimeException {
    public AlreadySubscribed(String userIsAlreadySubscribed) {
        super(userIsAlreadySubscribed);
    }
}
