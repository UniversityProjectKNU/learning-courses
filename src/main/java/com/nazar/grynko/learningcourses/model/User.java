package com.nazar.grynko.learningcourses.model;

import com.sun.xml.bind.v2.TODO;
import lombok.*;
import lombok.experimental.Accessors;
import org.aspectj.apache.bcel.classfile.Unknown;

import javax.persistence.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    // TODO [42S22] Unknown column 'user0_.dateOfBirth' in 'field list' when execute [select entity from User entity]
    @Temporal(TemporalType.DATE)
    private Calendar dateOfBirth;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_to_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

}
