package com.brigada.is.mapper;

import com.brigada.is.dto.response.AdminApplicationResponseDTO;
import com.brigada.is.security.entity.AdminApplication;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminApplicationMapper {
    AdminApplicationMapper INSTANCE = Mappers.getMapper(AdminApplicationMapper.class);

    AdminApplicationResponseDTO toAdminApplicationResponseDTO(AdminApplication application);
}
