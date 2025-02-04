package com.historia.zetu.resources;

import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.historia.zetu.model.Comments;
import com.historia.zetu.entity.HttpResponse;
import com.historia.zetu.model.ReaderInformations;
import com.historia.zetu.model.Story;
import com.historia.zetu.services.serviceImp.CommentsServiceImpl;
import com.historia.zetu.services.serviceImp.HistoryServiceImpl;
import com.historia.zetu.services.serviceImp.ReaderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.SubnetUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import static org.springframework.http.ResponseEntity.created;



@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/histories")
@RestController
public class StoryController {

    private final HistoryServiceImpl historyService;
    private final RestTemplate restTemplate;
    private final ReaderServiceImpl readerService;
    private final CommentsServiceImpl comments;



    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createHistory(@RequestBody Story history) {
        Story result = historyService.saveHistory(history);

        if (result != null) {
            // Success Response
            return ResponseEntity.created(URI.create("")).body(
                    HttpResponse.builder()
                            .timesStamp(LocalDateTime.now().toString())
                            .data(Map.of("history", result))
                            .message("History created successfully.")
                            .status(HttpStatus.CREATED)
                            .statusCode(HttpStatus.CREATED.value())
                            .build()
            );
        }

        // Error Response
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                HttpResponse.builder()
                        .timesStamp(LocalDateTime.now().toString())
                        .message("Failed to create history.")
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build()
        );
    }



    @PostMapping("/reader-infos")
    public void recordReaderInfos(@RequestHeader(value = "User-Agent", defaultValue = "Unknown") String userAgent, ReaderInformations readerInformations, HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);

        String country = "Unknown";
        String city = "Unknown";

        log.info("User Agent: {}", userAgent);
        log.info("IP Address: {}", ipAddress);

        try {
            String url = "http://ipinfo.io/" + ipAddress + "/json";
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, String> ipInfo = response.getBody();

            if (ipInfo != null) {
                country = ipInfo.getOrDefault("country", "Unknown");
                city = ipInfo.getOrDefault("city", "Unknown");
            }
        } catch (
                RestClientException e) {
            log.error("Error fetching IP information: ", e);
        }

        log.info("Country: {}", country);
        log.info("City: {}", city);

        readerInformations.setNumber(getNextReaderNumber());
        readerInformations.setCountry(country);
        readerInformations.setCity(city);
        readerInformations.setIpAddress(ipAddress);
        readerInformations.setUserAgent(userAgent);

        try {
            readerService.saveReader(readerInformations);
        } catch (Exception e) {
            log.error("Error fetching story information: ", e);
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (isValidIpAddress(ipAddress)) {
            return ipAddress;
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    private int getNextReaderNumber() {
        // Implement logic to get the next reader number, e.g., from a database sequence
        return 1; // Placeholder implementation
    }


    @PostMapping("/submit-comment")
    public ResponseEntity<HttpResponse> postComment(
            @RequestParam("commentContent") String content,
            @RequestParam("commentAuthor") String author,
            @RequestParam("commenterEmail") String email,
            @RequestParam(value = "parentCommentId", required = false) Long parentCommentId) {

        Comments newComment = new Comments();
        newComment.setCommentContent(content);;
        newComment.setCommenterEmail(email);
        newComment.setCommentDate(LocalDateTime.now());

        log.info("Received commentContent: " + content);
        log.info("Received commentAuthor: " + author);
        log.info("Received commenterEmail: " + email);
        log.info("Received parentCommentId: " + parentCommentId);

        if (parentCommentId != null) {
            Comments parentComment = comments.getCommentById(parentCommentId);
            newComment.setParentComment(parentComment);
        }

        boolean result = comments.saveComment(newComment);
        if (result) {
            return created(URI.create("")).body(
                    HttpResponse.builder()
                            .timesStamp(LocalDateTime.now().toString())
                            .data(Map.of("historys", result))
                            .message("")
                            .status(HttpStatus.CREATED)
                            .statusCode(HttpStatus.CREATED.value())
                            .build()
            );
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    private boolean isValidIpAddress(String ipAddress) {
        if (ipAddress == null || ipAddress.trim().isEmpty()) {
            return false;
        }

        // Split for multiple IP addresses in X-Forwarded-For
        String[] ips = ipAddress.split(",");
        String ip = ips[0].trim();

        // Basic IP format validation using regex
        String ipv4Pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        String ipv6Pattern = "^([0-9a-fA-F]{1,4}:){7}([0-9a-fA-F]{1,4})$";

        if (!ip.matches(ipv4Pattern) && !ip.matches(ipv6Pattern)) {
            return false;
        }

        // Check known VPN and proxy IP ranges
        return !isVpnOrProxy(ip);
    }

    private boolean isVpnOrProxy(String ip) {
        // Common VPN providers IP ranges
        String[] vpnRanges = {
                "162.158.0.0/16",  // Cloudflare
                "103.21.244.0/22", // Cloudflare
                "104.16.0.0/12",   // Cloudflare
                "108.162.192.0/18",// Cloudflare
                "131.0.72.0/22",   // ProtonVPN
                "185.159.157.0/24",// NordVPN
                "194.242.110.0/23",// ExpressVPN
                "37.120.137.0/24", // PureVPN
        };

        try {
            InetAddress ipAddress = InetAddress.getByName(ip);

            // Check if IP is in known VPN ranges
            for (String range : vpnRanges) {
                String[] parts = range.split("/");
                SubnetUtils subnet = new SubnetUtils(parts[0], String.valueOf(parts[1]));
                if (subnet.getInfo().isInRange(ip)) {
                    return true;
                }
            }

            // Additional checks for common VPN ports
            Socket socket = new Socket(ip, 1194); // OpenVPN default port
            socket.close();
            return true;
        } catch (Exception e) {
            // If connection fails, likely not a VPN
            return false;
        }
    }



    @GetMapping
    public ResponseEntity<Page<Story>> getStories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String searchTerm) {

        Page<Story> stories = historyService.getStories(page - 1, size, searchTerm);  // -1 because Spring Data Page starts at 0
       // Log each story in the page
        return ResponseEntity.ok(stories);
    }

    @PostMapping("/get-email")
    public ResponseEntity<Map<String, Object>> getUserEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        log.info("Received email: {} ", email);
        Map<String, Object> response = new HashMap<>();
        try {
            long userId = historyService.findUserEmail(email);

            log.info("User ID found is : {}", userId);
            response.put("id", userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("id", null);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


    @PostMapping("/like/{storyId}")
    public ResponseEntity<?> likeStory(@PathVariable Long storyId, @RequestParam String username) {
        try {
            // Validate inputs (optional but recommended)
            if (storyId == null || username == null || username.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid story ID or username.");
            }

            // Call the service to toggle like/unlike and get the updated status and likes count
            Map<String, Object> likeResponse = historyService.toggleLike(storyId, username);

            // Return the response as a HashMap
            return ResponseEntity.ok(likeResponse);
        } catch (IllegalArgumentException e) {
            // Handle specific exceptions (e.g., story not found, invalid input)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            // Log the error for debugging purposes
            e.printStackTrace();

            // Return a generic error response for unexpected issues
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your like/unlike request.");
        }
    }



    @PostMapping("/check-like/{storyId}")
    public ResponseEntity<?> checklikeStory(@PathVariable Long storyId, @RequestParam String username) {
        try {

            // Validate inputs (optional but recommended)
            if (storyId == null || username == null || username.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid story ID or username.");
            }

            // Call the service to toggle like/unlike and get the updated likes count
            boolean updatedLikes = historyService.checkUserNameHistoryLike(storyId, username);

            // Return the updated likes count as a response
            return ResponseEntity.ok(updatedLikes);
        } catch (IllegalArgumentException e) {
            // Handle specific exceptions (e.g., story not found, invalid input)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            // Log the error for debugging purposes
            e.printStackTrace();

            // Return a generic error response for unexpected issues
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your like/unlike request.");
        }
    }


}
