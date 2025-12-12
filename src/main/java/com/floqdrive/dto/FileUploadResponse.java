package com.floqdrive.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploadResponse
{
    private Long id;
    private String fileName;
    private Long size;
}
