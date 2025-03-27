package com.brigada.is.mapper;

import com.brigada.is.domain.MusicBand;
import com.brigada.is.domain.Nomination;
import com.brigada.is.dto.response.NominationResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NominationMapper {
    NominationMapper INSTANCE = Mappers.getMapper(NominationMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nominationTime", ignore = true)
    Nomination toEntity(MusicBand e);

    NominationResponseDTO toResponseDTO(Nomination nomination);
}
