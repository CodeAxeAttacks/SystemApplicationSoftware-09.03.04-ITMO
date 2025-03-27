package com.brigada.is.mapper;

import com.brigada.is.domain.Album;
import com.brigada.is.dto.request.AlbumRequestDTO;
import com.brigada.is.dto.response.AlbumResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AlbumMapper {
    AlbumMapper INSTANCE = Mappers.getMapper(AlbumMapper.class);

    @Mapping(target = "id", ignore = true)
    Album toEntity(AlbumRequestDTO requestDTO);

    AlbumResponseDTO toResponseDTO(Album album);

    @Mapping(target = "id", ignore = true)
    void updateAlbum(AlbumRequestDTO requestDTO, @MappingTarget Album e);
}
