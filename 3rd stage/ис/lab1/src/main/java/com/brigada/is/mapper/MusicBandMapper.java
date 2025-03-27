package com.brigada.is.mapper;

import com.brigada.is.domain.MusicBand;
import com.brigada.is.dto.request.MusicBandRequestDTO;
import com.brigada.is.dto.response.MusicBandResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {StudioMapper.class, AlbumMapper.class, CoordinatesMapper.class})
public interface MusicBandMapper {
    MusicBandMapper INSTANCE = Mappers.getMapper(MusicBandMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    MusicBand toEntity(MusicBandRequestDTO requestDTO);

    @Mapping(target = "createdBy", source = "createdBy.id")
    MusicBandResponseDTO toResponseDTO(MusicBand musicBand);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "studio", ignore = true)
    void updateMusicBand(MusicBandRequestDTO requestDTO, @MappingTarget MusicBand e);
}
