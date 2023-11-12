package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.hoeworkfile.FileDto;
import com.nazar.grynko.learningcourses.dto.hoeworkfile.HomeworkFileDto;
import com.nazar.grynko.learningcourses.mapper.HomeworkFileMapper;
import com.nazar.grynko.learningcourses.service.internal.HomeworkInternalService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HomeworkService {

    private final HomeworkInternalService homeworkInternalService;
    private final HomeworkFileMapper homeworkFileMapper;


    public HomeworkService(HomeworkInternalService homeworkInternalService, HomeworkFileMapper homeworkFileMapper) {
        this.homeworkInternalService = homeworkInternalService;
        this.homeworkFileMapper = homeworkFileMapper;
    }

    public HomeworkFileDto getFileInfo(Long id) {
        var entity = homeworkInternalService.get(id);
        return homeworkFileMapper.toDto(entity);
    }

    public FileDto download(Long fileId) {
        return homeworkInternalService.download(fileId);
    }

}
