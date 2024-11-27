package dev.mvc.burger;

import java.util.ArrayList;
import java.util.Map;

import dev.mvc.burger.BurgerVO;

public interface BurgerProcInter {
/** 
 * <pre>
 * 등록
 * </pre>
 * @param burgerVO
 * @return
 */
  public int create(BurgerVO burgerVO);
  
  /**
   * 전체 목록
   * @return
   */
  public ArrayList<BurgerVO> list_all();
  
  /**
   * 조회
   * @param burgerno
   * @return
   */
  public BurgerVO read(Integer burgerno);
  
  /**
   * 수정
   * @param burgerVO 수정할 내용
   * @return 수정된 레코드 개수
   */
  public int update(BurgerVO burgerVO);
  
  /**
   * 삭제
   * @param burgerno 삭제할 레코드 PK
   * @return 삭제된 레코드 개수
   */
  public int delete(int burgerno);
  
  /**
   * 삭제
   * @param cateno 삭제할 레코드 PK
   * @return 삭제된 레코드 개수
   */
  public int delete_contents(int burgerno);
  
  /**
   * 하위 게시물 전부 삭제 시 삭제한 개수만큼 '--'에서 차감
   * @param burgerno
   * @return
   */
  public int sync_cnt(int burgerno);
  
  /**
   * 우선 순위 높임 10등 -> 1등
   * @param burgerno
   * @return
   */
  public int update_seqno_forward(int burgerno);
  
  /**
   * 우선 순위 낮춤 1등 -> 10등
   * @param burgerno
   * @return
   */
  public int update_seqno_backward(int burgerno);
  
  /**
   * 공개 설정
   * @param burgerno
   * @return
   */
  public int update_isvisible_t(int burgerno);
  
  /**
   * 비공개 설정
   * @param burgerno
   * @return
   */
  public int update_isvisible_f(int burgerno);
  
  /**
   * <!-- 숨긴 종류를 제외하고 접속자에게 공개할 종류 출력 -->
   * SQL -> burgerVO 객체 레코드 수만큼 생성 -> ArrayList<burgerVO> 객체 생성되어 burgerDAOInter로 리턴
   * select id="list_all_brand_y" resultType="dev.mvc.burger.burgerVO"
   * @return
   */
  public ArrayList<BurgerVO> list_all_brand_y();
  
  /**
   * <!-- 숨긴 종류를 제외하고 접속자에게 공개할 게시물 출력 -->
   * SQL -> burgerVO 객체 레코드 수만큼 생성 -> ArrayList<burgerVO> 객체 생성되어 burgerDAOInter로 리턴
   * select id="list_all_brand_y" resultType="dev.mvc.burger.burgerVO"
   * @return
   */
  public ArrayList<BurgerVO> list_all_brand_y(String brand);
  
  /**
   * 화면 상단 메뉴
   * @return
   */
  public ArrayList<BurgerVOMenu> menu();
  
  /**
   * 종류 목록
   * @return
   */
  public ArrayList<String> brandset();
  
  /**
   * 검색 목록
   * @param word
   * @return
   */
  public ArrayList<BurgerVO> list_search(String word);
  
  /**
   * 검색 개수
   * @param word
   * @return
   */
  public int list_search_count(String word);
  
  /**
   * 검색 + 페이징 목록
   * select id="list_search_paging" resultType="dev.mvc.burger.burgerVO" parameterType="Map"
   * @param word 검색어
   * @param now_page 현재 페이지, 시작 페이지 번호 1★
   * @param record_per_page 페이지당 출력할 레코드 수
   * @return
   */
  public ArrayList<BurgerVO> list_search_paging(String word, int now_page, int record_per_page);
  
  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param now_page  현재 페이지
   * @param word 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */
    String pagingBox(int now_page, String word, String list_file, int search_count, int record_per_page,
      int page_per_block);
}
