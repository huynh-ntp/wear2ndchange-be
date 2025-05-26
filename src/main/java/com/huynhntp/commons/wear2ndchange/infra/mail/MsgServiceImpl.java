package com.huynhntp.commons.wear2ndchange.infra.mail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
class MsgServiceImpl implements MsgService {

    @Override
    public void send(Msg msg) {
        //TODO: handle send sms
    }
}
