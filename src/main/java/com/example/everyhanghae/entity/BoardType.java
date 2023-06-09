package com.example.everyhanghae.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class BoardType {

    @Id
    private int boardType;

    @Column
    private String typeName;

}
