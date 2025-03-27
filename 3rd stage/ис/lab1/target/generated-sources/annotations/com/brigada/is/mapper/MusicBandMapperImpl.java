package com.brigada.is.mapper;

import com.brigada.is.domain.Album;
import com.brigada.is.domain.Coordinates;
import com.brigada.is.domain.MusicBand;
import com.brigada.is.dto.request.MusicBandRequestDTO;
import com.brigada.is.dto.response.MusicBandResponseDTO;
import com.brigada.is.security.entity.User;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-09T21:36:24+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20 (Oracle Corporation)"
)
public class MusicBandMapperImpl implements MusicBandMapper {

    private final StudioMapper studioMapper = StudioMapper.INSTANCE;
    private final AlbumMapper albumMapper = AlbumMapper.INSTANCE;
    private final CoordinatesMapper coordinatesMapper = CoordinatesMapper.INSTANCE;

    @Override
    public MusicBand toEntity(MusicBandRequestDTO requestDTO) {
        if ( requestDTO == null ) {
            return null;
        }

        MusicBand musicBand = new MusicBand();

        musicBand.setName( requestDTO.getName() );
        musicBand.setCoordinates( coordinatesMapper.toEntity( requestDTO.getCoordinates() ) );
        musicBand.setGenre( requestDTO.getGenre() );
        musicBand.setNumberOfParticipants( requestDTO.getNumberOfParticipants() );
        musicBand.setSinglesCount( requestDTO.getSinglesCount() );
        musicBand.setDescription( requestDTO.getDescription() );
        musicBand.setBestAlbum( albumMapper.toEntity( requestDTO.getBestAlbum() ) );
        musicBand.setAlbumsCount( requestDTO.getAlbumsCount() );
        musicBand.setEstablishmentDate( requestDTO.getEstablishmentDate() );
        musicBand.setStudio( studioMapper.toEntity( requestDTO.getStudio() ) );

        return musicBand;
    }

    @Override
    public MusicBandResponseDTO toResponseDTO(MusicBand musicBand) {
        if ( musicBand == null ) {
            return null;
        }

        MusicBandResponseDTO musicBandResponseDTO = new MusicBandResponseDTO();

        musicBandResponseDTO.setCreatedBy( musicBandCreatedById( musicBand ) );
        musicBandResponseDTO.setId( musicBand.getId() );
        musicBandResponseDTO.setName( musicBand.getName() );
        musicBandResponseDTO.setCoordinates( coordinatesMapper.toResponseDTO( musicBand.getCoordinates() ) );
        musicBandResponseDTO.setCreationDate( musicBand.getCreationDate() );
        musicBandResponseDTO.setGenre( musicBand.getGenre() );
        musicBandResponseDTO.setNumberOfParticipants( musicBand.getNumberOfParticipants() );
        musicBandResponseDTO.setSinglesCount( musicBand.getSinglesCount() );
        musicBandResponseDTO.setDescription( musicBand.getDescription() );
        musicBandResponseDTO.setBestAlbum( albumMapper.toResponseDTO( musicBand.getBestAlbum() ) );
        musicBandResponseDTO.setAlbumsCount( musicBand.getAlbumsCount() );
        musicBandResponseDTO.setEstablishmentDate( musicBand.getEstablishmentDate() );
        musicBandResponseDTO.setStudio( studioMapper.toResponseDTO( musicBand.getStudio() ) );

        return musicBandResponseDTO;
    }

    @Override
    public void updateMusicBand(MusicBandRequestDTO requestDTO, MusicBand e) {
        if ( requestDTO == null ) {
            return;
        }

        e.setName( requestDTO.getName() );
        if ( requestDTO.getCoordinates() != null ) {
            if ( e.getCoordinates() == null ) {
                e.setCoordinates( new Coordinates() );
            }
            coordinatesMapper.updateCoordinates( requestDTO.getCoordinates(), e.getCoordinates() );
        }
        else {
            e.setCoordinates( null );
        }
        e.setGenre( requestDTO.getGenre() );
        e.setNumberOfParticipants( requestDTO.getNumberOfParticipants() );
        e.setSinglesCount( requestDTO.getSinglesCount() );
        e.setDescription( requestDTO.getDescription() );
        if ( requestDTO.getBestAlbum() != null ) {
            if ( e.getBestAlbum() == null ) {
                e.setBestAlbum( new Album() );
            }
            albumMapper.updateAlbum( requestDTO.getBestAlbum(), e.getBestAlbum() );
        }
        else {
            e.setBestAlbum( null );
        }
        e.setAlbumsCount( requestDTO.getAlbumsCount() );
        e.setEstablishmentDate( requestDTO.getEstablishmentDate() );
    }

    private Long musicBandCreatedById(MusicBand musicBand) {
        if ( musicBand == null ) {
            return null;
        }
        User createdBy = musicBand.getCreatedBy();
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
