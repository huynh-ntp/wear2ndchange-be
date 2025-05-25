package com.huynhntp.commons.wear2ndchange.mapper;

import com.huynhntp.commons.wear2ndchange.model.dto.AccountDTO;
import com.huynhntp.commons.wear2ndchange.model.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO toDto(Account account);

}