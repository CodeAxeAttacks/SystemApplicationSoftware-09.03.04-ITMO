package com.brigada.is.mapper;

import com.brigada.is.domain.Studio;
import com.brigada.is.dto.request.StudioRequestDTO;
import com.brigada.is.dto.response.StudioResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudioMapper {
    StudioMapper INSTANCE = Mappers.getMapper(StudioMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Studio toEntity(StudioRequestDTO requestDTO);

    @Mapping(target = "createdBy", source = "createdBy.id")
    StudioResponseDTO toResponseDTO(Studio studio);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateStudio(StudioRequestDTO requestDTO, @MappingTarget Studio e);
}
