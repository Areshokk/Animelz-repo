package com.areshok.animelz.model;

import lombok.*;

import jakarta.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "title", "type", "episodes", "studio"})
@Table(name = "anime")
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Назва аніме не може бути пустою")
    private String title;

    @Column(name = "alternative_name")
    private String alternativeName;

    @NotBlank(message = "Тип аніме не може бути пустим")
    private String type;

    @NotBlank(message = "Кількість епізодів не може бути пустим полем")
    private String episodes;

    @NotBlank(message = "Поле статус не може бути пустим")
    private String status;

    @ManyToMany
    @JoinTable(
            name = "animegenre",
            joinColumns = @JoinColumn(name = "anime_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @NotBlank(message = "Поле першоджерело не може бути пустим")
    private String source;

    @NotBlank(message = "Поле сезон не може бути пустим")
    private Integer season;

    @NotBlank(message = "Поле студія не може бути пустим")
    private String studio;

    private String fileName;

    @Column(name = "age_restrictions")
    private String ageRestrictions;

    @NotBlank(message = "Поле тривалість не може бути пустим")
    private String duration;

    @NotBlank(message = "Поле озвучування не може бути пустим")
    private String voiceover;

    @NotBlank(message = "Поле головні герої не може бути пустим")
    private String protagonists;

    @NotBlank(message = "Поле опис не може бути пустим")
    @Column(length = 2000)
    private String description;

    @Column(name = "video_url")
    private String videoUrl;


}

