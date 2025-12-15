package com.floqdrive.controller;

import com.floqdrive.dto.FileIndoDto;
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

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;

    // Upload file
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

        return fileStorageService.uploadFile(file, userId);
    }

    // Download file
    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long fileId,
            @RequestParam Long userId)
    {
        Resource resource = fileStorageService.loadFile(fileId, userId);

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
            @RequestParam Long userId)
    {
        fileStorageService.deleteFile(fileId, userId);
    }

    // Get user file list
    @GetMapping
    public List<FileIndoDto> listFiles(@RequestParam Long userId)
    {
        return fileStorageService.listFiles(userId);
    }

}
