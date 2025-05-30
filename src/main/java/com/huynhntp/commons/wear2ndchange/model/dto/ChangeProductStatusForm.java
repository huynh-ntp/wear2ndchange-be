package com.huynhntp.commons.wear2ndchange.model.dto;

import com.huynhntp.commons.wear2ndchange.enums.ProductStatusEnum;
import lombok.Data;

@Data
public class ChangeProductStatusForm {
    private ProductStatusEnum status;
}
