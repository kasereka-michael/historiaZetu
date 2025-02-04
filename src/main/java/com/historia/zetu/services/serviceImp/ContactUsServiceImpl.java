package com.historia.zetu.services.serviceImp;

import com.historia.zetu.Repository.ContactUsRepository;
import com.historia.zetu.model.ContactUs;
import com.historia.zetu.services.ContactUsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactUsServiceImpl implements ContactUsService {

    private final ContactUsRepository contactUsRepository;

    @Override
    public void saveMessage(ContactUs contactUs) {
        contactUsRepository.save(contactUs);
    }
}
