package com.brigada.is.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "album")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Check(constraints = "name <> ''")
    @Column(nullable = false)
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Column(nullable = false)
    @Check(constraints = "tracks > 0")
    private Integer tracks; //Поле не может быть null, Значение поля должно быть больше 0

    @Check(constraints = "length > 0")
    private int length; //Значение поля должно быть больше 0

    @Check(constraints = "sales > 0")
    private long sales; //Значение поля должно быть больше 0
}