//package com.ims.fullstack.service;
//
//import com.ims.fullstack.model.EmailLog;
//import com.ims.fullstack.model.InterviewRound;
//import com.ims.fullstack.model.enums.EmailStatus;
//import com.ims.fullstack.repository.EmailLogRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//@RequiredArgsConstructor
//public class MailService {
//
//    private final JavaMailSender mailSender;
//    private final EmailLogRepository emailLogRepository;
//
//    // -----------------------------------------
//    // ✨ OLD STYLE CALL (NO InterviewRound)
//    // -----------------------------------------
//    public void sendInterviewInvite(String to, String subject, String body) {
//        sendInterviewInvite(to, subject, body, null);
//    }
//
//    // -----------------------------------------
//    // ✨ NEW VERSION (Supports InterviewRound)
//    // -----------------------------------------
//    public void sendInterviewInvite(String to, String subject, String body, InterviewRound round) {
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(body);
//
//        try {
//            mailSender.send(message);
//
//            EmailLog log = EmailLog.builder()
//                    .recipient(to)
//                    .subject(subject)
//                    .bodyPreview(body.substring(0, Math.min(body.length(), 200)))
//                    .status(EmailStatus.SENT)
//                    .sentAt(LocalDateTime.now())
//                    .interviewRound(round)
//                    .build();
//
//            emailLogRepository.save(log);
//
//        } catch (Exception ex) {
//            EmailLog log = EmailLog.builder()
//                    .recipient(to)
//                    .subject(subject)
//                    .bodyPreview(body.substring(0, Math.min(body.length(), 200)))
//                    .status(EmailStatus.FAILED)
//                    .sentAt(LocalDateTime.now())
//                    .providerMessageId(ex.getMessage())
//                    .interviewRound(round)
//                    .build();
//
//            emailLogRepository.save(log);
//
//            throw ex;
//        }
//    }
//}

package com.ims.fullstack.service;

import com.ims.fullstack.model.EmailLog;
import com.ims.fullstack.model.InterviewRound;
import com.ims.fullstack.model.enums.EmailStatus;
import com.ims.fullstack.repository.EmailLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;

    public void sendInterviewInvite(String to, String subject, String body) {
        sendInterviewInvite(to, subject, body, null);
    }

    public void sendInterviewInvite(String to, String subject, String body, InterviewRound round) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        try {
            mailSender.send(message);
            EmailLog log = EmailLog.builder()
                    .recipient(to)
                    .subject(subject)
                    .bodyPreview(body.substring(0, Math.min(body.length(), 200)))
                    .status(EmailStatus.SENT)
                    .sentAt(LocalDateTime.now())
                    .interviewRound(round)
                    .build();
            emailLogRepository.save(log);
        } catch (Exception ex) {
            EmailLog log = EmailLog.builder()
                    .recipient(to)
                    .subject(subject)
                    .bodyPreview(body.substring(0, Math.min(body.length(), 200)))
                    .status(EmailStatus.FAILED)
                    .sentAt(LocalDateTime.now())
                    .providerMessageId(ex.getMessage())
                    .interviewRound(round)
                    .build();
            emailLogRepository.save(log);
            throw ex;
        }
    }
}