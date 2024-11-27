-- /src/main/webapp/WEB-INF/doc/컨텐츠/burgerpost.sql
DROP TABLE burgerpost CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능
DROP TABLE burgerpost;
COMMIT;

CREATE TABLE burgerpost(
        postno                            NUMBER(10)         NOT NULL         PRIMARY KEY,
        memberno                              NUMBER(10)     NOT NULL , -- FK
        burgerno                                NUMBER(10)         NOT NULL , -- FK
        title                                 VARCHAR2(100)         NOT NULL,
        content                               CLOB                  NOT NULL,
        recomcnt                                 NUMBER(7)         DEFAULT 0         NOT NULL,
        readcnt                                   NUMBER(7)         DEFAULT 0         NOT NULL,
        replycnt                              NUMBER(7)         DEFAULT 0         NOT NULL,
        passwd                                VARCHAR2(100)         NOT NULL,
        word                                  VARCHAR2(200)         NULL ,
        rdate                                 DATE               NOT NULL,
        file1                                   VARCHAR(100)          NULL,  -- 원본 파일명 image
        file1saved                            VARCHAR(100)          NULL,  -- 저장된 파일명, image
        thumb1                              VARCHAR(100)          NULL,   -- preview image
        size1                                 NUMBER(10)      DEFAULT 0 NULL,  -- 파일 사이즈
        map                                   VARCHAR2(1000)            NULL,
        youtube                               VARCHAR2(1000)            NULL,
        visible                                CHAR(1)         DEFAULT 'Y' NOT NULL,
        FOREIGN KEY (memberno) REFERENCES member (memberno),
        FOREIGN KEY (burgerno) REFERENCES burger (burgerno)
);

COMMENT ON TABLE burgerpost is '햄버거 게시물';
COMMENT ON COLUMN burgerpost.postno is '햄버거 게시물 번호';
COMMENT ON COLUMN burgerpost.memberno is '관리자 번호';
COMMENT ON COLUMN burgerpost.burgerno is '햄버거 번호';
COMMENT ON COLUMN burgerpost.title is '제목';
COMMENT ON COLUMN burgerpost.content is '내용';
COMMENT ON COLUMN burgerpost.recomcnt is '추천수';
COMMENT ON COLUMN burgerpost.readcnt is '조회수';
COMMENT ON COLUMN burgerpost.replycnt is '댓글수';
COMMENT ON COLUMN burgerpost.passwd is '패스워드';
COMMENT ON COLUMN burgerpost.word is '검색어';
COMMENT ON COLUMN burgerpost.rdate is '등록일';
COMMENT ON COLUMN burgerpost.file1 is '메인 이미지';
COMMENT ON COLUMN burgerpost.file1saved is '실제 저장된 메인 이미지';
COMMENT ON COLUMN burgerpost.thumb1 is '메인 이미지 Preview';
COMMENT ON COLUMN burgerpost.size1 is '메인 이미지 크기';
COMMENT ON COLUMN burgerpost.map is '지도';
COMMENT ON COLUMN burgerpost.youtube is 'Youtube 영상';
COMMENT ON COLUMN burgerpost.visible is '출력 모드';

DROP SEQUENCE burgerpost_seq;

CREATE SEQUENCE burgerpost_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지

-- 등록 화면 유형 1: 커뮤니티(공지사항, 게시판, 자료실, 갤러리,  Q/A...)글 등록
INSERT INTO burgerpost(postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, passwd, 
                     word, rdate, file1, file1saved, thumb1, size1)
VALUES(burgerpost_seq.nextval, 1, 7, '통새우 와퍼', '통새우 와퍼에 새우 2마리 추가', 0, 0, 0, '123',
       '버거', sysdate, 'space.jpg', 'space_1.jpg', 'space_t.jpg', 1000);

-- 유형 1 전체 목록
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, passwd, word, rdate,
           file1, file1saved, thumb1, size1
FROM burgerpost
ORDER BY postno DESC;

-- 유형 2 카테고리별 목록
INSERT INTO burgerpost(postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, passwd, 
                     word, rdate, file1, file1saved, thumb1, size1)
VALUES(burgerpost_seq.nextval, 1, 7, '콰트로치즈와퍼', '버거킹의 야심작', 0, 0, 0, '123',
       '햄버거', sysdate, 'space.jpg', 'space_1.jpg', 'space_t.jpg', 1000);
            
INSERT INTO burgerpost(postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, passwd, 
                     word, rdate, file1, file1saved, thumb1, size1)
VALUES(burgerpost_seq.nextval, 17, 7, '불고기와퍼', '좀 달다', 0, 0, 0, '123',
       '햄버거', sysdate, 'space.jpg', 'space_1.jpg', 'space_t.jpg', 1000);

INSERT INTO burgerpost(postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, passwd, 
                     word, rdate, file1, file1saved, thumb1, size1)
VALUES(burgerpost_seq.nextval, 17, 7, '사각새우버거', '은근 양이 많음', 0, 0, 0, '123',
       '햄버거', sysdate, 'space.jpg', 'space_1.jpg', 'space_t.jpg', 1000);

COMMIT;

-- 전체 목록
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, passwd, word, rdate,
           file1, file1saved, thumb1, size1, map, youtube mp4
FROM burgerpost
ORDER BY postno DESC;

-- 52번 burgerno 만 출력
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, passwd, word, rdate,
        LOWER(file1) as file1, file1saved, thumb1, size1, map, youtube
FROM burgerpost
WHERE burgerno=47
ORDER BY postno DESC;

-- 2번 burgerno 만 출력
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, passwd, word, rdate,
        LOWER(file1) as file1, file1saved, thumb1, size1, map, youtube
FROM burgerpost
WHERE burgerno=2
ORDER BY postno ASC;

-- 3번 burgerno 만 출력
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, passwd, word, rdate,
        LOWER(file1) as file1, file1saved, thumb1, size1, map, youtube
FROM burgerpost
WHERE burgerno=5
ORDER BY postno ASC;

commit;

-- 모든 레코드 삭제
DELETE FROM burgerpost;
commit;

-- 삭제
DELETE FROM burgerpost
WHERE postno = 25;
commit;

DELETE FROM burgerpost
WHERE burgerno=12 AND postno <= 41;

commit;


-- ----------------------------------------------------------------------------------------------------
-- 검색, burgerno별 검색 목록
-- ----------------------------------------------------------------------------------------------------
-- 모든글
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, word, rdate,
       file1, file1saved, thumb1, size1, map, youtube
FROM burgerpost
ORDER BY postno ASC;

-- 카테고리별 목록
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, word, rdate,
       file1, file1saved, thumb1, size1, map, youtube
FROM burgerpost
WHERE burgerno=2
ORDER BY postno ASC;

-- 1) 검색
-- ① burgerno별 검색 목록
-- word 컬럼의 존재 이유: 검색 정확도를 높이기 위하여 중요 단어를 명시
-- 글에 'swiss'라는 단어만 등장하면 한글로 '스위스'는 검색 안됨.
-- 이런 문제를 방지하기위해 'swiss,스위스,스의스,수의스,유럽' 검색어가 들어간 word 컬럼을 추가함.
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, word, rdate,
           file1, file1saved, thumb1, size1, map, youtube
FROM burgerpost
WHERE burgerno=8 AND word LIKE '%부대찌게%'
ORDER BY postno DESC;

-- title, content, word column search
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, word, rdate,
           file1, file1saved, thumb1, size1, map, youtube
FROM burgerpost
WHERE burgerno=8 AND (title LIKE '%부대찌게%' OR content LIKE '%부대찌게%' OR word LIKE '%부대찌게%')
ORDER BY postno DESC;

-- ② 검색 레코드 갯수
-- 전체 레코드 갯수, 집계 함수
SELECT COUNT(*)
FROM burgerpost
WHERE burgerno=8;

  COUNT(*)  <- 컬럼명
----------
         5
         
SELECT COUNT(*) as readcnt -- 함수 사용시는 컬럼 별명을 선언하는 것을 권장
FROM burgerpost
WHERE burgerno=8;

       readcnt <- 컬럼명
----------
         5

-- burgerno 별 검색된 레코드 갯수
SELECT COUNT(*) as readcnt
FROM burgerpost
WHERE burgerno=8 AND word LIKE '%부대찌게%';

SELECT COUNT(*) as readcnt
FROM burgerpost
WHERE burgerno=8 AND (title LIKE '%부대찌게%' OR content LIKE '%부대찌게%' OR word LIKE '%부대찌게%');

-- SUBSTR(컬럼명, 시작 index(1부터 시작), 길이), 부분 문자열 추출
SELECT postno, SUBSTR(title, 1, 4) as title
FROM burgerpost
WHERE burgerno=8 AND (content LIKE '%부대%');

-- SQL은 대소문자를 구분하지 않으나 WHERE문에 명시하는 값은 대소문자를 구분하여 검색
SELECT postno, title, word
FROM burgerpost
WHERE burgerno=8 AND (word LIKE '%FOOD%');

SELECT postno, title, word
FROM burgerpost
WHERE burgerno=8 AND (word LIKE '%food%'); 

SELECT postno, title, word
FROM burgerpost
WHERE burgerno=8 AND (LOWER(word) LIKE '%food%'); -- 대소문자를 일치 시켜서 검색

SELECT postno, title, word
FROM burgerpost
WHERE burgerno=8 AND (UPPER(word) LIKE '%' || UPPER('FOOD') || '%'); -- 대소문자를 일치 시켜서 검색 ★

SELECT postno, title, word
FROM burgerpost
WHERE burgerno=8 AND (LOWER(word) LIKE '%' || LOWER('Food') || '%'); -- 대소문자를 일치 시켜서 검색

SELECT postno || '. ' || title || ' 태그: ' || word as title -- 컬럼의 결합, ||
FROM burgerpost
WHERE burgerno=8 AND (LOWER(word) LIKE '%' || LOWER('Food') || '%'); -- 대소문자를 일치 시켜서 검색


SELECT UPPER('한글') FROM dual; -- dual: 오라클에서 SQL 형식을 맞추기위한 시스템 테이블

-- ----------------------------------------------------------------------------------------------------
-- 검색 + 페이징 + 메인 이미지
-- ----------------------------------------------------------------------------------------------------
-- step 1
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube
FROM burgerpost
WHERE burgerno=1 AND (title LIKE '%단풍%' OR content LIKE '%단풍%' OR word LIKE '%단풍%')
ORDER BY postno DESC;

-- step 2
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, rownum as r
FROM (
          SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
                     file1, file1saved, thumb1, size1, map, youtube
          FROM burgerpost
          WHERE burgerno=1 AND (title LIKE '%단풍%' OR content LIKE '%단풍%' OR word LIKE '%단풍%')
          ORDER BY postno DESC
);

-- step 3, 1 page
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, r
FROM (
           SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
                      file1, file1saved, thumb1, size1, map, youtube, rownum as r
           FROM (
                     SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
                                file1, file1saved, thumb1, size1, map, youtube
                     FROM burgerpost
                     WHERE burgerno=1 AND (title LIKE '%단풍%' OR content LIKE '%단풍%' OR word LIKE '%단풍%')
                     ORDER BY postno DESC
           )          
)
WHERE r >= 1 AND r <= 3;

-- step 3, 2 page
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, r
FROM (
           SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
                      file1, file1saved, thumb1, size1, map, youtube, rownum as r
           FROM (
                     SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
                                file1, file1saved, thumb1, size1, map, youtube
                     FROM burgerpost
                     WHERE burgerno=1 AND (title LIKE '%단풍%' OR content LIKE '%단풍%' OR word LIKE '%단풍%')
                     ORDER BY postno DESC
           )          
)
WHERE r >= 4 AND r <= 6;

-- 대소문자를 처리하는 페이징 쿼리
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, r
FROM (
           SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
                      file1, file1saved, thumb1, size1, map, youtube, rownum as r
           FROM (
                     SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
                                file1, file1saved, thumb1, size1, map, youtube
                     FROM burgerpost
                     WHERE burgerno=1 AND (UPPER(title) LIKE '%' || UPPER('단풍') || '%' 
                                         OR UPPER(content) LIKE '%' || UPPER('단풍') || '%' 
                                         OR UPPER(word) LIKE '%' || UPPER('단풍') || '%')
                     ORDER BY postno DESC
           )          
)
WHERE r >= 1 AND r <= 3;

-- ----------------------------------------------------------------------------
-- 조회
-- ----------------------------------------------------------------------------
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, passwd, word, rdate,
           file1, file1saved, thumb1, size1, map, youtube
FROM burgerpost
WHERE postno = 1;

-- ----------------------------------------------------------------------------
-- 다음 지도, MAP, 먼저 레코드가 등록되어 있어야함.
-- map                                   VARCHAR2(1000)         NULL ,
-- ----------------------------------------------------------------------------
-- MAP 등록/수정
UPDATE burgerpost SET map='버거킹 종로점' WHERE postno=1;

-- MAP 삭제
UPDATE burgerpost SET map='' WHERE postno=1;

commit;

-- ----------------------------------------------------------------------------
-- Youtube, 먼저 레코드가 등록되어 있어야함.
-- youtube                                   VARCHAR2(1000)         NULL ,
-- ----------------------------------------------------------------------------
-- youtube 등록/수정
UPDATE burgerpost SET youtube='Youtube 스크립트' WHERE postno=1;

-- youtube 삭제
UPDATE burgerpost SET youtube='' WHERE postno=1;

commit;

-- 패스워드 검사, id="password_check"
SELECT COUNT(*) as readcnt 
FROM burgerpost
WHERE postno=1 AND passwd='123';

-- 텍스트 수정: 예외 컬럼: 추천수, 조회수, 댓글 수
UPDATE burgerpost
SET title='기차를 타고', content='계획없이 여행 출발',  word='나,기차,생각' 
WHERE postno = 2;

-- ERROR, " 사용 에러
UPDATE burgerpost
SET title='기차를 타고', content="계획없이 '여행' 출발",  word='나,기차,생각'
WHERE postno = 1;

-- ERROR, \' 에러
UPDATE burgerpost
SET title='기차를 타고', content='계획없이 \'여행\' 출발',  word='나,기차,생각'
WHERE postno = 1;

-- SUCCESS, '' 한번 ' 출력됨.
UPDATE burgerpost
SET title='기차를 타고', content='계획없이 ''여행'' 출발',  word='나,기차,생각'
WHERE postno = 1;

-- SUCCESS
UPDATE burgerpost
SET title='기차를 타고', content='계획없이 "여행" 출발',  word='나,기차,생각'
WHERE postno = 1;

commit;

-- 파일 수정
UPDATE burgerpost
SET file1='train.jpg', file1saved='train.jpg', thumb1='train_t.jpg', size1=5000
WHERE postno = 1;

-- 삭제
DELETE FROM burgerpost
WHERE postno = 42;

commit;

DELETE FROM burgerpost
WHERE postno >= 7;

commit;

-- 추천
UPDATE burgerpost
SET recomcnt = recomcnt + 1
WHERE postno = 1;

-- burgerno FK 특정 그룹에 속한 레코드 갯수 산출
SELECT COUNT(*) as readcnt 
FROM burgerpost 
WHERE burgerno=1;

-- memberno FK 특정 관리자에 속한 레코드 갯수 산출
SELECT COUNT(*) as readcnt 
FROM burgerpost 
WHERE memberno=1;

-- burgerno FK 특정 그룹에 속한 레코드 모두 삭제
DELETE FROM burgerpost
WHERE burgerno=1;

-- memberno FK 특정 관리자에 속한 레코드 모두 삭제
DELETE FROM burgerpost
WHERE memberno=1;

commit;

-- 다수의 카테고리에 속한 레코드 갯수 산출: IN
SELECT COUNT(*) as readcnt
FROM burgerpost
WHERE burgerno IN(1,2,3);

-- 다수의 카테고리에 속한 레코드 모두 삭제: IN
SELECT postno, memberno, burgerno, title
FROM burgerpost
WHERE burgerno IN(1,2,3);

postNO    ADMINNO     burgerNO TITLE                                                                                                                                                                                                                                                                                                       
---------- ---------- ---------- ------------------------
         3             1                   1           인터스텔라                                                                                                                                                                                                                                                                                                  
         4             1                   2           드라마                                                                                                                                                                                                                                                                                                      
         5             1                   3           컨저링                                                                                                                                                                                                                                                                                                      
         6             1                   1           마션       
         
SELECT postno, memberno, burgerno, title
FROM burgerpost
WHERE burgerno IN('1','2','3');

postNO    ADMINNO     burgerNO TITLE                                                                                                                                                                                                                                                                                                       
---------- ---------- ---------- ------------------------
         3             1                   1           인터스텔라                                                                                                                                                                                                                                                                                                  
         4             1                   2           드라마                                                                                                                                                                                                                                                                                                      
         5             1                   3           컨저링                                                                                                                                                                                                                                                                                                      
         6             1                   1           마션       

-- ----------------------------------------------------------------------------------------------------
-- burger + burgerpost INNER JOIN
-- ----------------------------------------------------------------------------------------------------
-- 모든글
SELECT c.name,
       t.postno, t.memberno, t.burgerno, t.title, t.content, t.recomcnt, t.readcnt, t.replycnt, t.word, t.rdate,
       t.file1, t.file1saved, t.thumb1, t.size1, t.map, t.youtube
FROM burger c, burgerpost t
WHERE c.burgerno = t.burgerno
ORDER BY t.postno DESC;

-- burgerpost, member INNER JOIN
SELECT t.postno, t.memberno, t.burgerno, t.title, t.content, t.recomcnt, t.readcnt, t.replycnt, t.word, t.rdate,
       t.file1, t.file1saved, t.thumb1, t.size1, t.map, t.youtube,
       a.mname
FROM member a, burgerpost t
WHERE a.memberno = t.memberno
ORDER BY t.postno DESC;

SELECT t.postno, t.memberno, t.burgerno, t.title, t.content, t.recomcnt, t.readcnt, t.replycnt, t.word, t.rdate,
       t.file1, t.file1saved, t.thumb1, t.size1, t.map, t.youtube,
       a.mname
FROM member a INNER JOIN burgerpost t ON a.memberno = t.memberno
ORDER BY t.postno DESC;

-- ----------------------------------------------------------------------------------------------------
-- View + paging
-- ----------------------------------------------------------------------------------------------------
CREATE OR REPLACE VIEW vpost
AS
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, word, rdate,
        file1, file1saved, thumb1, size1, map, youtube
FROM burgerpost
ORDER BY postno DESC;
                     
-- 1 page
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
       file1, file1saved, thumb1, size1, map, youtube, r
FROM (
     SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
            file1, file1saved, thumb1, size1, map, youtube, rownum as r
     FROM vpost -- View
     WHERE burgerno=14 AND (title LIKE '%야경%' OR content LIKE '%야경%' OR word LIKE '%야경%')
)
WHERE r >= 1 AND r <= 3;

-- 2 page
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
       file1, file1saved, thumb1, size1, map, youtube, r
FROM (
     SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
            file1, file1saved, thumb1, size1, map, youtube, rownum as r
     FROM vpost -- View
     WHERE burgerno=14 AND (title LIKE '%야경%' OR content LIKE '%야경%' OR word LIKE '%야경%')
)
WHERE r >= 4 AND r <= 6;


-- ----------------------------------------------------------------------------------------------------
-- 관심 카테고리의 좋아요(recomcnt) 기준, 1번 회원이 1번 카테고리를 추천 받는 경우, 추천 상품이 7건일 경우
-- ----------------------------------------------------------------------------------------------------
SELECT postno, memberno, burgerno, title, thumb1, r
FROM (
           SELECT postno, memberno, burgerno, title, thumb1, rownum as r
           FROM (
                     SELECT postno, memberno, burgerno, title, thumb1
                     FROM burgerpost
                     WHERE burgerno=1
                     ORDER BY recomcnt DESC
           )          
)
WHERE r >= 1 AND r <= 7;

-- ----------------------------------------------------------------------------------------------------
-- 관심 카테고리의 평점(score) 기준, 1번 회원이 1번 카테고리를 추천 받는 경우, 추천 상품이 7건일 경우
-- ----------------------------------------------------------------------------------------------------
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, r
FROM (
           SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
                      file1, file1saved, thumb1, size1, map, youtube, rownum as r
           FROM (
                     SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
                                file1, file1saved, thumb1, size1, map, youtube
                     FROM burgerpost
                     WHERE burgerno=1
                     ORDER BY score DESC
           )          
)
WHERE r >= 1 AND r <= 7;

-- ----------------------------------------------------------------------------------------------------
-- 관심 카테고리의 최신 상품 기준, 1번 회원이 1번 카테고리를 추천 받는 경우, 추천 상품이 7건일 경우
-- ----------------------------------------------------------------------------------------------------
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, r
FROM (
           SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
                      file1, file1saved, thumb1, size1, map, youtube, rownum as r
           FROM (
                     SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
                                file1, file1saved, thumb1, size1, map, youtube
                     FROM burgerpost
                     WHERE burgerno=1
                     ORDER BY rdate DESC
           )          
)
WHERE r >= 1 AND r <= 7;

-- ----------------------------------------------------------------------------------------------------
-- 관심 카테고리의 조회수 높은 상품기준, 1번 회원이 1번 카테고리를 추천 받는 경우, 추천 상품이 7건일 경우
-- ----------------------------------------------------------------------------------------------------
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, r
FROM (
           SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
                      file1, file1saved, thumb1, size1, map, youtube, rownum as r
           FROM (
                     SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
                                file1, file1saved, thumb1, size1, map, youtube
                     FROM burgerpost
                     WHERE burgerno=1
                     ORDER BY readcnt DESC
           )          
)
WHERE r >= 1 AND r <= 7;

-- ----------------------------------------------------------------------------------------------------
-- 관심 카테고리의 낮은 가격 상품 추천, 1번 회원이 1번 카테고리를 추천 받는 경우, 추천 상품이 7건일 경우
-- ----------------------------------------------------------------------------------------------------
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, r
FROM (
           SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
                      file1, file1saved, thumb1, size1, map, youtube, rownum as r
           FROM (
                     SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
                                file1, file1saved, thumb1, size1, map, youtube
                     FROM burgerpost
                     WHERE burgerno=1
                     ORDER BY price ASC
           )          
)
WHERE r >= 1 AND r <= 7;

-- ----------------------------------------------------------------------------------------------------
-- 관심 카테고리의 높은 가격 상품 추천, 1번 회원이 1번 카테고리를 추천 받는 경우, 추천 상품이 7건일 경우
-- ----------------------------------------------------------------------------------------------------
SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, r
FROM (
           SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
                      file1, file1saved, thumb1, size1, map, youtube, rownum as r
           FROM (
                     SELECT postno, memberno, burgerno, title, content, recomcnt, readcnt, replycnt, rdate,
                                file1, file1saved, thumb1, size1, map, youtube
                     FROM burgerpost
                     WHERE burgerno=1
                     ORDER BY price DESC
           )          
)
WHERE r >= 1 AND r <= 7;

-----------------------------------------------------------
-- FK burgerno 컬럼에 대응하는 필수 SQL
-----------------------------------------------------------
-- 특정 카테고리에 속한 레코드 갯수를 리턴
SELECT COUNT(*) as readcnt 
FROM burgerpost 
WHERE burgerno=1;
  
-- 특정 카테고리에 속한 모든 레코드 삭제
DELETE FROM burgerpost
WHERE burgerno=1;

-----------------------------------------------------------
-- FK memberno 컬럼에 대응하는 필수 SQL
-----------------------------------------------------------
-- 특정 회원에 속한 레코드 갯수를 리턴
SELECT COUNT(*) as readcnt 
FROM burgerpost 
WHERE memberno=1;
  
-- 특정 회원에 속한 모든 레코드 삭제
DELETE FROM burgerpost
WHERE memberno=1;

1) 댓글수 증가
UPDATE burgerpost
SET replycnt = replycnt + 1
WHERE postno = 3;

2) 댓글수 감소
UPDATE burgerpost
SET replycnt = replycnt - 1
WHERE postno = 3;   

commit;