package dev.mvc.burgerpost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 개발자가 구현합니다.
 * @author soldesk
 *
 */
public interface BurgerpostProcInter {
  /**
   * 등록
   * @param BurgerpostVO
   * @return
   */
  public int create(BurgerpostVO burgerpostVO);

  /**
   * 모든 카테고리의 등록된 글목록
   * @return
   */
  public ArrayList<BurgerpostVO> list_all();
 
  /**
   * 카테고리별 등록된 글 목록
   * @param burgerno
   * @return
   */
  public ArrayList<BurgerpostVO> list_by_burgerno(int burgerno);
  
  /**
   * 조회
   * @param postno
   * @return
   */
  public BurgerpostVO read(int postno);
  
  /**
   * map 등록, 수정, 삭제
   * @param map
   * @return 수정된 레코드 갯수
   */
  public int map(HashMap<String, Object> map);

  /**
   * youtube 등록, 수정, 삭제
   * @param youtube
   * @return 수정된 레코드 갯수
   */
  public int youtube(HashMap<String, Object> map);
  
  /**
   * 카테고리별 검색 목록
   * @param map
   * @return
   */
  public ArrayList<BurgerpostVO> list_by_burgerno_search(HashMap<String, Object> hashMap);
  
  /**
   * 카테고리별 검색된 레코드 갯수
   * @param map
   * @return
   */
  public int list_by_burgerno_search_count(HashMap<String, Object> hashMap);
  
  /**
   * 카테고리별 검색 목록 + 페이징
   * @param BurgerpostVO
   * @return
   */
  public ArrayList<BurgerpostVO> list_by_burgerno_search_paging(HashMap<String, Object> map);
  
  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param burgerno 카테고리 번호
   * @param now_page 현재 페이지
   * @param word 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */ 
  public String pagingBox(int burgerno, int now_page, String word, String list_file, int search_count, 
                                      int record_per_page, int page_per_block);   

  /**
   * 패스워드 검사
   * @param hashMap
   * @return
   */
  public int password_check(HashMap<String, Object> hashMap);
  
  /**
   * 글 정보 수정
   * @param BurgerpostVO
   * @return 처리된 레코드 갯수
   */
  public int update_text(BurgerpostVO burgerpostVO);
  
  /**
   * 파일 정보 수정
   * @param BurgerpostVO
   * @return 처리된 레코드 갯수
   */
  public int update_file(BurgerpostVO burgerpostVO);
  
  /**
   * 삭제
   * @param postno
   * @return 삭제된 레코드 갯수
   */
  public int delete(int postno);
  
  /**
   * FK burgerno 값이 같은 레코드 갯수 산출
   * @param burgerno
   * @return
   */
  public int count_by_burgerno(int burgerno);
  
  /**
   * 특정 카테고리에 속한 모든 레코드 삭제
   * @param burgerno
   * @return 삭제된 레코드 갯수
   */
  public int delete_by_burgerno(int burgerno);

  /**
   * FK memberno 값이 같은 레코드 갯수 산출
   * @param memberno
   * @return
   */
  public int count_by_memberno(int memberno);
 
  /**
   * 특정 카테고리에 속한 모든 레코드 삭제
   * @param memberno
   * @return 삭제된 레코드 갯수
   */
  public int delete_by_memberno(int memberno);
  
  /**
   * 글 수 증가
   * @param 
   * @return
   */ 
  public int increaseReplycnt(int postno);
 
  /**
   * 글 수 감소
   * @param 
   * @return
   */   
  public int decreaseReplycnt(int postno);
  
  /**
   * 브랜드 자료수 증가
   * @param burgerno
   * @return
   */
  public int inc_cnt_brand(int burgerno);
  
  /**
   * 브랜드 자료수 감소
   * @param burgerno
   * @return
   */
  public int dec_cnt_brand(int burgerno);
  
  /**
   * 버거 자료수 증가
   * @param burgerno
   * @return
   */
  public int inc_cnt_burger(int burgerno);
  
  /**
   * 버거 자료수 감소
   * @param burgerno
   * @return
   */
  public int dec_cnt_burger(int burgerno);
}