package com.floqdrive.controller;

import com.floqdrive.dto.FileIndoDto;
import com.floqdrive.dto.FileUploadResponse;
import com.floqdrive.service.FileStorageService;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;

    // Upload file
    @PatchMapping("/upload")
    public FileUploadResponse uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId)
    {
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
