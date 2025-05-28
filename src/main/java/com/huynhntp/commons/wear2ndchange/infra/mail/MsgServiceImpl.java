package com.huynhntp.commons.wear2ndchange.infra.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
class MsgServiceImpl implements MsgService {

    private final JavaMailSender mailSender;

    @Override
    public void send(Msg msg) {
        String template = String.valueOf(msg.getParams().get("template"));

        switch (template) {
            case "reset":
                resetPasswordTemplate(mailSender, msg);
                break;
            case "sendNewPassword":
                sendNewPasswordTemplate(mailSender, msg);
                break;
            default:
                throw new IllegalArgumentException("Unknown email template: " + template);
        }
    }

    public void resetPasswordTemplate(JavaMailSender mailSender, Msg msg) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(msg.getMsgUser().getEmail());
            helper.setTo(msg.getParams().get("emailTo").toString());
            helper.setSubject("Reset Password");

            String resetLink = msg.getParams().get("link").toString();
            String htmlContent = """
            <html>
            <body>
                <h2>Reset Your Password</h2>
                <p>Click the link below to reset your password:</p>
                <a href="%s">Reset Password</a>
                <br/><br/>
                <p>If you didn’t request this, just ignore this email.</p>
            </body>
            </html>
        """.formatted(resetLink);

            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send reset password email", e);
        }
    }

    public void sendNewPasswordTemplate(JavaMailSender mailSender, Msg msg) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(msg.getMsgUser().getEmail());
            helper.setTo(msg.getParams().get("emailTo").toString());
            helper.setSubject("New Password");

            String password = msg.getParams().get("password").toString();

            String html = """
            <html>
              <body>
                <h2>Your New Password</h2>
                <p>Please use the following password to login:</p>
                <p style="font-weight: bold; font-size: 18px; color: #2b2b2b;">%s</p>
                <p>We recommend changing your password after logging in.</p>
              </body>
            </html>
        """.formatted(password);

            helper.setText(html, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send new password email", e);
        }
    }

}
