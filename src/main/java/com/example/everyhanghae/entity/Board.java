package com.example.everyhanghae.entity;
import com.example.everyhanghae.dto.request.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int classId;

    @Column(nullable = false)
    private int boardType;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public Board(BoardRequestDto postRequestDto, User user) {
        this.classId = user.getClassId();
        this.boardType = postRequestDto.getBoardType();
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.user = user;
    }

    public void update(String title, String content) {
        this.title=title;
        this.content=content;
    }

}
