package com.floqdrive.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FileIndoDto
{
    // ID file in database
    private Long id;
    // Original file name
    private String name;
    // File size
    private Long size;
    // Upload date
    private LocalDateTime uploadedAt;
}
