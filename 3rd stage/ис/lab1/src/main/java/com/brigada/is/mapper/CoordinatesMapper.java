package com.brigada.is.mapper;

import com.brigada.is.domain.Coordinates;
import com.brigada.is.dto.request.CoordinatesRequestDTO;
import com.brigada.is.dto.response.CoordinatesResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CoordinatesMapper {
    CoordinatesMapper INSTANCE = Mappers.getMapper(CoordinatesMapper.class);

    @Mapping(target = "id", ignore = true)
    Coordinates toEntity(CoordinatesRequestDTO requestDTO);

    CoordinatesResponseDTO toResponseDTO(Coordinates coordinates);

    @Mapping(target = "id", ignore = true)
    void updateCoordinates(CoordinatesRequestDTO requestDTO, @MappingTarget Coordinates e);
}
