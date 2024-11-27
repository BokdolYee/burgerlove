/**********************************/
/* Table Name: 햄버거 */
/**********************************/
DROP TABLE burger;
COMMIT;

CREATE TABLE burger(
    BURGERNO NUMERIC(10) NOT NULL PRIMARY KEY,
    NAME VARCHAR(50) NOT NULL,
    BRAND VARCHAR(20) NOT NULL,
    CNT NUMBER(7) DEFAULT 0 NOT NULL,
    SEQNO NUMBER(5) DEFAULT 1 NOT NULL,
    ISVISIBLE CHAR(1) DEFAULT 'T' NOT NULL,
    RDATE DATE NOT NULL
);


COMMENT ON TABLE burger is '햄버거';
COMMENT ON COLUMN burger.BURGERNO is '햄버거 번호';
COMMENT ON COLUMN burger.NAME is '햄버거 이름';
COMMENT ON COLUMN burger.BRAND is '햄버거 브랜드';
COMMENT ON COLUMN burger.ISVISIBLE is '출력 모드';
COMMENT ON COLUMN burger.RDATE is '작성 시간';
COMMENT ON COLUMN burger.cnt is '자료수';

DROP SEQUENCE BURGER_SEQ;

CREATE SEQUENCE BURGER_SEQ
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

-- CRUD
-- 등록
INSERT INTO burger(burgerno, name, brand, cnt, seqno, isvisible, rdate)
VALUES(burger_seq.nextval, '와퍼', '버거킹', 0, 0, 'T', sysdate);

INSERT INTO burger(burgerno, name, brand, cnt, seqno, isvisible, rdate)
VALUES(burger_seq.nextval, '데리버거', '롯데리아',  0, 0, 'T', sysdate);

INSERT INTO burger(burgerno, name, brand, cnt, seqno, isvisible, rdate)
VALUES(burger_seq.nextval, '싸이버거', '맘스터치',  0, 0, 'T', sysdate);

commit;
-- 전체 조회
SELECT * FROM burger;
  BURGERNO NAME                                               BRAND                I RDATE              
---------- -------------------------------------------------- -------------------- - -------------------
         1 와퍼                                               버거킹               T 2024-10-18 11:44:16
         2 데리버거                                           롯데리아             T 2024-10-18 11:44:16
         3 싸이버거                                           맘스터치             T 2024-10-18 11:44:16

-- 조건 조회
SELECT burgerno, name, rdate FROM burger WHERE burgerno = 1;
  BURGERNO NAME                                               RDATE              
---------- -------------------------------------------------- -------------------
         1 와퍼                                               2024-10-18 11:44:16

-- 수정
UPDATE burger SET name = '와퍼' WHERE burgerno = 1;
SELECT * FROM burger;
 BURGERNO NAME                                               BRAND                I RDATE              
---------- -------------------------------------------------- -------------------- - -------------------
         1 와퍼                                               버거킹               T 2024-10-18 11:44:16
         2 데리버거                                           롯데리아             T 2024-10-18 11:44:16
         3 싸이버거                                           맘스터치             T 2024-10-18 11:44:16
-- 삭제
DELETE FROM burger WHERE burgerno = 3;
SELECT * FROM burger;
    burgerNO name                                          I RDATE              
---------- -------------------------------------------------- - -------------------
         1 9월 1주차 신상 맛도리!                             T 2024-09-03 04:05:30
         2 9월 2주차 신상 맛도리!                             T 2024-09-03 04:05:30
