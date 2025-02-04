package com.historia.zetu.resources;

import com.historia.zetu.services.serviceImp.SubscriptionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final SubscriptionServiceImpl subscriptionService;

    /**
     * Check if the email is already subscribed
     */
    @PostMapping("/status")
    public ResponseEntity<Map<String, Object>> checkSubscriptionStatus(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        boolean isSubscribed = subscriptionService.isSubscribed(email);
        Map<String, Object> response = new HashMap<>();
        response.put("isSubscribed", isSubscribed);
        return ResponseEntity.ok(response);
    }

    /**
     * Handle subscription requests
     */
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> subscribe(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        Map<String, Object> response = new HashMap<>();
        try {
           boolean b = subscriptionService.subscribe(email);
           if(b){
               response.put("success", true);
               response.put("message", "Subscription successful!");
               return ResponseEntity.ok(response);
           }else{
               response.put("success", true);
               response.put("message", "You are already subscribed!");
               return ResponseEntity.ok(response);
           }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }


    }

    /**
     * Handle unsubscription requests
     */
    @PostMapping("/unsubscribe")
    public ResponseEntity<Map<String, Object>> unSubscribe(@RequestBody Map<String, String> request) {

        String email = request.get("email");

        Map<String, Object> response = new HashMap<>();
        try {
            boolean b = subscriptionService.unSubscribe(email);
            if (b) {
                response.put("success", true);
                response.put("message", "unSubscription successful!");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", true);
                response.put("message", "there is not way");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

}


