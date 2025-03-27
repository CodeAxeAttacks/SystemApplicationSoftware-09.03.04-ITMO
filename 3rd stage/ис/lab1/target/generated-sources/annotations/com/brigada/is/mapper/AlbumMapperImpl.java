package com.brigada.is.mapper;

import com.brigada.is.domain.Album;
import com.brigada.is.dto.request.AlbumRequestDTO;
import com.brigada.is.dto.response.AlbumResponseDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-09T21:36:23+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20 (Oracle Corporation)"
)
public class AlbumMapperImpl implements AlbumMapper {

    @Override
    public Album toEntity(AlbumRequestDTO requestDTO) {
        if ( requestDTO == null ) {
            return null;
        }

        Album album = new Album();

        album.setName( requestDTO.getName() );
        album.setTracks( requestDTO.getTracks() );
        album.setLength( requestDTO.getLength() );
        album.setSales( requestDTO.getSales() );

        return album;
    }

    @Override
    public AlbumResponseDTO toResponseDTO(Album album) {
        if ( album == null ) {
            return null;
        }

        AlbumResponseDTO albumResponseDTO = new AlbumResponseDTO();

        albumResponseDTO.setId( album.getId() );
        albumResponseDTO.setName( album.getName() );
        albumResponseDTO.setTracks( album.getTracks() );
        albumResponseDTO.setLength( album.getLength() );
        albumResponseDTO.setSales( album.getSales() );

        return albumResponseDTO;
    }

    @Override
    public void updateAlbum(AlbumRequestDTO requestDTO, Album e) {
        if ( requestDTO == null ) {
            return;
        }

        e.setName( requestDTO.getName() );
        e.setTracks( requestDTO.getTracks() );
        e.setLength( requestDTO.getLength() );
        e.setSales( requestDTO.getSales() );
    }
}
