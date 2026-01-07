package com.floqdrive.controller;

import com.floqdrive.dto.FileInfoDto;
import com.floqdrive.dto.FileUploadResponse;
import com.floqdrive.entity.User;
import com.floqdrive.service.FileStorageService;

import com.floqdrive.service.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// FileController - work with user files
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;

    /**
     *  Upload file
     *  @param file MultipartFile from Form
     *  @param userDetails current user from SecurityContext
     */
    @PostMapping("/upload")
    public FileUploadResponse uploadFile(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails) // Spring sec paste current user
    {
        Long userId = userDetails.getUser().getId();

        // Check file empty
        if(file.isEmpty())
        {
            throw new IllegalArgumentException("File is empty");
        }

        // Check file size <= 5 MB
        long maxSize = 5 * 1024 * 1024;
        if(file.getSize() > maxSize)
        {
            throw new IllegalArgumentException("File is too large (max 5 MB)");
        }

        // Delegate file save to a service
        return fileStorageService.uploadFile(file, userId);
    }

    // Download file
    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long fileId,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        // Load file from disk
        Long userId = userDetails.getUser().getId();
        Resource resource = fileStorageService.loadFile(fileId, userId);

        // Send file as attachment
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""+resource.getFilename() + "\""
                )
                .body(resource);
    }

    // Delete file
    @DeleteMapping("/{fileId}")
    public void deleteFile(
            @PathVariable Long fileId,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        Long userId = userDetails.getUser().getId();
        fileStorageService.deleteFile(fileId, userId);
    }

    // Get list of user files
    @GetMapping
    public List<FileInfoDto> listFiles(@AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        Long userId = userDetails.getUser().getId();
        return fileStorageService.listFiles(userId);
    }

}
