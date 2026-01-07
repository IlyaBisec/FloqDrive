package com.floqdrive.service;

import com.floqdrive.dto.FileInfoDto;
import com.floqdrive.dto.FileUploadResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// Service for work with user files
// upload, get, delete and list files
public interface FileStorageService
{
    // Upload user file:
    //  - generate uniq file name
    //  - save file in disk
    //  - create rec in database
    FileUploadResponse uploadFile(MultipartFile file, Long userId);

    // Load files for download
    Resource loadFile(Long fileId, Long userId);

    // Delete user file
    void deleteFile(Long fileId, Long userId);

    // Return user file list
    List<FileInfoDto> listFiles(Long userId);
}
