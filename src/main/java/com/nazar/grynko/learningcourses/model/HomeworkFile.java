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
@Table(name="homework_files")
public class HomeworkFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="s3_name", nullable = false)
    private String s3Name;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Long size;

    @ManyToOne
    @JoinColumn(
            name = "user_to_lesson_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_homework_files_user_to_lesson_id")
    )
    @JsonIgnore
    private UserToLesson userToLesson;

}
