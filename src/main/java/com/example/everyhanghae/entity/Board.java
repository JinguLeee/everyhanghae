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

    @OneToOne
    @JoinColumn(name = "boardType")
    private BoardType boardType;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public Board(BoardRequestDto boardRequestDto, BoardType boardType, User user) {
        this.classId = user.getClassId();
        this.boardType = boardType;
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
        this.user = user;
    }

    public void update(String title, String content) {
        this.title=title;
        this.content=content;
    }

}
