package com.brigada.is.mapper;

import com.brigada.is.domain.ImportHistory;
import com.brigada.is.dto.response.ImportHistoryResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ImportHistoryMapper {
    ImportHistoryMapper INSTANCE = Mappers.getMapper(ImportHistoryMapper.class);

    @Mapping(target = "userId", source = "user.id")
    ImportHistoryResponseDTO toResponseDTO(ImportHistory e);
}
