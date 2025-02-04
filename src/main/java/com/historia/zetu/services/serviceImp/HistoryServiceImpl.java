package com.historia.zetu.services.serviceImp;

import com.historia.zetu.Repository.HistoryRepo;
import com.historia.zetu.Repository.LikeRepository;
import com.historia.zetu.Repository.UserRepo;
import com.historia.zetu.model.Likes;
import com.historia.zetu.model.Story;
import com.historia.zetu.model.Users;
import com.historia.zetu.services.HistoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepo historyRepo;
    private final UserRepo userRepo;
    private final LikeRepository likeRepository;
    @Override
    public Story saveHistory(Story history) {

        Users user = userRepo.findById(history.getPostedBy().getId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        log.info("User found: {}", user.getEmail());

        history.setPostedBy(user);


        return historyRepo.save(history);
    }

    @Override
    public Story getHistoryById(Long historyId) {
        return historyRepo.findById(historyId).orElse(null);
    }

    @Override
    public Story getLastPostedStory() {
        return historyRepo.findFirstByOrderByHistoryIdDesc();
    }

    @Override
    public Page<Story> findHistoryByHashtagContainingItems(List<String> hashtags, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return historyRepo.findByHashtagsIn(hashtags, pageable);
    }


    @Override
    public boolean updateHistory(Story history) {
        return false;
    }

    @Override
    public boolean deleteHistory(Long historyId) {
        return false;
    }

    public Map<String, List<Story>> getStoryGroupedByMonth() {
        List<Story> allStory = historyRepo.findAllOrderByDateDesc();

        // Group the Story by month and year
        return allStory.stream().collect(Collectors.groupingBy(history -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            return history.getHistoryDate().format(formatter);
        }));
    }

    public Page<Story> getStories(int page, int size, String searchTerm) {
        Pageable pageable = PageRequest.of(page, size);

        if (searchTerm != null && !searchTerm.isEmpty()) {
            return historyRepo.findByStoryTitleContainingIgnoreCaseOrHistoryCategoryContainingIgnoreCase(
                    searchTerm, searchTerm, pageable);
        } else {
            return historyRepo.findAll(pageable);
        }
    }

    @Override
    public long findUserEmail(String email) {
        // Find the user by email
        Optional<Users> users = userRepo.findByEmail(email);
        log.info("Received email in service: {} ", users.get().getEmail());
        // If user is not found, throw a custom exception or return a default value
        Users user = users.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        // Return the user's ID
        return user.getId();
    }


    // Method to toggle like/unlike
    @Override
    @Transactional
    public Map<String, Object> toggleLike(Long storyId, String username) {
        // Find the story by ID
        Story story = historyRepo.findById(storyId)
                .orElseThrow(() -> new RuntimeException("Story not found"));

        log.info("The story retrieved :: {}", story.getLikesCount());

        // Check if the user has already liked the story
        boolean hasLiked = likeRepository.existsByStoryHistoryIdAndUsername(storyId, username);

        if (hasLiked) {
            // If the user has already liked the story, remove the like
            likeRepository.deleteByStoryHistoryIdAndUsername(storyId, username);
            story.setLikesCount(story.getLikesCount() - 1);
        } else {
            // If the user hasn't liked the story, add a like
            Likes like = new Likes();
            like.setStory(story);
            like.setUsername(username);
            likeRepository.save(like);
            story.setLikesCount(story.getLikesCount() + 1);
        }

        // Prepare the response map
        Map<String, Object> response = new HashMap<>();
        response.put("hasLiked", hasLiked); // New like status after toggling
        response.put("likeCount", story.getLikesCount());

        return response;
    }


    @Override
    public boolean checkUserNameHistoryLike(Long storyId, String username) {
       return likeRepository.existsByStoryHistoryIdAndUsername(storyId, username);
    }

    public Page<Story> getStoriesRelated(int page, int size, long id) {
        Pageable pageable = PageRequest.of(page, size);
        Optional<Story> storyOpt = historyRepo.findById(id);

        if (storyOpt.isPresent()) {
            Story story = storyOpt.get();
            String category = story.getHistoryCategory();

            // Fetch only a paginated set of related stories from DB (excluding the current story)
            Page<Story> storiesPage = historyRepo.findByHistoryCategoryAndIdNot(category, id, pageable);

            // Combine the main story with related paginated stories
            List<Story> finalStories = new ArrayList<>();
            finalStories.add(story); // Add the current story first
            finalStories.addAll(storiesPage.getContent()); // Add paginated related stories

            // Create a new paginated response
            return new PageImpl<>(finalStories, pageable, storiesPage.getTotalElements() + 1);
        }

        return Page.empty();
    }





}
