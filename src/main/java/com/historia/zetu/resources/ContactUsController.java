package com.historia.zetu.resources;

import com.historia.zetu.model.ContactUs;
import com.historia.zetu.services.serviceImp.ContactUsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/contactus")
public class ContactUsController {

    private final ContactUsServiceImpl contactUsService;

    
    @PostMapping
    public ResponseEntity<String> handleContactUs(@RequestBody ContactUs contactUs) {
        contactUsService.saveMessage(contactUs);
        return ResponseEntity.ok("Message sent successfully!");
    }
}
