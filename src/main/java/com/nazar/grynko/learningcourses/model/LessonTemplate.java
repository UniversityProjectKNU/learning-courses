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
@Table(name="lessons_templates")
public class LessonTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Integer number;
    @Column(nullable = false)
    private Integer maxMark;
    @Column(nullable = false)
    private Integer successMark;

    @ManyToOne
    @JoinColumn(
            name = "chapter_template_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_lessons_chapter_template_id")
    )
    @JsonIgnore
    private ChapterTemplate chapterTemplate;

}
