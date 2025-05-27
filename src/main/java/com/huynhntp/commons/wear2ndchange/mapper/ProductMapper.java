package com.huynhntp.commons.wear2ndchange.mapper;

import com.huynhntp.commons.wear2ndchange.model.dto.*;
import com.huynhntp.commons.wear2ndchange.model.entity.*;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toDto(Product product);

    Product toEntity(ProductForm productForm);

}