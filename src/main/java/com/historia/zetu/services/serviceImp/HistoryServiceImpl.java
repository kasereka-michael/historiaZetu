package com.historia.zetu.services.serviceImp;

import com.historia.zetu.Repository.HistoryRepo;
import com.historia.zetu.Repository.ReadRepository;
import com.historia.zetu.Repository.ShareRepository;
import com.historia.zetu.Repository.UserRepo;
import com.historia.zetu.model.Reads;
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
    private final ReadRepository readRepository;
    private final ShareRepository shareRepository;

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
    public Map<String, Object> toggleRead(Long storyId, String username) {
        // Find the story by ID
        Story story = historyRepo.findById(storyId)
                .orElseThrow(() -> new RuntimeException("Story not found"));

        log.info("The story retrieved :: {}", story.getReadsCount());

        // Check if the user has already liked the story
        boolean hasLiked = readRepository.existsByStoryHistoryIdAndUsername(storyId, username);

        if (hasLiked) {
            // If the user has already liked the story, remove the like
            readRepository.deleteByStoryHistoryIdAndUsername(storyId, username);
            story.setLikesCount(story.getReadsCount() - 1);
        } else {
            // If the user hasn't liked the story, add a like
            Reads like = new Reads();
            like.setStory(story);
            like.setUsername(username);
            readRepository.save(like);
            story.setLikesCount(story.getReadsCount() + 1);
        }

        // Prepare the response map
        Map<String, Object> response = new HashMap<>();
        response.put("hasRead", hasLiked); // New like status after toggling
        response.put("ReadCount", story.getReadsCount());

        return response;
    }


    @Override
    public boolean checkUserNameHistoryLike(Long storyId, String username) {
       return readRepository.existsByStoryHistoryIdAndUsername(storyId, username);
    }

    @Override
    public Map<String, Object> toggleShares(Long storyId, String username) {
        return Map.of();
    }

    @Override
    public Map<String, Object> toggleShares(Long storyId) {
        return Map.of();
    }

    public Page<Story> getStoriesRelated(int page, int size, long id) {
        Pageable pageable = PageRequest.of(page, size);
        Optional<Story> storyOpt = historyRepo.findById(id);

        if (storyOpt.isPresent()) {
            Story story = storyOpt.get();
            String category = story.getHistoryCategory();

            // Fetch only a paginated set of related stories from DB (excluding the current story)
            Page<Story> storiesPage = historyRepo.findByHistoryCategoryAndHistoryIdNot(category, id, pageable);

            // Combine the main story with related paginated stories
            List<Story> finalStories = new ArrayList<>(storiesPage.getContent()); // Add paginated related stories

            // Create a new paginated response
            return new PageImpl<>(finalStories, pageable, storiesPage.getTotalElements() + 1);
        }

        return Page.empty();
    }


//    @Transactional
//    public boolean saveStoryRead(Long userId, Long storyId, String sessionId, String ipAddress) {
//        Optional<StoryRead> existingRead;
//
//        if (userId != null) {
//            existingRead = storyReadRepository.findByUserIdAndStoryId(userId, storyId);
//        } else {
//            existingRead = storyReadRepository.findBySessionIdAndStoryId(sessionId, storyId);
//        }
//
//        if (existingRead.isPresent()) {
//            return false; // User has already read the story
//        }
//
//        StoryRead storyRead = new StoryRead();
//        storyRead.setUser(userId != null ? new User(userId) : null);
//        storyRead.setStory(new Story(storyId));
//        storyRead.setSessionId(sessionId);
//        storyRead.setIpAddress(ipAddress);
//        storyRead.setCreatedAt(LocalDateTime.now());
//
//        storyReadRepository.save(storyRead);
//        return true; // Successfully saved
//    }



    // Method to toggle like/unlike
//    @Transactional
//    @Override
//    public Map<String, Object> toggleShares(Long storyId) {
//        // Find the story by ID
//        Story story = historyRepo.findById(storyId)
//                .orElseThrow(() -> new RuntimeException("Story not found"));
//
//        log.info("The story retrieved :: {}", story.getReadsCount());
//
//        // Check if the user has already liked the story
//        boolean hasRead = shareRepository.existsByStoryHistoryId(storyId);
//
//        if (hasRead) {
//            // If the user has already liked the story, remove the like
//            readRepository.deleteByStoryHistoryIdAndUsername(storyId, username);
//            story.setLikesCount(story.getReadsCount() - 1);
//        } else {
//            // If the user hasn't liked the story, add a like
//            Reads like = new Reads();
//            like.setStory(story);
//            like.setUsername(username);
//            readRepository.save(like);
//            story.setLikesCount(story.getReadsCount() + 1);
//        }
//
//        // Prepare the response map
//        Map<String, Object> response = new HashMap<>();
//        response.put("hasRead", hasLiked); // New like status after toggling
//        response.put("ReadCount", story.getReadsCount());
//
//        return response;
//    }




}
