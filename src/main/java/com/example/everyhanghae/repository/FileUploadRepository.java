package com.example.everyhanghae.repository;

import com.example.everyhanghae.entity.Board;
import com.example.everyhanghae.entity.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileUploadRepository extends JpaRepository<FileUpload,Long> {

    Optional<FileUpload> findByBoard(Board board);
    void deleteAllByBoard(Board board);

}
