package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.service.FileService;
import com.nazar.grynko.learningcourses.service.HomeworkService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("learning-courses/api/v1/file")
public class FileController {

    private final FileService fileService;
    private final HomeworkService homeworkService;

    public FileController(FileService fileService,
                          HomeworkService homeworkService) {
        this.fileService = fileService;
        this.homeworkService = homeworkService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInfoById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(homeworkService.getFileInfo(id));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestBody MultipartFile file) {
        try {
            return ResponseEntity.ok(fileService.upload(file));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<?> download(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(homeworkService.download(id));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

}
