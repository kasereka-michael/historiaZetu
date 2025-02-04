package com.historia.zetu.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ReaderInformations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long readerId;
    private String userAgent;
    private String ipAddress;
    private String country;
    private String city;
    private long number;
}
