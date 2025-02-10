package com.historia.zetu.services;

import com.historia.zetu.model.Story;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;


import java.util.List;
import java.util.Map;

public interface HistoryService {

    Story saveHistory(Story history);

    Story getHistoryById(Long historyId);

    Story getLastPostedStory();

    Page<Story> findHistoryByHashtagContainingItems(List<String> hashtags, int page, int size);

    boolean updateHistory(Story history);

    boolean deleteHistory(Long historyId);

    Page<Story> getStories(int page, int size, String searchTerm);

    long findUserEmail(String email);
    Map<String, Object> toggleRead(Long storyId, String username);
    boolean checkUserNameHistoryLike(Long storyId, String username);

    // Method to toggle like/unlike
    @Transactional
    Map<String, Object> toggleShares(Long storyId, String username);

    // Method to toggle like/unlike
    @Transactional
    Map<String, Object> toggleShares(Long storyId);
}
