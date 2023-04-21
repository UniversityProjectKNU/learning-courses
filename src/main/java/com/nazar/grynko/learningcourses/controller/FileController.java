package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.service.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("learning-courses/api/v1/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
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

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> download(@RequestParam String fileName) {
        var data = fileService.download(fileName);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(new ByteArrayResource(data));
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam String fileName) {
        fileService.delete(fileName);
    }

}
