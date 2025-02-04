package com.historia.zetu.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long historyId;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private String historyContent;
    private String historyCategory;
    private String storyCaption;
    private String storyAuthor;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private String historyMainImage;
    private String storyTitle;

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Likes> likes = new ArrayList<>();

    @Transient
    private long likesCount;

    private String storyPhotoGrapherName;
    @CreationTimestamp
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime historyDate;
    @Column(columnDefinition = "LONGBLOB")
    private String moreInfos;

    @ElementCollection
    @CollectionTable(
            name = "post_hashtags",
            joinColumns = @JoinColumn(name = "history_id")
    )
    private List<String> hastTags;
    @OneToMany(mappedBy = "history", cascade = CascadeType.MERGE , orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comments> comments = new ArrayList<>();
    private String storyWhatsAppLink;
    private String storyFacebookLink;
    private String storyInstagramLink;
    private String storyTwitterLink;
    private String storyYoutubeLink;
    private String storyTiktokLink;
    @ManyToOne
    @JoinColumn(name = "id")
    private Users postedBy;

    public long getLikesCount() {
        return (likes != null) ? likes.size() : 0;
    }


}
