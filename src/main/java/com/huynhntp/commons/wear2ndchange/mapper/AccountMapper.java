package com.huynhntp.commons.wear2ndchange.mapper;

import com.huynhntp.commons.wear2ndchange.model.dto.*;
import com.huynhntp.commons.wear2ndchange.model.entity.Account;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO toDto(Account account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccountFromForm(UpdateAccountForm form, @MappingTarget Account account);
}