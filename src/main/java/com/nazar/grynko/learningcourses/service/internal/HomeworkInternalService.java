package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.dto.hoeworkfile.FileDto;
import com.nazar.grynko.learningcourses.exception.EntityNotFoundException;
import com.nazar.grynko.learningcourses.model.HomeworkFile;
import com.nazar.grynko.learningcourses.model.UserToLesson;
import com.nazar.grynko.learningcourses.repository.HomeworkFileRepository;
import com.nazar.grynko.learningcourses.service.S3FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HomeworkInternalService {

    private final S3FileService s3FileService;
    private final HomeworkFileRepository homeworkFileRepository;

    private static final String FILE_ID_MISSING_PATTER = "Homework file %d doesn't exist";
    private static final String FILE_UTL_MISSING_PATTER = "Homework file  of user %d and lesson %d doesn't exist";

    public HomeworkInternalService(S3FileService s3FileService,
                                   HomeworkFileRepository homeworkFileRepository) {
        this.s3FileService = s3FileService;
        this.homeworkFileRepository = homeworkFileRepository;
    }

    /**
     * If user already has uploaded file for this lesson userToLesson.lesson then we delete old file from
     * AWS, database and upload new to AWS and update information in database.
     *
     * @param multipartFile is file that user has sent
     * @param userToLesson  is entity from {@link HomeworkFileRepository} database with user and lesson
     * @return entity with meta information about file and its userToLesson
     */
    public HomeworkFile uploadFile(MultipartFile multipartFile, UserToLesson userToLesson) {
        var homework = homeworkFileRepository.getByUserToLesson(userToLesson);
        HomeworkFile homeworkFile;

        if (homework.isPresent()) {
            homeworkFile = homework.get();
            s3FileService.deleteFromS3(homeworkFile.getS3Name());
        } else {
            homeworkFile = new HomeworkFile()
                    .setUserToLesson(userToLesson);
        }

        var s3Name = s3FileService.uploadToS3(multipartFile);

        homeworkFile.setS3Name(s3Name)
                .setTitle(multipartFile.getOriginalFilename())
                .setSize(multipartFile.getSize());

        homeworkFile = homeworkFileRepository.save(homeworkFile);

        return homeworkFile;
    }

    public FileDto downloadFile(UserToLesson userToLesson) {
        var homework = getFileByUserToLesson(userToLesson);
        return downloadInternal(homework);
    }

    public FileDto downloadFile(Long fileId) {
        var homework = getFileById(fileId);
        return downloadInternal(homework);
    }

    private FileDto downloadInternal(HomeworkFile file) {
        var s3Name = file.getS3Name();

        return new FileDto()
                .setTitle(file.getTitle())
                .setData(s3FileService.download(s3Name))
                .setS3Name(s3Name);
    }

    public void deleteFile(UserToLesson userToLesson) {
        var homework = getFileByUserToLesson(userToLesson);
        s3FileService.deleteFromS3(homework.getS3Name());
        deleteFromDatabase(homework);
    }

    private void deleteFromDatabase(HomeworkFile homework) {
        homeworkFileRepository.delete(homework);
    }

    public HomeworkFile getFile(UserToLesson userToLesson) {
        return homeworkFileRepository.getByUserToLesson(userToLesson).orElse(null);
    }

    public HomeworkFile getFile(Long fileId) {
        return homeworkFileRepository.findById(fileId).orElse(null);
    }

    private HomeworkFile getFileById(Long fileId) {
        return homeworkFileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(FILE_ID_MISSING_PATTER, fileId)));
    }

    private HomeworkFile getFileByUserToLesson(UserToLesson utl) {
        return homeworkFileRepository.getByUserToLesson(utl)
                .orElseThrow(() -> new EntityNotFoundException(String.format(FILE_UTL_MISSING_PATTER, utl.getUser().getId(), utl.getLesson().getId())));
    }

}
