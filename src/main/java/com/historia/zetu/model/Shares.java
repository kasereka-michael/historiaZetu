package com.historia.zetu.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@Entity
public class Shares {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "history_Id")
    @JsonBackReference
    private Story story;
    private String sessionId;
    private String ipAddress;
    private LocalDateTime createdAt;
    private String username;

}
