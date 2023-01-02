package com.nazar.grynko.learningcourses.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="chapters_templates")
public class ChapterTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Integer number;

    @ManyToOne
    @JoinColumn(
            name = "course_template_id",
            nullable = false
    )
    @JsonIgnore
    private CourseTemplate courseTemplate;

    @OneToMany(
            mappedBy = "chapterTemplate",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private Set<LessonTemplate> lessonTemplates = new HashSet<>();

}
