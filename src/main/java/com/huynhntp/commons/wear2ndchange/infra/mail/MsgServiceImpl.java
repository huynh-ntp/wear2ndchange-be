package com.huynhntp.commons.wear2ndchange.infra.mail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
class MsgServiceImpl implements MsgService {

    private final JavaMailSender mailSender;

    @Override
    public void send(Msg msg) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(msg.getMsgUser().getEmail());
        message.setTo(msg.getParams().get("emailTo").toString());
        message.setSubject("Reset Password");
        message.setText(msg.getParams().get("link").toString());

        mailSender.send(message);
    }
}
