package dev.mvc.burgerpost;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
        postno                            NUMBER(10)         NOT NULL         PRIMARY KEY,
        memberno                            NUMBER(10)     NOT NULL ,
        burgerno                                NUMBER(10)         NOT NULL ,
        title                                 VARCHAR2(300)         NOT NULL,
        content                               CLOB                  NOT NULL,
        recomcnt                                 NUMBER(7)         DEFAULT 0         NOT NULL,
        readcnt                                   NUMBER(7)         DEFAULT 0         NOT NULL,
        replycnt                              NUMBER(7)         DEFAULT 0         NOT NULL,
        passwd                                VARCHAR2(15)         NOT NULL,
        word                                  VARCHAR2(300)         NULL ,
        rdate                                 DATE               NOT NULL,
        file1                                   VARCHAR(100)          NULL,
        file1saved                            VARCHAR(100)          NULL,
        thumb1                              VARCHAR(100)          NULL,
        size1                                 NUMBER(10)      DEFAULT 0 NULL,  
        map                                  VARCHAR2(1000)            NULL,
        youtube                             VARCHAR2(1000)            NULL,   
 */

@Getter @Setter @ToString
public class BurgerpostVO {
    /** 게시물 번호 */
    private int postno;
    /** 관리자 권한의 회원 번호 */
    private int memberno;
    /** 햄버거 번호 */
    private int burgerno;
    /** 제목 */
    private String title = "";
    /** 내용 */
    private String content = "";
    /** 추천수 */
    private int recomcnt;
    /** 조회수 */
    private int readcnt = 0;
    /** 댓글수 */
    private int replycnt = 0;
    /** 패스워드 */
    private String passwd = "";
    /** 검색어 */
    private String word = "";
    /** 등록 날짜 */
    private String rdate = "";
    /** 지도 */
    private String map = "";
    /** Youtube */
    private String youtube = "";
    
    // 파일 업로드 관련
    // -----------------------------------------------------------------------------------
    /**
    이미지 파일
    <input type='file' class="form-control" name='file1MF' id='file1MF' 
               value='' placeholder="파일 선택">
    */
    private MultipartFile file1MF = null;
    /** 메인 이미지 크기 단위, 파일 크기 */
    private String size1_label = "";
    /** 메인 이미지 */
    private String file1 = "";
    /** 실제 저장된 메인 이미지 */
    private String file1saved = "";
    /** 메인 이미지 preview */
    private String thumb1 = "";
    /** 메인 이미지 크기 */
    private long size1 = 0;

}