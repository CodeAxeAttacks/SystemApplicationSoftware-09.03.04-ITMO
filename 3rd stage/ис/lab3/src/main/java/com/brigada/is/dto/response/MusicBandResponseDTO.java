package com.brigada.is.dto.response;

import com.brigada.is.domain.MusicGenre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MusicBandResponseDTO {
    private Long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    private String name; //Поле не может быть null, Строка не может быть пустой

    private CoordinatesResponseDTO coordinates; //Поле не может быть null

    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    private MusicGenre genre; //Поле не может быть null

    private int numberOfParticipants; //Значение поля должно быть больше 0

    private Long singlesCount; //Поле не может быть null, Значение поля должно быть больше 0

    private String description; //Поле не может быть null

    private AlbumResponseDTO bestAlbum; //Поле может быть null

    private Long albumsCount; //Поле не может быть null, Значение поля должно быть больше 0

    private ZonedDateTime establishmentDate; //Поле не может быть null

    private StudioResponseDTO studio; //Поле может быть null

    private Long createdBy;
}
