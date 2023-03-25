package com.example.everyhanghae.service;

import com.example.everyhanghae.dto.request.BoardRequestDto;
import com.example.everyhanghae.dto.request.CommentRequestDto;
import com.example.everyhanghae.dto.response.CommentResponseDto;
import com.example.everyhanghae.entity.Board;
import com.example.everyhanghae.entity.Comment;
import com.example.everyhanghae.entity.User;
import com.example.everyhanghae.exception.CustomException;
import com.example.everyhanghae.repository.CommentRepository;
import com.example.everyhanghae.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.everyhanghae.exception.CustomErrorCode.COMMENT_NOT_FOUND;
import static com.example.everyhanghae.exception.CustomErrorCode.NOT_AUTHOR;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final UserRepository userRepository;

    @Transactional
    public void createComment(Long boardId, CommentRequestDto commentRequestDto, User user) {
        Board board = boardService.isExistBoard(boardId); //게시글이 있는지 확인
        Comment comment = new Comment(commentRequestDto,user,board);
        commentRepository.save(comment);

    }

    @Transactional
    public void update(Long commentId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = getComment(commentId);
        isAuthor(comment,user);
        comment.update(commentRequestDto);
    }



    public void delete(Long commentId, User user) {
        Comment comment = getComment(commentId);
        isAuthor(comment,user);
        commentRepository.deleteById(commentId);
    }


    //댓글 존재하는지 확인
    private Comment getComment(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(
                ()-> new CustomException(COMMENT_NOT_FOUND)
        );
    }

    //댓글 작성자 일치 여부 확인
    public void isAuthor(Comment comment, User user) {
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(NOT_AUTHOR);
        }
    }


}
