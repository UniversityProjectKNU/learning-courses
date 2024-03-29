package com.nazar.grynko.learningcourses.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.nazar.grynko.learningcourses.dto.hoeworkfile.HomeworkFileDto;
import com.nazar.grynko.learningcourses.model.HomeworkFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class FileService {

    @Value("${cloud.aws.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public FileService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public String upload(MultipartFile multipartFile) {
        var fileName = formatFileName(multipartFile.getOriginalFilename());
        var file = convertFile(multipartFile);

        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));

        if (file.delete()) {
            log.info("{} file was deleted", file.getName());
        }

        return fileName;
    }

    public byte[] download(String fileName) {
        var s3Object = s3Client.getObject(bucketName, fileName);
        byte[] content;

        try (var is = s3Object.getObjectContent()) {
            content = IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new IllegalStateException("Error downloading file: " + e.getMessage());
        }

        return content;
    }

    public void delete(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
    }

    private File convertFile(MultipartFile multipartFile) {
        if (isNull(multipartFile.getOriginalFilename())) {
            throw new NullPointerException();
        }

        var file = new File(multipartFile.getOriginalFilename());

        try (var fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new IllegalStateException("Error converting multipartFile to file");
        }

        return file;
    }

    private String formatFileName(String fileName) {
        var newName = System.currentTimeMillis() + "_" + fileName;
        return newName.replace(" ", "_");
    }

}
