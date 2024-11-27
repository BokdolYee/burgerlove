package dev.mvc.burger;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

import dev.mvc.burger.BurgerVO;
import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/burger")
public class BurgerCont {
  @Autowired
  @Qualifier("dev.mvc.burger.BurgerProc")
  private BurgerProc burgerProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;
  
  /** 페이징 목록 주소 */
  private String list_file_name = "/burger/list_search";
  
  public BurgerCont() {
    System.out.println("-> BurgerCont created");
  }
  
//  @GetMapping(value="/create")  // http://localhost:9092/burger/create
//  @ResponseBody //html 파일 내용임
//  public String create() {
//    return "<h2>Create test</h2>";
//  }
//  @GetMapping(value="/create")
//  public String create() {
//    return "/burger/create";
//  }
  
  @GetMapping(value="/create")
  public String create(Model model) {
    BurgerVO burgerVO = new BurgerVO();
    model.addAttribute("burgerVO", burgerVO);
    
    burgerVO.setName("와퍼!");  //Form으로 초기값 전달
    return "/burger/create";  // /templates/burger/create.html
  }
  /**
   * 등록 처리, http://localhost:9092/burger/create
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @param burgerVO Form 태그 값 -> 검증 -> burgerVO 자동 저장, request.getParameter() 자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value="/create")
  public String create(Model model, @Valid BurgerVO burgerVO, BindingResult bindResult) {
    // System.out.println("-> create burger");
    if(bindResult.hasErrors() == true) {  //에러가 있으면 폼으로 돌아갈 것
      System.out.println("에러 발생");
      return "/burger/create";  // /templates/burger/create.html
    }
    
    
//    System.out.println(burgerVO.getname());
//    System.out.println(burgerVO.getIsvisible());
    burgerVO.setBrand(burgerVO.getBrand().trim());
    burgerVO.setName(burgerVO.getName().trim());
    
    int cnt = this.burgerProc.create(burgerVO);
    System.out.println("cnt: " + cnt);
    
    if(cnt == 1) {
//      model.addAttribute("code", "create_success");
//      model.addAttribute("name", burgerVO.getname());
      return "redirect:/burger/list_search"; // @GetMapping(value="/list_search")
    } else {
      model.addAttribute("code", "create_fail");
    }
    
    model.addAttribute("cnt: ", cnt);
    return "/burger/msg"; // /templates/burger/msg.html
  }
  
  /**
   * 등록 폼 및 목록
   * @param model
   * @return
   */
  @GetMapping(value="/list_all")
  public String list_all(Model model) {
    BurgerVO burgerVO = new BurgerVO();
    model.addAttribute("burgerVO", burgerVO);
    
    //종류 목록
    ArrayList<String> list_brand = this.burgerProc.brandset();
    burgerVO.setBrand(String.join("/", list_brand));
    
    ArrayList<BurgerVO> list = this.burgerProc.list_all();
    model.addAttribute("list", list);
    
    //ArrayList<burgerVO> menu = this.burgerProc.list_all_brand_y();
    //model.addAttribute("menu", menu);
    
//  ArrayList<burgerVO> menu = this.burgerProc.list_all_brand_y();
//  model.addAttribute("menu", menu);
    ArrayList<BurgerVOMenu> menu = this.burgerProc.menu();
    model.addAttribute("menu", menu);
    
    return "/burger/list_all";  // /templates/burger/list_all.html
  }
  
  /**
   * 조회
   * http://localhost:9092/burger/read/1
   */
  @GetMapping(value="/read/{burgerno}")
  public String read(Model model, @PathVariable("burgerno")Integer burgerno,
      @RequestParam(name="word", defaultValue = "")String word,
      @RequestParam(name="now_page", defaultValue = "1")int now_page) {
    BurgerVO burgerVO = this.burgerProc.read(burgerno);
    model.addAttribute("burgerVO", burgerVO);
    
    //ArrayList<burgerVO> list = this.burgerProc.list_search(word);
    ArrayList<BurgerVO> list = this.burgerProc.list_search_paging(word, now_page, this.record_per_page);
    model.addAttribute("list", list);
    
    model.addAttribute("word", word);
    
    //---------------------------------------------------------------------
    // 페이징 번호 목록 생성
    //---------------------------------------------------------------------
    int search_count = this.burgerProc.list_search_count(word);
    String paging = this.burgerProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page, this.page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);
    
    //일련 번호 생성: 레코드 갯수 - ((현재 페이지 수 -1) * 페이지 당 레코드 수)
    int no = search_count - ((now_page - 1) * this.record_per_page);
    model.addAttribute("no", no);
    //------------------------------------------------------------------
    
    return "/burger/read";
  }
  
  /**
   * 수정폼
   * http://localhost:9092/burger/update/1
   */
  @GetMapping(value="/update/{burgerno}")
  public String update(Model model, 
      HttpSession session,
      @PathVariable("burgerno")Integer burgerno,
      @RequestParam(name="word", defaultValue = "")String word,
      @RequestParam(name="now_page", defaultValue = "1")int now_page) {
    if (this.memberProc.isMemberAdmin(session)) {
      BurgerVO burgerVO = this.burgerProc.read(burgerno);
      model.addAttribute("burgerVO", burgerVO);
      
      //ArrayList<burgerVO> list = this.burgerProc.list_search(word);
      ArrayList<BurgerVO> list = this.burgerProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);
      
      ArrayList<BurgerVOMenu> menu = this.burgerProc.menu();
      model.addAttribute("menu", menu);
      
      //브랜드 목록
      ArrayList<String> list_brand = this.burgerProc.brandset();
      model.addAttribute("list_brand", String.join("/", list_brand));
      
      model.addAttribute("word", word);
      
      //---------------------------------------------------------------------
      // 페이징 번호 목록 생성
      //---------------------------------------------------------------------
      int search_count = this.burgerProc.list_search_count(word);
      String paging = this.burgerProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      
      //일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      //----------------------------------------------------------------------
      
      return "/burger/update";  // /templates/burger/update.html
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
  }
  
  /**
   * 수정 처리, http://localhost:9092/burger/update
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @param burgerVO Form 태그 값 -> 검증 -> burgerVO 자동 저장, request.getParameter() 자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value="/update")
  public String update(Model model, 
      HttpSession session,
      @Valid @ModelAttribute("burgerVO") BurgerVO burgerVO, 
      BindingResult bindResult,
      @RequestParam(name="word", defaultValue = "")String word,
      @RequestParam(name="now_page", defaultValue = "1")int now_page,
      RedirectAttributes ra) {
    // System.out.println("-> update burger");
    if (this.memberProc.isMemberAdmin(session)) {
      if(bindResult.hasErrors() == true) {  //에러가 있으면 폼으로 돌아갈 것
        System.out.println("에러 발생");
        return "/burger/update";  // /templates/burger/update.html
      }
      
      
//      System.out.println(burgerVO.getname());
//      System.out.println(burgerVO.getIsvisible());
      
      burgerVO.setBrand(burgerVO.getBrand().trim());
      burgerVO.setName(burgerVO.getName().trim());
      
      int cnt = this.burgerProc.update(burgerVO);
      System.out.println("cnt: " + cnt);
      
      if(cnt == 1) {
        //model.addAttribute("code", "update_success");
        //model.addAttribute("name", burgerVO.getName());
        //model.addAttribute("brand", burgerVO.getBrand());
        
        ra.addAttribute("word", word);
        ra.addAttribute("now_page", now_page);
        
        return "redirect:/burger/update/" + burgerVO.getBurgerno(); // @GetMapping(value="/update/{burgerno}")
      } else {
        model.addAttribute("code", "update_fail");
      }
      
      model.addAttribute("cnt: ", cnt);
      
      // --------------------------------------------------------------------------------------
      // 페이지 번호 목록 생성
      // --------------------------------------------------------------------------------------
      int search_count = this.burgerProc.list_search_count(word);
      String paging = this.burgerProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
          this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      // --------------------------------------------------------------------------------------
      
      return "/burger/msg"; // /templates/burger/msg.html
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
  }
  
  /**
   * 삭제 폼
   * http://localhost:9092/burger/delete/1
   */
  @GetMapping(value="/delete/{burgerno}")
  public String delete(Model model, 
      HttpSession session,
      @PathVariable("burgerno")Integer burgerno,
      @RequestParam(name="word", defaultValue = "")String word,
      @RequestParam(name="now_page", defaultValue = "1")int now_page) {
    if (this.memberProc.isMemberAdmin(session)) {
      BurgerVO burgerVO = this.burgerProc.read(burgerno);
      model.addAttribute("burgerVO", burgerVO);
      
      ArrayList<BurgerVO> list = this.burgerProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);
      
      ArrayList<BurgerVOMenu> menu = this.burgerProc.menu();
      model.addAttribute("menu", menu);
      
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      
      //---------------------------------------------------------------------
      // 페이징 번호 목록 생성
      //---------------------------------------------------------------------
      int search_count = this.burgerProc.list_search_count(word);
      String paging = this.burgerProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      
      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      // --------------------------------------------------------------------------------------
      
      return "/burger/delete";  // /templates/burger/delete.html
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
  }
  
  /**
   * 삭제 처리, http://localhost:9092/burger/delete
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @param burgerVO Form 태그 값 -> 검증 -> burgerVO 자동 저장, request.getParameter() 자동 실행
   * @return
   */
  @PostMapping(value="/delete")
  public String delete(Model model, 
      HttpSession session,
      @RequestParam(name="burgerno", defaultValue="0")int burgerno,
      @RequestParam(name="word", defaultValue = "")String word,
      @RequestParam(name="now_page", defaultValue = "1")int now_page,
      RedirectAttributes ra) {
    if (this.memberProc.isMemberAdmin(session)) {
      BurgerVO burgerVO = this.burgerProc.read(burgerno); //삭제 전에 삭제할 레코드 결과를 조회
      model.addAttribute("burgerVO", burgerVO);
      
      this.burgerProc.delete_contents(burgerno);
      this.burgerProc.sync_cnt(burgerno);
      
      int cnt = this.burgerProc.delete(burgerno);
      System.out.println("cnt: " + cnt);
      
      if(cnt == 1) {
//        model.addAttribute("code", "delete_success");
//        model.addAttribute("name", burgerVO.getname());
//        model.addAttribute("brand", burgerVO.getBrand());
        ra.addAttribute("word", word);
        
        // ----------------------------------------------------------------------------------------------------------
        // 마지막 페이지에서 모든 레코드가 삭제되면 페이지수를 1 감소 시켜야함.
        int search_cnt = this.burgerProc.list_search_count(word);
        if (search_cnt % this.record_per_page == 0) {
          now_page = now_page - 1;
          if (now_page < 1) {
            now_page = 1; // 최소 시작 페이지
          }
        }
        // ----------------------------------------------------------------------------------------------------------
        
        ra.addAttribute("now_page", now_page);
        
        return "redirect:/burger/list_search";
      } else {
        model.addAttribute("code", "delete_fail");
      }
      
      model.addAttribute("cnt: ", cnt);
      
      return "/burger/msg"; // /templates/burger/msg.html
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
  }
  
  /**
   * 우선 순위 높임 10등 -> 1등, http://localhost:9092/burger/update_seqno_forward/1
   * @param model Controller -> thymeleaf HTML로 데이터 전송에 사용
   * @param burgerVO Form 태그 값 -> 검증 -> burgerVO 자동 저장, request.getParameter() 자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @GetMapping(value="/update_seqno_forward/{burgerno}")
  public String update_seqno_forward(Model model, @PathVariable("burgerno")Integer burgerno,
      @RequestParam(name="word", defaultValue = "")String word,
      @RequestParam(name="now_page", defaultValue = "1")int now_page,
      RedirectAttributes ra) {
    this.burgerProc.update_seqno_forward(burgerno);
    
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    
    return "redirect:/burger/list_search";
  }
  
  /**
   * 우선 순위 낮춤 1등 -> 10등, http://localhost:9092/burger/update_seqno_backward/1
   * @param model Controller -> thymeleaf HTML로 데이터 전송에 사용
   * @param burgerVO Form 태그 값 -> 검증 -> burgerVO 자동 저장, request.getParameter() 자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @GetMapping(value="/update_seqno_backward/{burgerno}")
  public String update_seqno_backward(Model model, @PathVariable("burgerno")Integer burgerno,
      @RequestParam(name="word", defaultValue = "")String word,
      @RequestParam(name="now_page", defaultValue = "1")int now_page,
      RedirectAttributes ra) {
    this.burgerProc.update_seqno_backward(burgerno);
    
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    
    return "redirect:/burger/list_search";
  }
  
  /**
   * 게시물 공개 설정, http://localhost:9092/burger/update_isvisible_t/1
   * @param model Controller -> thymeleaf HTML로 데이터 전송에 사용
   * @param burgerVO Form 태그 값 -> 검증 -> burgerVO 자동 저장, request.getParameter() 자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @GetMapping(value="update_isvisible_t/{burgerno}")
  public String update_isvisible_t(Model model, 
      HttpSession session,
      @PathVariable("burgerno")Integer burgerno,
      @RequestParam(name="word", defaultValue = "")String word,
      @RequestParam(name="now_page", defaultValue = "1")int now_page,
      RedirectAttributes ra) {
    if (this.memberProc.isMemberAdmin(session)) {
      this.burgerProc.update_isvisible_t(burgerno);
      
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      
      return "redirect:/burger/list_search";
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
  }
  
  /**
   * 게시물 비공개 설정, http://localhost:9092/burger/update_isvisible_f/1
   * @param model Controller -> thymeleaf HTML로 데이터 전송에 사용
   * @param burgerVO Form 태그 값 -> 검증 -> burgerVO 자동 저장, request.getParameter() 자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @GetMapping(value="update_isvisible_f/{burgerno}")
  public String update_isvisible_f(Model model, 
      HttpSession session,
      @PathVariable("burgerno")Integer burgerno,
      @RequestParam(name="word", defaultValue = "")String word,
      @RequestParam(name="now_page", defaultValue = "1")int now_page,
      RedirectAttributes ra) {
    if (this.memberProc.isMemberAdmin(session)) {
      this.burgerProc.update_isvisible_f(burgerno);
      
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      
      return "redirect:/burger/list_search";
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
  }
  
///**
//* 등록 폼 및 검색 목록
//* http://localhost:9092/burger/list_search
//* http://localhost:9092/burger/list_search?word=
//* http://localhost:9092/burger/list_search?word=치킨
//* @param model
//* @return
//*/
//@GetMapping(value="/list_search")
//public String list_search(Model model, @RequestParam(name="word", defaultValue = "")String word) {
// burgerVO burgerVO = new burgerVO();
// //burgerVO.setname("게시물 제목을 입력하세요.");  //Form으로 초기값 전달
// //burgerVO.setbrand("종류");
// // 종류 목록
// ArrayList<String> list_brand = this.burgerProc.brandset();
// burgerVO.setbrand(String.join("/", list_brand));
// 
// model.addAttribute("burgerVO", burgerVO);
// 
// word = Tool.checkNull(word);
// 
// ArrayList<burgerVO> list = this.burgerProc.list_search(word);
// model.addAttribute("list", list);
// 
// 
////ArrayList<burgerVO> menu = this.burgerProc.list_all_brand_y();
////model.addAttribute("menu", menu);
// ArrayList<burgerVOMenu> menu = this.burgerProc.menu();
// model.addAttribute("menu", menu);
// 
// int search_cnt = this.burgerProc.list_search_count(word);
// model.addAttribute("search_cnt", search_cnt);
// 
// model.addAttribute("word", word);
// 
// return "/burger/list_search";  // /templates/burger/list_search.html
//}
  
  /**
   * 등록 폼 및 검색 목록 + 페이징
   * http://localhost:9092/burger/list_search
   * http://localhost:9092/burger/list_search?word=&now_page
   * http://localhost:9092/burger/list_search?word=치킨&now_page=1
   * @param model
   * @return
   */
  @GetMapping(value="/list_search")
  public String list_search_paging(Model model, 
      HttpSession session,
      @RequestParam(name="word", defaultValue = "")String word,
      @RequestParam(name="now_page", defaultValue = "1")int now_page) {
    if (this.memberProc.isMemberAdmin(session)) {
      BurgerVO burgerVO = new BurgerVO();
      model.addAttribute("burgerVO", burgerVO);
      
      //종류 목록
      ArrayList<String> list_brand = this.burgerProc.brandset();
      burgerVO.setBrand(String.join("/", list_brand));
      
      word = Tool.checkNull(word);
      
      ArrayList<BurgerVO> list = this.burgerProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);
      
      //ArrayList<burgerVO> menu = this.burgerProc.list_all_brand_y();
      //model.addAttribute("menu", menu);
      
//    ArrayList<burgerVO> menu = this.burgerProc.list_all_brand_y();
//    model.addAttribute("menu", menu);
      ArrayList<BurgerVOMenu> menu = this.burgerProc.menu();
      model.addAttribute("menu", menu);
      
      int search_cnt = this.burgerProc.list_search_count(word);
      model.addAttribute("search_cnt", search_cnt);
      
      model.addAttribute("word", word);
      
      //---------------------------------------------------------------------
      // 페이징 번호 목록 생성
      //---------------------------------------------------------------------
      int search_count = this.burgerProc.list_search_count(word);
      String paging = this.burgerProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      //----------------------------------------------------------------------
      
      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      
      return "/burger/list_search";  // /templates/burger/list_search.html
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
    
    
  }
}
