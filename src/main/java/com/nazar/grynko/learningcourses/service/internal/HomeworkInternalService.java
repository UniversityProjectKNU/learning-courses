package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.dto.hoeworkfile.FileDto;
import com.nazar.grynko.learningcourses.model.HomeworkFile;
import com.nazar.grynko.learningcourses.model.UserToLesson;
import com.nazar.grynko.learningcourses.repository.HomeworkFileRepository;
import com.nazar.grynko.learningcourses.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class HomeworkInternalService {

    private final FileService fileService;
    private final HomeworkFileRepository homeworkFileRepository;

    public HomeworkInternalService(FileService fileService, HomeworkFileRepository homeworkFileRepository) {
        this.fileService = fileService;
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
    public HomeworkFile upload(MultipartFile multipartFile, UserToLesson userToLesson) {
        var homework = homeworkFileRepository.get(userToLesson);
        HomeworkFile homeworkFile;

        if (homework.isPresent()) {
            homeworkFile = homework.get();
            fileService.delete(homeworkFile.getTitle());
        } else {
            homeworkFile = new HomeworkFile()
                    .setUserToLesson(userToLesson);
        }

        var title = fileService.upload(multipartFile);

        homeworkFile.setTitle(title)
                .setSize(multipartFile.getSize());
        homeworkFile = homeworkFileRepository.save(homeworkFile);

        return homeworkFile;
    }

    public FileDto download(UserToLesson userToLesson) {
        var homework = homeworkFileRepository.get(userToLesson)
                .orElseThrow(IllegalArgumentException::new);

        return downloadInternal(homework);
    }

    public FileDto download(Long id) {
        var homework = get(id);
        return downloadInternal(homework);
    }

    private FileDto downloadInternal(HomeworkFile file) {
        var title = file.getTitle();

        return new FileDto()
                .setData(fileService.download(title))
                .setTitle(title);
    }

    public void delete(UserToLesson userToLesson) {
        var homework = homeworkFileRepository.get(userToLesson).orElseThrow(IllegalArgumentException::new);
        fileService.delete(homework.getTitle());
    }

    public HomeworkFile get(UserToLesson userToLesson) {
        return homeworkFileRepository.get(userToLesson).orElse(null);
    }

    public HomeworkFile get(Long id) {
        return homeworkFileRepository.findById(id).orElse(null);
    }

}
