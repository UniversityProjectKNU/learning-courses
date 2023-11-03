package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.hoeworkfile.HomeworkFileDto;
import com.nazar.grynko.learningcourses.service.HomeworkService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("learning-courses/api/v1/files")
@RolesAllowed({"ADMIN", "INSTRUCTOR"})
public class FileController {

    private final HomeworkService homeworkService;

    public FileController(HomeworkService homeworkService) {
        this.homeworkService = homeworkService;
    }

/*    @PostMapping("/files/upload")
    public ResponseEntity<HomeworkFileDto> upload(@RequestBody MultipartFile file) {
        return ResponseEntity.ok(homeworkService.upload(file));
    }*/

    @GetMapping("/files/file/info")
    public ResponseEntity<HomeworkFileDto> getFileInfo(@RequestParam Long fileId) {
        return ResponseEntity.ok(homeworkService.getFileInfo(fileId));
    }

    @GetMapping("/files/file")
    public ResponseEntity<ByteArrayResource> download(@RequestParam Long fileId) {
        var fileDto = homeworkService.download(fileId);
        var data = fileDto.getData();

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileDto.getTitle() + "\"")
                .body(new ByteArrayResource(data));
    }

/*    @DeleteMapping("/files/file")
    public ResponseEntity<SimpleDto<String>> delete(@RequestParam Long fileId) {
        homeworkService.delete(fileId);
        return ResponseEntity.ok(new SimpleDto<>("Ok"));
    }*/

}


