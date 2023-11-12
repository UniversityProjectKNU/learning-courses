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
public class S3FileService {

    @Value("${cloud.aws.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public S3FileService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadToS3(MultipartFile multipartFile) {
        var s3FileName = formatS3FileName(multipartFile.getOriginalFilename());
        var file = convertFile(multipartFile);

        s3Client.putObject(new PutObjectRequest(bucketName, s3FileName, file));

        if (file.delete()) {
            log.info("{} file was deleted", file.getName());
        }

        return s3FileName;
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

    public void deleteFromS3(String s3FileName) {
        s3Client.deleteObject(bucketName, s3FileName);
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

    private String formatS3FileName(String fileName) {
        var newName = System.currentTimeMillis() + "_" + fileName;
        return newName.replace(" ", "_");
    }

}
