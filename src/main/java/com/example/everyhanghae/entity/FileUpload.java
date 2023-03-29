package com.example.everyhanghae.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileUpload {
    @Id
    @GeneratedValue
    private Long ID;

    @OneToOne
    @JoinColumn(name = "boardId")
    private Board board;

    @Column
    private String fileName;

    @Column
    private String filePath;

    public FileUpload(Board board, String fileName, String filePath) {
        this.board = board;
        this.fileName = fileName;
        this.filePath = filePath;
    }

}
