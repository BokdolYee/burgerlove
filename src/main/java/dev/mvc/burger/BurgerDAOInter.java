package dev.mvc.burger;

import java.util.ArrayList;
import java.util.Map;

import dev.mvc.burger.BurgerVO;

public interface BurgerDAOInter {
  /** 
   * <pre> <- 줄바꿈 보존
   * MyBATIS: insert id="create" parameterType="dev.mvc.burger.burgerVO">
   * insert: int를 리턴, 등록한 레코드 갯루를 리턴
   * id ="create" : 메소드명으로 사용
   * parameterType ="dev.mvc.burger.burgerVO": 메소드의 파라미터
   * Spring Boot 자동으로 구현
   * </pre>
   * @param burgerVO
   * @return
   */
   public int create(BurgerVO burgerVO);
   
   /**
    * 전체 목록
    * SQL -> burgerVO 객체 레코드 수만큼 생성 -> ArrayList<burgerVO> 객체 생성되어 burgerDAOInter로 리턴
    * select id="list_all" resultType="dev.mvc.burger.burgerVO"
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
    * select id="list_all_name_y" resultType="dev.mvc.burger.burgerVO"
    * @return
    */
   public ArrayList<BurgerVO> list_all_name_y(String brand);
   
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
   public ArrayList<BurgerVO> list_search_paging(Map<String, Object>map);
}
