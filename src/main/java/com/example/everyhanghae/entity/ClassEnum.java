package com.example.everyhanghae.entity;

public enum ClassEnum {

    /*
    시크릿 코드를 가져오기 위한 Enum 설계
    추후에 시크릿 코드를 저장하는 화면이 생길 시 데이터베이스에 저장할 예정
    */
    CLASS12("12기", "12345"),
    CLASS13("13기", "ABCDE");

    private final String className;
    private final String secretCode;

    ClassEnum(String className, String secretCode) {
        this.className = className;
        this.secretCode = secretCode;
    }

    // 시크릿 코드를 비교하기 위해 Getter 사용
    public String getSecretCode() {
        return secretCode;
    }

}
