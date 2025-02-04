package com.historia.zetu.Repository;

import com.historia.zetu.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {
    // Check if the user has already liked the story
    boolean existsByStoryHistoryIdAndUsername(Long storyId, String username);

    long countByStoryHistoryId(Long storyId);

    // Delete a like by storyId and username
    void deleteByStoryHistoryIdAndUsername(Long storyId, String username);

}
