package com.nazar.grynko.learningcourses.mapper;

import com.nazar.grynko.learningcourses.dto.role.RoleDto;
import com.nazar.grynko.learningcourses.model.RoleType;
import org.springframework.stereotype.Component;

//TODO delete
@Component
public class RoleMapper {

    public RoleDto toDto(RoleType type) {
        return new RoleDto().setType(type);
    }

}
