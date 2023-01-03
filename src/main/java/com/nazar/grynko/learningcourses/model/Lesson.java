package com.nazar.grynko.learningcourses.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Integer number = 0;
    @Column(nullable = false)
    private boolean isFinished;

    @ManyToOne
    @JoinColumn(
            name = "chapter_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_lessons_chapters_id")
    )
    @JsonIgnore
    private Chapter chapter;

}
