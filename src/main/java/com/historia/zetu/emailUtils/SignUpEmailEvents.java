package com.historia.zetu.emailUtils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class SignUpEmailEvents extends ApplicationEvent {
    private String subject;
    private String otp;
    public SignUpEmailEvents(Object source, String subject, String otp) {
        super(source);
        this.subject = subject;
        this.otp = otp;
    }
}
