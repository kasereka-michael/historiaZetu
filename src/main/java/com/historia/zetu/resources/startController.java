package com.historia.zetu.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.historia.zetu.model.Comments;
import com.historia.zetu.model.Story;
import com.historia.zetu.services.serviceImp.CommentsServiceImpl;
import com.historia.zetu.services.serviceImp.HistoryServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class startController {

    private final HistoryServiceImpl historyService;
    private final CommentsServiceImpl comments;

    @GetMapping("/")
    public String openingWebSite(Model model) {
        return "home-page-webapp";
    }

    @GetMapping("/about-us")
    public String openingAboutWebSite(Model model) {
        return "about";
    }

    @GetMapping("/contact-us")
    public String openingContactWebSite(Model model) {
        return "contact";
    }

    @GetMapping("/blog")
    public String openingBlogWebSite(Model model) {
        return "blog";
    }

    @GetMapping("/view-story")
    public String openingViewStoryWebSite(Model model) {
        return "view-story";
    }

    @GetMapping("/view-single-story")
    public String openingViewSingleStoryWebSite(Model model) {
        return "single-history";
    }

    @GetMapping("/authentication")
    public String authenticationAdmin(Model model) {
        return "login";
    }

    @GetMapping("/sign-in")
    public String registerAdminAccount(Model model) {
        return "register";
    }


    @GetMapping("/api/history/{id}")
    public String getHistoryById(@PathVariable("id") Long id, Model model) {
        Story history = historyService.getHistoryById(id);
        if(history != null) {
            model.addAttribute("mainHistory", history);
            List<Comments> topLevelComments = comments.getAllTopLevelComments();
            model.addAttribute("comments", topLevelComments);
            return "single-post";
        } else {
            return "single-post";
        }
    }



    @GetMapping("/api/dashboard/admin")
    public String openDashboard(Model model){


        // Create an instance of Calendar
        Calendar calendar = Calendar.getInstance();

        // Get the day number (day of the month)
        int dayNumber = calendar.get(Calendar.DAY_OF_MONTH);

        // Get the full day name (e.g., Monday, Tuesday, etc.)
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
        String dayLetter = dayFormat.format(calendar.getTime());

        // Get the month and year
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMM yyyy");
        String monthYear = monthYearFormat.format(calendar.getTime());



        model.addAttribute("dayNumber",dayNumber);
        model.addAttribute("dayLetter",dayLetter);
        model.addAttribute("monthYear",monthYear);
        return "frame/dashboard/Dashboard";
    }




    @GetMapping("/api/histories/view-story/{storyId}")
    public String getStoriesWithRelated(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @PathVariable("storyId") long storyId, Model model) throws JsonProcessingException {

        Page<Story> stories = historyService.getStoriesRelated(page - 1, size, storyId);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Convert stories to JSON and add to the model
        String jsonStories = objectMapper.writeValueAsString(stories.getContent());
        model.addAttribute("stories", jsonStories);

        log.info("Page details - Total Pages: {}, Total Elements: {}, Current Page: {}",
                stories.getTotalPages(), stories.getTotalElements(), stories.getNumber());

        return "view-story"; // Return the Thymeleaf view name
    }

//    /api/histories/view-entire-story/**"

    @GetMapping("/api/histories/view-entire-story/{storyId}")
    public String getToReadStoriesWithRelated(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @PathVariable("storyId") long storyId, Model model) throws JsonProcessingException {

        Story story = historyService.getHistoryById(storyId);
        Page<Story> stories = historyService.getStoriesRelated(page - 1, size, storyId);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Convert stories to JSON and add to the model
        String jsonStories = objectMapper.writeValueAsString(stories.getContent());
        model.addAttribute("stories", jsonStories);
        model.addAttribute("story", story);
        return "single-history"; // Return the Thymeleaf view name
    }

    @GetMapping("/api/history/create")
    public String create(Model model) {
        Map<String, List<Story>> groupedHistories = historyService.getStoryGroupedByMonth();
        model.addAttribute("groupedHistories", groupedHistories);
        return "frame/dashboard/history";
    }

    @GetMapping(value = "/history/{id}", produces = "application/json")
    @ResponseBody
    public Story getHistoryById(@PathVariable Long id) {
        return historyService.getHistoryById(id);
    }
}
