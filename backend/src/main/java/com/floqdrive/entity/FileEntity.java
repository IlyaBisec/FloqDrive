package com.floqdrive.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Name of file the user uploaded
    private String originalName;
    // File name on disk (UUID)
    private String storedName;
    private Long size;
    private LocalDateTime uploadedAt;

    // File owner
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;
}
