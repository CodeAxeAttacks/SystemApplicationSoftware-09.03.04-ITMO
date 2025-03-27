package com.brigada.is.domain;

import com.brigada.is.security.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "music_band")
public class MusicBand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Check(constraints = "id > 0")
    private Long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @NotBlank
    @Check(constraints = "name <> ''")
    @Column(nullable = false)
    private String name; //Поле не может быть null, Строка не может быть пустой

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "coordinates_id")
    private Coordinates coordinates; //Поле не может быть null

    @Column(nullable = false)
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MusicGenre genre; //Поле не может быть null

    @Check(constraints = "number_of_participants > 0")
    private int numberOfParticipants; //Значение поля должно быть больше 0

    @Check(constraints = "singles_count > 0")
    @Column(nullable = false)
    private Long singlesCount; //Поле не может быть null, Значение поля должно быть больше 0

    @Column(nullable = false)
    private String description; //Поле не может быть null

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = true, name = "best_album_id")
    private Album bestAlbum; //Поле может быть null

    @Column(nullable = false)
    @Check(constraints = "albums_count > 0")
    private Long albumsCount; //Поле не может быть null, Значение поля должно быть больше 0

    @Column(nullable = false)
    private ZonedDateTime establishmentDate; //Поле не может быть null

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(nullable = true, name = "studio_id",
            foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, foreignKeyDefinition = "FOREIGN KEY (studio_id) REFERENCES studio(id) ON DELETE SET NULL"))
    private Studio studio; //Поле может быть null

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
    }
}
