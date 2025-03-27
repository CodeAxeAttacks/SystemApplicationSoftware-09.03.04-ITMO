package com.brigada.is.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CoordinatesResponseDTO {
    private Long id;

    private double x;

    private Long y; //Поле не может быть null
}
