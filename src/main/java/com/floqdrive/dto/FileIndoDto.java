package com.floqdrive.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FileIndoDto
{
    private Long id;
    private String name;
    private Long size;
    private LocalDateTime uploadedAt;
}
