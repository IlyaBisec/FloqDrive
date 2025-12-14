package com.floqdrive.service.impl;

import com.floqdrive.dto.FileIndoDto;
import com.floqdrive.dto.FileUploadResponse;
import com.floqdrive.entity.FileEntity;
import com.floqdrive.entity.User;
import com.floqdrive.exception.NotFoundException;
import com.floqdrive.repository.FileRepository;
import com.floqdrive.repository.UserRepository;
import com.floqdrive.service.FileStorageService;

import org.hibernate.annotations.NotFound;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// Realisation service for file store
@Service
public class FileStorageServiceImpl implements FileStorageService
{
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    // Root directory
    private final Path root = Paths.get("uploads");

    public FileStorageServiceImpl(UserRepository userRepository, FileRepository fileRepository)
    {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;

        // Create root directory 'uploads'
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    // Load user file
    @Override
    public FileUploadResponse uploadFile(MultipartFile file, Long userId)
    {
        // Check user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Generate uniq file name
        String storedName = UUID.randomUUID().toString();
        String originalName = file.getOriginalFilename();

        // Create user folder uploads/{userId}
        Path userDir = root.resolve(userId.toString());
        Path targetPath = userDir.resolve(storedName);

        try{
            Files.createDirectories(userDir);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e){
            throw  new RuntimeException("Failed to save file", e);
        }

        // Save file meta in database
        FileEntity entity = FileEntity.builder()
                .owner(user)
                .originalName(originalName)
                .storedName(storedName)
                .size(file.getSize())
                .uploadedAt(LocalDateTime.now())
                .build();

        fileRepository.save(entity);

        // Return DTO
        return new FileUploadResponse(entity.getId(), originalName);
    }

    @Override
    public Resource loadFile(Long fileId, Long userId)
    {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException("File not found"));

        if(!file.getOwner().getId().equals(userId))
        {
            throw new NotFoundException("Access denied");
        }

        Path filePath = root.resolve(userId.toString()).resolve(file.getStoredName());
        return new FileSystemResource(filePath);
    }

    @Override
    public void deleteFile(Long fileId, Long userId)
    {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException("File not found"));

        if(!file.getOwner().getId().equals(userId))
        {
            throw new NotFoundException("Access denied");
        }

        Path filePath = root.resolve(userId.toString()).resolve(file.getStoredName());

        try{
            Files.deleteIfExists((filePath));
        } catch (IOException ignored)
        {}


        fileRepository.delete(file);
    }

    @Override
    public List<FileIndoDto> listFiled(Long userId)
    {
        User user  = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return fileRepository.findByOwner((user)).stream()
                .map(f -> new FileIndoDto(f.getId(), f.getOriginalName(), f.getSize(), f.getUploadedAt()
                )).toList();
    }
}
