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
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_chapters_course_template_id")
    )
    @JsonIgnore
    private CourseTemplate courseTemplate;

}
