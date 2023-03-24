package com.example.everyhanghae.entity;

public enum BoardEnum {

    /*
    boardNumber를 가져오기 위한 Enum 설계
    현재 세 가지 게시판으로 진행하기로 했지만
    추후에 게시판 분류를 등록하는 화면이 생길 시 데이터베이스에 저장할 예정
    */
    NOTICE_BOARD(1, "공지방"),
    BIN_BOARD(2, "감정쓰레기통"),
    STUDY_BOARD(3, "스터디 게시판"),;

    private final int boardNumber;
    private final String boardName;

    BoardEnum(int boardNumber, String boardName) {
        this.boardNumber = boardNumber;
        this.boardName = boardName;
    }

    // 저장할 때 boardNumber로 저장
    public int getBoardNumber() {
        return boardNumber;
    }

}
