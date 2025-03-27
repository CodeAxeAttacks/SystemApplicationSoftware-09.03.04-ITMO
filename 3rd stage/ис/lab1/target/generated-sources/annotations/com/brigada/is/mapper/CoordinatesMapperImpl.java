package com.brigada.is.mapper;

import com.brigada.is.domain.Coordinates;
import com.brigada.is.dto.request.CoordinatesRequestDTO;
import com.brigada.is.dto.response.CoordinatesResponseDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-09T21:36:23+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20 (Oracle Corporation)"
)
public class CoordinatesMapperImpl implements CoordinatesMapper {

    @Override
    public Coordinates toEntity(CoordinatesRequestDTO requestDTO) {
        if ( requestDTO == null ) {
            return null;
        }

        Coordinates coordinates = new Coordinates();

        coordinates.setX( requestDTO.getX() );
        coordinates.setY( requestDTO.getY() );

        return coordinates;
    }

    @Override
    public CoordinatesResponseDTO toResponseDTO(Coordinates coordinates) {
        if ( coordinates == null ) {
            return null;
        }

        CoordinatesResponseDTO coordinatesResponseDTO = new CoordinatesResponseDTO();

        coordinatesResponseDTO.setId( coordinates.getId() );
        coordinatesResponseDTO.setX( coordinates.getX() );
        coordinatesResponseDTO.setY( coordinates.getY() );

        return coordinatesResponseDTO;
    }

    @Override
    public void updateCoordinates(CoordinatesRequestDTO requestDTO, Coordinates e) {
        if ( requestDTO == null ) {
            return;
        }

        e.setX( requestDTO.getX() );
        e.setY( requestDTO.getY() );
    }
}
