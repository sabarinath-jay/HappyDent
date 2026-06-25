package com.happydent.service;

import com.happydent.model.Appointment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${app.notification.email.enabled:false}") private boolean emailEnabled;
    @Value("${app.notification.email.to:}") private String emailTo;
    @Value("${spring.mail.username:}") private String emailFrom;

    @Value("${app.notification.whatsapp.enabled:false}") private boolean waEnabled;
    @Value("${app.notification.whatsapp.phone:}") private String waPhone;
    @Value("${app.notification.whatsapp.apikey:}") private String waApiKey;

    @Async
    public void notifyNewAppointment(Appointment appt) {
        if (emailEnabled) sendEmail(appt);
        if (waEnabled) sendWhatsApp(appt);
    }

    public boolean sendTestEmail() {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(emailFrom);
            msg.setTo(emailTo.isEmpty() ? emailFrom : emailTo);
            msg.setSubject("Test – Happy Dent Email Notifications");
            msg.setText("Gmail SMTP is working correctly.\n\nYou will receive this email whenever a patient books an appointment at Happy Dent Dental Care.");
            mailSender.send(msg);
            return true;
        } catch (Exception e) {
            log.warn("Test email failed: {}", e.getMessage());
            return false;
        }
    }

    private void sendEmail(Appointment appt) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(emailFrom);
            msg.setTo(emailTo);
            msg.setSubject("New Appointment – " + appt.getName());
            msg.setText(buildEmailText(appt));
            mailSender.send(msg);
        } catch (Exception e) {
            log.warn("Email notification failed: {}", e.getMessage());
        }
    }

    private void sendWhatsApp(Appointment appt) {
        try {
            String text = buildWhatsAppText(appt);
            String url = "https://api.callmebot.com/whatsapp.php?phone=" + waPhone
                    + "&text=" + java.net.URLEncoder.encode(text, "UTF-8")
                    + "&apikey=" + waApiKey;
            new RestTemplate().getForObject(URI.create(url), String.class);
        } catch (Exception e) {
            log.warn("WhatsApp notification failed: {}", e.getMessage());
        }
    }

    private String buildEmailText(Appointment a) {
        return "New Appointment Request – Happy Dent Dental Care\n\n"
                + "Patient : " + a.getName() + "\n"
                + "Phone   : " + a.getPhone() + "\n"
                + "Email   : " + (a.getEmail() != null ? a.getEmail() : "—") + "\n"
                + "Service : " + (a.getService() != null ? a.getService() : "—") + "\n"
                + "Date    : " + (a.getPreferredDate() != null
                        ? a.getPreferredDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")) : "Flexible") + "\n"
                + "Message : " + (a.getMessage() != null ? a.getMessage() : "—") + "\n\n"
                + "Login to the admin portal to confirm or reschedule.";
    }

    private String buildWhatsAppText(Appointment a) {
        return "🦷 *New Appointment - Happy Dent*\n\n"
                + "👤 *Patient:* " + a.getName() + "\n"
                + "📞 *Phone:* " + a.getPhone() + "\n"
                + "🏥 *Service:* " + (a.getService() != null ? a.getService() : "—") + "\n"
                + "📅 *Date:* " + (a.getPreferredDate() != null
                        ? a.getPreferredDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")) : "Flexible") + "\n"
                + "💬 *Message:* " + (a.getMessage() != null ? a.getMessage() : "None");
    }
}
