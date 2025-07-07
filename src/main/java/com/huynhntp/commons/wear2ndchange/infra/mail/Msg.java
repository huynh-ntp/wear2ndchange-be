package com.huynhntp.commons.wear2ndchange.infra.mail;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Msg {

    private MsgUser msgUser;

    private Map<String, Object> params = new HashMap<>();
}
