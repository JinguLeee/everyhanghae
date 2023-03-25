package com.example.everyhanghae.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ClassCode {
    @Id
    private int classId;

    @Column
    private String className;

    @Column
    private String secretCode;

}
