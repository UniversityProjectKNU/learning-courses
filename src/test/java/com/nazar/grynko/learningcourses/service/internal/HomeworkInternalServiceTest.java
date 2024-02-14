package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.repository.HomeworkFileRepository;
import com.nazar.grynko.learningcourses.service.S3FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.nazar.grynko.learningcourses.util.MockEntities.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HomeworkInternalServiceTest {

    @Mock
    private S3FileService s3FileService;
    @Mock
    private HomeworkFileRepository homeworkFileRepository;

    @InjectMocks
    private HomeworkInternalService sut;

    @Test
    void uploadFile_newFile_checkResult() {
        // PREPARE
        var s3Name = "test.s3Name";
        var multipartFile = mockMultipartFile();
        var userToLesson = mockUsersToLesson();
        var homeworkFile = mockHomeWorkFile()
                .setS3Name(s3Name)
                .setTitle(multipartFile.getOriginalFilename())
                .setSize(multipartFile.getSize());

        when(homeworkFileRepository.getByUserToLesson(any())).thenReturn(Optional.empty());
        when(s3FileService.uploadToS3(any())).thenReturn(s3Name);
        when(homeworkFileRepository.save(any())).thenReturn(homeworkFile);

        // ACT
        var actual = sut.uploadFile(multipartFile, userToLesson);

        // VERIFY
        assertNotNull(actual);
        assertEquals(homeworkFile, actual);
    }

    @Test
    void uploadFile_changeOldFile_checkResult() {
        // PREPARE
        var s3Name = "test.s3Name";
        var multipartFile = mockMultipartFile();
        var userToLesson = mockUsersToLesson();
        var homeworkFile = mockHomeWorkFile()
                .setS3Name(s3Name)
                .setTitle(multipartFile.getOriginalFilename())
                .setSize(multipartFile.getSize());

        when(homeworkFileRepository.getByUserToLesson(any())).thenReturn(Optional.empty());
        when(s3FileService.uploadToS3(any())).thenReturn(s3Name);
        when(homeworkFileRepository.save(any())).thenReturn(homeworkFile);

        // ACT
        var actual = sut.uploadFile(multipartFile, userToLesson);

        // VERIFY
        assertNotNull(actual);
        assertEquals(homeworkFile, actual);
    }

    @Test
    void downloadFileByUserToLesson_checkResult() {
        // PREPARE
        var homeworkFile = mockHomeWorkFile();
        var fileDto = mockFileDto()
                .setTitle(homeworkFile.getTitle());

        when(homeworkFileRepository.getByUserToLesson(any())).thenReturn(Optional.of(homeworkFile));
        when(s3FileService.download(any())).thenReturn(fileDto.getData());

        // ACT
        var actual = sut.downloadFile(mockUsersToLesson());

        // VERIFY
        assertNotNull(actual);
        assertEquals(fileDto.getTitle(), actual.getTitle());
        assertArrayEquals(fileDto.getData(), actual.getData());
        assertEquals(fileDto.getS3Name(), actual.getS3Name());
    }

    @Test
    void downloadFileByUserToLesson_error_noSuchFile() {
        // PREPARE
        when(homeworkFileRepository.getByUserToLesson(any())).thenReturn(Optional.empty());

        // ACT + VERIFY
        assertThrows(IllegalArgumentException.class, () -> sut.downloadFile(mockUsersToLesson()));
    }

    @Test
    void downloadFileByFileId_checkResult() {
        // PREPARE
        var homeworkFile = mockHomeWorkFile();
        var fileDto = mockFileDto()
                .setTitle(homeworkFile.getTitle());

        when(homeworkFileRepository.findById(anyLong())).thenReturn(Optional.of(homeworkFile));
        when(s3FileService.download(any())).thenReturn(fileDto.getData());

        // ACT
        var actual = sut.downloadFile(1L);

        // VERIFY
        assertNotNull(actual);
        assertEquals(fileDto.getTitle(), actual.getTitle());
        assertArrayEquals(fileDto.getData(), actual.getData());
        assertEquals(fileDto.getS3Name(), actual.getS3Name());
    }

    @Test
    void downloadFileByFileId_error_noSuchFile() {
        // PREPARE
        when(homeworkFileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT + VERIFY
        assertThrows(IllegalArgumentException.class, () -> sut.downloadFile(1L));
    }

    @Test
    void deleteFile_checkResult() {
        // PREPARE
        var homeworkFile = mockHomeWorkFile();

        when(homeworkFileRepository.getByUserToLesson(any())).thenReturn(Optional.of(homeworkFile));

        // ACT
        sut.deleteFile(mockUsersToLesson());

        // VERIFY
        verify(homeworkFileRepository, times(1)).getByUserToLesson(any());
        verify(s3FileService, times(1)).deleteFromS3(any());
        verify(homeworkFileRepository, times(1)).delete(any());
    }

    @Test
    void deleteFile_error_noSuchFile() {
        // PREPARE
        when(homeworkFileRepository.getByUserToLesson(any())).thenThrow(new IllegalArgumentException());

        // ACT + VERIFY
        assertThrows(IllegalArgumentException.class, () -> sut.deleteFile(mockUsersToLesson()));
    }

    @Test
    void getFileByUserToLesson_checkResult() {
        // PREPARE
        var homeworkFile = mockHomeWorkFile();

        when(homeworkFileRepository.getByUserToLesson(any())).thenReturn(Optional.of(homeworkFile));

        // ACT
        var actual = sut.getFile(mockUsersToLesson());

        // VERIFY
        assertNotNull(actual);
    }

    @Test
    void getFileByUserToLesson_error_noSuchFile() {
        // PREPARE
        when(homeworkFileRepository.getByUserToLesson(any())).thenThrow(new IllegalArgumentException());

        // ACT + VERIFY
        assertThrows(IllegalArgumentException.class, () -> sut.getFile(mockUsersToLesson()));
    }

    @Test
    void getFileByFileId_checkResult() {
        // PREPARE
        var homeworkFile = mockHomeWorkFile();

        when(homeworkFileRepository.findById(anyLong())).thenReturn(Optional.of(homeworkFile));

        // ACT
        var actual = sut.getFile(1L);

        // VERIFY
        assertNotNull(actual);
    }

    @Test
    void getFileByFileId_error_noSuchFile() {
        // PREPARE
        when(homeworkFileRepository.findById(anyLong())).thenThrow(new IllegalArgumentException());

        // ACT + VERIFY
        assertThrows(IllegalArgumentException.class, () -> sut.getFile(1L));
    }

}
