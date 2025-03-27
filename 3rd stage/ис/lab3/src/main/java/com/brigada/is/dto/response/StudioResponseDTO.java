package com.brigada.is.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudioResponseDTO {
    private Long id;

    private String name; //Поле может быть null

    private Long createdBy;
}
