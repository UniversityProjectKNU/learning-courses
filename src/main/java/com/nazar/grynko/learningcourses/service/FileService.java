package com.nazar.grynko.learningcourses.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
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
        var fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        var file = convertFile(multipartFile);

        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));

        if (file.delete()) {
            log.info("{} file was deleted", file.getName());
        }

        return "File was uploaded: " + fileName;
    }

    public byte[] download(String fileName) {
        var s3Object = s3Client.getObject(bucketName, fileName);
        byte[] content;

        try (var is = s3Object.getObjectContent()) {
            content = IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new IllegalStateException("Error downloading file");
        }
        return content;
    }

    public String delete(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return "File was removed: " + fileName;
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

}
