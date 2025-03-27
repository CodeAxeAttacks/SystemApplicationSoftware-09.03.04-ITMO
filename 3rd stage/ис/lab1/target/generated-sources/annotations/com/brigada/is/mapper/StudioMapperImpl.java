package com.brigada.is.mapper;

import com.brigada.is.domain.Studio;
import com.brigada.is.dto.request.StudioRequestDTO;
import com.brigada.is.dto.response.StudioResponseDTO;
import com.brigada.is.security.entity.User;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-09T21:36:23+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20 (Oracle Corporation)"
)
public class StudioMapperImpl implements StudioMapper {

    @Override
    public Studio toEntity(StudioRequestDTO requestDTO) {
        if ( requestDTO == null ) {
            return null;
        }

        Studio studio = new Studio();

        studio.setName( requestDTO.getName() );

        return studio;
    }

    @Override
    public StudioResponseDTO toResponseDTO(Studio studio) {
        if ( studio == null ) {
            return null;
        }

        StudioResponseDTO studioResponseDTO = new StudioResponseDTO();

        studioResponseDTO.setCreatedBy( studioCreatedById( studio ) );
        studioResponseDTO.setId( studio.getId() );
        studioResponseDTO.setName( studio.getName() );

        return studioResponseDTO;
    }

    @Override
    public void updateStudio(StudioRequestDTO requestDTO, Studio e) {
        if ( requestDTO == null ) {
            return;
        }

        e.setName( requestDTO.getName() );
    }

    private Long studioCreatedById(Studio studio) {
        if ( studio == null ) {
            return null;
        }
        User createdBy = studio.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        Long id = createdBy.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
