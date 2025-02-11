package com.historia.zetu.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private String commentContent;
    private LocalDateTime commentDate;
    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    @JsonBackReference
    private Comments parentComment;
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.MERGE)
    @JsonManagedReference
    private List<Comments> subComments = new ArrayList<>();
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "history_id")
    private Story history;
    private String commenterEmail;
}
