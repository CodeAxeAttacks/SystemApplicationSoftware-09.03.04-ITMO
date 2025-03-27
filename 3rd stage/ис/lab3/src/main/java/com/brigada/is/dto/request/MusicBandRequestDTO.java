package com.brigada.is.dto.request;

import com.brigada.is.domain.MusicGenre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MusicBandRequestDTO {
    private String name; //Поле не может быть null, Строка не может быть пустой

    private CoordinatesRequestDTO coordinates; //Поле не может быть null

    private MusicGenre genre; //Поле не может быть null

    private int numberOfParticipants; //Значение поля должно быть больше 0

    private Long singlesCount; //Поле не может быть null, Значение поля должно быть больше 0

    private String description; //Поле не может быть null

    private AlbumRequestDTO bestAlbum; //Поле может быть null

    private Long albumsCount; //Поле не может быть null, Значение поля должно быть больше 0

    private ZonedDateTime establishmentDate; //Поле не может быть null

    private StudioRequestDTO studio; //Поле может быть null

}
