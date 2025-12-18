package com.floqdrive.repository;

import com.floqdrive.entity.FileEntity;
import com.floqdrive.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long>
{
    List<FileEntity> findByOwner(User owner);
}
