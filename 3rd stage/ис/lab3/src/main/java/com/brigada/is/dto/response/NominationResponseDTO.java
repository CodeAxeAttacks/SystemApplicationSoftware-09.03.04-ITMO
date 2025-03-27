package com.brigada.is.dto.response;

import com.brigada.is.domain.MusicGenre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NominationResponseDTO {
    private Long id;

    private MusicGenre genre;

    private LocalDateTime nominationTime;
}
