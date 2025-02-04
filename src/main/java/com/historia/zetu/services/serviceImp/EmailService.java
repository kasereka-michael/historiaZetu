package com.historia.zetu.services.serviceImp;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Component
public class EmailService {

    public static final String EMAIL_TEMPLATE = "emails/otp";
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String TEXT_HTML_ENCODING = "text/html";
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;


    private MimeMessage getMimeMessage() {
        return mailSender.createMimeMessage();
    }

    public void confirmAdminAccountEmail(String name, String to, String otp) {
        try {

            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject("Confirm your email");
            helper.setFrom(fromEmail);
            helper.setTo(to);

            Context context = new Context();
            context.setVariables(Map.of("name", name, "otp",otp));
//            add html email body
            String text = templateEngine.process(EMAIL_TEMPLATE, context);
            MimeMultipart mimeMultipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, TEXT_HTML_ENCODING);
            mimeMultipart.addBodyPart(messageBodyPart);

//             TODO: i will work on adding the image or logo to the html being sent later
//            log.info("the file path in the email service "+ path);
//            add image to the email body
            BodyPart imageBodyPart = new MimeBodyPart();
            DataSource datasource = new FileDataSource("src/main/resources/static/system-logo.webp");
            imageBodyPart.setDataHandler(new DataHandler(datasource));
            imageBodyPart.setHeader("Content-ID", "image");
            mimeMultipart.addBodyPart(imageBodyPart);

//            adding content and file in to the message
            message.setContent(mimeMultipart);
            mailSender.send(message);
        } catch (Exception exception) {
            System.out.print("the error is here " + exception.getMessage());
            throw new RuntimeException("the error " + exception.getMessage());
        }
    }




//    public void sendToTechnicianEmailWhenAddedToMaintenanceGroup(String name, String to, Technicians technicians) {
//        try {
//
//            MimeMessage message = getMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
//            helper.setPriority(1);
//            helper.setSubject("ADDING TECHNICIAN INTO MAINTENANCE GROUP");
//            helper.setFrom(fromEmail);
//            helper.setTo(to);
//
//            Context context = new Context();
//            context.setVariables(Map.of("name", name, "maintenancegroupname", technicians.getMaintenanceGroup().getGroupName(), "maintenancegroupprogram", technicians.getMaintenanceGroup().getGroupProgram(), "maintenancegroupchef", technicians.getMaintenanceGroup().getMaintenanceGroupChef().getFirstName()));
////            add html email body
//            String text = templateEngine.process(TECHNICIAN_EMAIL_TEMPLATE, context);
//            MimeMultipart mimeMultipart = new MimeMultipart("related");
//            BodyPart messageBodyPart = new MimeBodyPart();
//            messageBodyPart.setContent(text, TEXT_HTML_ENCODING);
//            mimeMultipart.addBodyPart(messageBodyPart);
//
////             TODO: i will work on adding the image or logo to the html being sent later
////            log.info("the file path in the email service "+ path);
////            add image to the email body
//            BodyPart imageBodyPart = new MimeBodyPart();
//            DataSource datasource = new FileDataSource("src/main/resources/static/img/logo.png");
//            imageBodyPart.setDataHandler(new DataHandler(datasource));
//            imageBodyPart.setHeader("Content-ID", "image");
//            mimeMultipart.addBodyPart(imageBodyPart);
//
////            adding content and file in to the message
//            message.setContent(mimeMultipart);
//            mailSender.send(message);
//        } catch (Exception exception) {
//            System.out.print("the error is here " + exception.getMessage());
//            throw new RuntimeException("the error " + exception.getMessage());
//        }
//    }
//

}
