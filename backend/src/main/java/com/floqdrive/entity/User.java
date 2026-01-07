package com.floqdrive.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    // Stored in encrypted form
    private String password;

    // One user - many files
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<FileEntity> files;
}
