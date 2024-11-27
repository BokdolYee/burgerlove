package dev.mvc.burgerpost;

import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.burger.BurgerProcInter;
import dev.mvc.burger.BurgerVO;
import dev.mvc.burger.BurgerVOMenu;
import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;

@RequestMapping(value = "/burgerpost")
@Controller
public class BurgerpostCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  @Autowired
  @Qualifier("dev.mvc.burger.BurgerProc") // @Component("dev.mvc.burger.burgerProc")
  private BurgerProcInter burgerProc;

  @Autowired
  @Qualifier("dev.mvc.burgerpost.BurgerpostProc") // @Component("dev.mvc.burgerpost.burgerpostProc")
  private BurgerpostProcInter burgerpostProc;

  public BurgerpostCont() {
    System.out.println("-> BurgerpostCont created.");
  }

  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue = "")String url) {
    ArrayList<BurgerVOMenu> menu = this.burgerProc.menu();
    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }

  // 등록 폼, burgerpost 테이블은 FK로 burgerno를 사용함.
  // http://localhost:9091/burgerpost/create X
  // http://localhost:9091/burgerpost/create?burgerno=1 // burgerno 변수값을 보내는 목적
  // http://localhost:9091/burgerpost/create?burgerno=2
  // http://localhost:9091/burgerpost/create?burgerno=5
  @GetMapping(value = "/create")
  public String create(Model model, @ModelAttribute("burgerpostVO")BurgerpostVO burgerpostVO, 
    @RequestParam(name="burgerno", defaultValue = "0")int burgerno) {
    ArrayList<BurgerVOMenu> menu = this.burgerProc.menu();
    model.addAttribute("menu", menu);

    BurgerVO burgerVO = this.burgerProc.read(burgerno); // 카테고리 정보를 출력하기위한 목적
    model.addAttribute("burgerVO", burgerVO);

    return "burgerpost/create"; // /templates/burgerpost/create.html
  }

  /**
   * 등록 처리 http://localhost:9091/burgerpost/create
   * 
   * @return
   */
  @PostMapping(value = "/create")
  public String create(HttpServletRequest request, 
      HttpSession session, Model model,
      @RequestParam(name="burgerno", defaultValue = "")int burgerno,
      @ModelAttribute("burgerpostVO")BurgerpostVO burgerpostVO,
      RedirectAttributes ra) {

    if (memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 시작
      // ------------------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image
      String file1saved = ""; // 저장된 파일명, image
      String thumb1 = ""; // preview image

      String upDir = Burgerpost.getUploadDir(); // 파일을 업로드할 폴더 준비
      // upDir = upDir + "/" + 한글을 제외한 카테고리 이름
      System.out.println("-> upDir: " + upDir);

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = burgerpostVO.getFile1MF();

      file1 = mf.getOriginalFilename(); // 원본 파일명 산출, 01.jpg
      System.out.println("-> 원본 파일명 산출 file1: " + file1);

      long size1 = mf.getSize(); // 파일 크기
      if (size1 > 0) { // 파일 크기 체크, 파일을 올리는 경우
        if (Tool.checkUploadFile(file1) == true) { // 업로드 가능한 파일인지 검사
          // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg, spring_2.jpg...
          file1saved = Upload.saveFileSpring(mf, upDir);

          if (Tool.isImage(file1saved)) { // 이미지인지 검사
            // thumb 이미지 생성후 파일명 리턴됨, width: 200, height: 150
            thumb1 = Tool.preview(upDir, file1saved, 200, 150);
          }

          burgerpostVO.setFile1(file1); // 순수 원본 파일명
          burgerpostVO.setFile1saved(file1saved); // 저장된 파일명(파일명 중복 처리)
          burgerpostVO.setThumb1(thumb1); // 원본이미지 축소판
          burgerpostVO.setSize1(size1); // 파일 크기

        } else { // 전송 못하는 파일 형식
          ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 할 수 없는 파일
          ra.addFlashAttribute("cnt", 0); // 업로드 실패
          ra.addFlashAttribute("url", "/burgerpost/msg"); // msg.html, redirect parameter 적용
          return "redirect:/burgerpost/msg"; // Post -> Get - param... 안 하면 새로고침 하면 파일 또 올라감
        }
      } else { // 글만 등록하는 경우
        System.out.println("-> 글만 등록");
      }

      // ------------------------------------------------------------------------------
      // 파일 전송 코드 종료
      // ------------------------------------------------------------------------------

      // Call By Reference: 메모리 공유, Hashcode 전달
      int memberno = (int) session.getAttribute("memberno"); // memberno FK
      burgerpostVO.setMemberno(memberno);
      int cnt = this.burgerpostProc.create(burgerpostVO);
      
      this.burgerpostProc.inc_cnt_brand(burgerno);
      this.burgerpostProc.inc_cnt_burger(burgerno);
      
      // ------------------------------------------------------------------------------
      // PK의 return
      // ------------------------------------------------------------------------------
      // System.out.println("--> postno: " + burgerpostVO.getpostno());
      // mav.addObject("postno", burgerpostVO.getpostno()); // redirect
      // parameter 적용
      // ------------------------------------------------------------------------------

      if (cnt == 1) {
        // type 1, 재업로드 발생
        // return "<h1>파일 업로드 성공</h1>"; // 연속 파일 업로드 발생

        // type 2, 재업로드 발생
        // model.addAttribute("cnt", cnt);
        // model.addAttribute("code", "create_success");
        // return "burgerpost/msg";

        // type 3 권장
        // return "redirect:/burgerpost/list_all"; // /templates/burgerpost/list_all.html

        // System.out.println("-> burgerpostVO.getburgerno(): " + burgerpostVO.getburgerno());
        // ra.addFlashAttribute("burgerno", burgerpostVO.getburgerno()); // controller ->
        // controller: X

        ra.addAttribute("burgerno", burgerpostVO.getBurgerno()); // controller -> controller: O
        return "redirect:/burgerpost/list_by_burgerno";

        // return "redirect:/burgerpost/list_by_burgerno?burgerno=" + burgerpostVO.getburgerno();
        // // /templates/burgerpost/list_by_burgerno.html
      } else {
        ra.addFlashAttribute("code", "create_fail"); // DBMS 등록 실패
        ra.addFlashAttribute("cnt", 0); // 업로드 실패
        ra.addFlashAttribute("url", "/burgerpost/msg"); // msg.html, redirect parameter 적용
        return "redirect:/burgerpost/msg"; // Post -> Get - param...
      }
    } else { // 로그인 실패 한 경우
      return "redirect:/member/login_cookie_need"; // /member/login_cookie_need.html
    }
  }

  /**
   * 전체 목록, 관리자만 사용 가능 http://localhost:9091/burgerpost/list_all.
   * 
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, Model model) {
    // System.out.println("-> list_all");
    ArrayList<BurgerVOMenu> menu = this.burgerProc.menu();
    model.addAttribute("menu", menu);

    if (this.memberProc.isMemberAdmin(session)) { // 관리자만 조회 가능
      ArrayList<BurgerpostVO> list = this.burgerpostProc.list_all();  // 모든 목록

      // Thymeleaf는 CSRF(크로스사이트) 스크립팅 해킹 방지 자동 지원
      // for문을 사용하여 객체를 추출, Call By Reference 기반의 원본 객체 값 변경
//      for (burgerpostVO burgerpostVO : list) {
//        String title = burgerpostVO.getTitle();
//        String content = burgerpostVO.getContent();
//        
//        title = Tool.convertChar(title);  // 특수 문자 처리
//        content = Tool.convertChar(content); 
//        
//        burgerpostVO.setTitle(title);
//        burgerpostVO.setContent(content);  
//
//      }

      model.addAttribute("list", list);
      return "burgerpost/list_all";

    } else {
      return "redirect:/member/login_cookie_need";
    }
  }

//  /**
//   * 유형 1
//   * 카테고리별 목록
//   * http://localhost:9091/burgerpost/list_by_burgerno?burgerno=5
//   * http://localhost:9091/burgerpost/list_by_burgerno?burgerno=6 
//   * @return
//   */
//  @GetMapping(value="/list_by_burgerno")
//  public String list_by_burgerno(HttpSession session, Model model, 
//      @RequestParam(name="burgerno", defaultValue = "")int burgerno) {
//    ArrayList<BurgerVOMenu> menu = this.burgerProc.menu();
//    model.addAttribute("menu", menu);
//    
//     BurgerVO burgerVO = this.burgerProc.read(burgerno);
//     model.addAttribute("burgerVO", burgerVO);
//    
//    ArrayList<BurgerpostVO> list = this.burgerpostProc.list_by_burgerno(burgerno);
//    model.addAttribute("list", list);
//    
//    // System.out.println("-> size: " + list.size());
//
//    return "burgerpost/list_by_burgerno";
//  }

//  /**
//   * 유형 2
//   * 카테고리별 목록 + 검색
//   * http://localhost:9091/burgerpost/list_by_burgerno?burgerno=5
//   * http://localhost:9091/burgerpost/list_by_burgerno?burgerno=6 
//   * @return
//   */
//  @GetMapping(value="/list_by_burgerno")
//  public String list_by_burgerno_search(HttpSession session, Model model, 
//                                                    int burgerno, @RequestParam(name="word", defaultValue = "") String word) {
//    ArrayList<burgerVOMenu> menu = this.burgerProc.menu();
//    model.addAttribute("menu", menu);
//    
//     burgerVO burgerVO = this.burgerProc.read(burgerno);
//     model.addAttribute("burgerVO", burgerVO);
//    
//     word = Tool.checkNull(word).trim();
//     
//     HashMap<String, Object> map = new HashMap<>();
//     map.put("burgerno", burgerno);
//     map.put("word", word);
//     
//    ArrayList<burgerpostVO> list = this.burgerpostProc.list_by_burgerno_search(map);
//    model.addAttribute("list", list);
//    
//    // System.out.println("-> size: " + list.size());
//    model.addAttribute("word", word);
//    
//    int search_count = this.burgerpostProc.list_by_burgerno_search_count(map);
//    model.addAttribute("search_count", search_count);
//    
//    return "burgerpost/list_by_burgerno_search"; // /templates/burgerpost/list_by_burgerno_search.html
//  }

  /**
   * 유형 3
   * 카테고리별 목록 + 검색 + 페이징 http://localhost:9091/burgerpost/list_by_burgerno?burgerno=5
   * http://localhost:9091/burgerpost/list_by_burgerno?burgerno=6
   * 
   * @return
   */
  @GetMapping(value = "/list_by_burgerno")
  public String list_by_burgerno_search_paging(HttpSession session, Model model, 
      @RequestParam(name = "burgerno", defaultValue = "") int burgerno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    // System.out.println("-> burgerno: " + burgerno);

    ArrayList<BurgerVOMenu> menu = this.burgerProc.menu();
    model.addAttribute("menu", menu);

    BurgerVO burgerVO = this.burgerProc.read(burgerno);
    model.addAttribute("burgerVO", burgerVO);

    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    map.put("burgerno", burgerno);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<BurgerpostVO> list = this.burgerpostProc.list_by_burgerno_search_paging(map);
    model.addAttribute("list", list);

    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);

    int search_count = this.burgerpostProc.list_by_burgerno_search_count(map);
    String paging = this.burgerpostProc.pagingBox(burgerno, now_page, word, "/burgerpost/list_by_burgerno", search_count,
        Burgerpost.RECORD_PER_PAGE, Burgerpost.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Burgerpost.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    return "/burgerpost/list_by_burgerno_search_paging"; // /templates/burgerpost/list_by_burgerno_search_paging.html
  }

  /**
   * 카테고리별 목록 + 검색 + 페이징 + Grid
   * http://localhost:9091/burgerpost/list_by_burgerno?burgerno=5
   * http://localhost:9091/burgerpost/list_by_burgerno?burgerno=6
   * 
   * @return
   */
  @GetMapping(value = "/list_by_burgerno_grid")
  public String list_by_burgerno_search_paging_grid(HttpSession session, Model model, 
      @RequestParam(name = "burgerno", defaultValue = "") int burgerno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    // System.out.println("-> burgerno: " + burgerno);

    ArrayList<BurgerVOMenu> menu = this.burgerProc.menu();
    model.addAttribute("menu", menu);

    BurgerVO burgerVO = this.burgerProc.read(burgerno);
    model.addAttribute("burgerVO", burgerVO);

    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    map.put("burgerno", burgerno);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<BurgerpostVO> list = this.burgerpostProc.list_by_burgerno_search_paging(map);
    model.addAttribute("list", list);

    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);

    int search_count = this.burgerpostProc.list_by_burgerno_search_count(map);
    String paging = this.burgerpostProc.pagingBox(burgerno, now_page, word, "/burgerpost/list_by_burgerno_grid", search_count,
        Burgerpost.RECORD_PER_PAGE, Burgerpost.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Burgerpost.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    // /templates/burgerpost/list_by_burgerno_search_paging_grid.html
    return "/burgerpost/list_by_burgerno_search_paging_grid";
  }

  /**
   * 조회 http://localhost:9091/burgerpost/read?postno=17
   * 
   * @return
   */
  @GetMapping(value = "/read")
  public String read(Model model, 
      @RequestParam(name="postno", defaultValue = "0")int postno, 
      @RequestParam(name="word", defaultValue = "")String word, 
      @RequestParam(name="now_page", defaultValue = "1")int now_page) { 
    ArrayList<BurgerVOMenu> menu = this.burgerProc.menu();
    model.addAttribute("menu", menu);

    BurgerpostVO burgerpostVO = this.burgerpostProc.read(postno);

//    String title = burgerpostVO.getTitle();
//    String content = burgerpostVO.getContent();
//    
//    title = Tool.convertChar(title);  // 특수 문자 처리
//    content = Tool.convertChar(content); 
//    
//    burgerpostVO.setTitle(title);
//    burgerpostVO.setContent(content);  

    long size1 = burgerpostVO.getSize1();
    String size1_label = Tool.unit(size1);
    burgerpostVO.setSize1_label(size1_label);

    model.addAttribute("burgerpostVO", burgerpostVO);

    BurgerVO burgerVO = this.burgerProc.read(burgerpostVO.getBurgerno());
    model.addAttribute("burgerVO", burgerVO);

    // 조회에서 화면 하단에 출력
    // ArrayList<ReplyVO> reply_list = this.replyProc.list_burgerpost(postno);
    // mav.addObject("reply_list", reply_list);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    return "/burgerpost/read";
  }

  /**
   * 맵 등록/수정/삭제 폼 http://localhost:9091/burgerpost/map?postno=1
   * 
   * @return
   */
  @GetMapping(value = "/map")
  public String map(Model model, 
      @RequestParam(name="postno", defaultValue = "") int postno) {
    ArrayList<BurgerVOMenu> menu = this.burgerProc.menu();
    model.addAttribute("menu", menu);

    BurgerpostVO burgerpostVO = this.burgerpostProc.read(postno); // map 정보 읽어 오기
    model.addAttribute("burgerpostVO", burgerpostVO); // request.setAttribute("burgerpostVO", burgerpostVO);

    BurgerVO burgerVO = this.burgerProc.read(burgerpostVO.getBurgerno()); // 그룹 정보 읽기
    model.addAttribute("burgerVO", burgerVO);

    return "burgerpost/map";
  }

  /**
   * MAP 등록/수정/삭제 처리 http://localhost:9091/burgerpost/map
   * 
   * @param burgerpostVO
   * @return
   */
  @PostMapping(value = "/map")
  public String map_update(Model model, 
      @RequestParam(name="postno", defaultValue = "") int postno, 
      @RequestParam(name="map", defaultValue = "") String map) {
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("postno", postno);
    hashMap.put("map", map);

    this.burgerpostProc.map(hashMap);

    return "redirect:/burgerpost/read?postno=" + postno;
  }

  /**
   * Youtube 등록/수정/삭제 폼 http://localhost:9091/burgerpost/youtube?postno=1
   * 
   * @return
   */
  @GetMapping(value = "/youtube")
  public String youtube(Model model, 
      @RequestParam(name="postno", defaultValue = "") int postno, 
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "") int now_page) {
    ArrayList<BurgerVOMenu> menu = this.burgerProc.menu();
    model.addAttribute("menu", menu);

    BurgerpostVO burgerpostVO = this.burgerpostProc.read(postno); // map 정보 읽어 오기
    model.addAttribute("burgerpostVO", burgerpostVO); // request.setAttribute("burgerpostVO", burgerpostVO);

    BurgerVO burgerVO = this.burgerProc.read(burgerpostVO.getBurgerno()); // 그룹 정보 읽기
    model.addAttribute("burgerVO", burgerVO);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    return "burgerpost/youtube";  // forward
  }

  /**
   * Youtube 등록/수정/삭제 처리 http://localhost:9091/burgerpost/youtube
   * 
   * @param burgerpostVO
   * @return
   */
  @PostMapping(value = "/youtube")
  public String youtube_update(Model model, 
                                             RedirectAttributes ra,
                                             @RequestParam(name="postno", defaultValue = "")int postno, 
                                             @RequestParam(name="youtube", defaultValue = "")String youtube, 
                                             @RequestParam(name="word", defaultValue = "")String word, 
                                             @RequestParam(name="now_page", defaultValue = "")int now_page) {

    if (youtube.trim().length() > 0) { // 삭제 중인지 확인, 삭제가 아니면 youtube 크기 변경
      youtube = Tool.youtubeResize(youtube, 640); // youtube 영상의 크기를 width 기준 640 px로 변경
    }

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("postno", postno);
    hashMap.put("youtube", youtube);

    this.burgerpostProc.youtube(hashMap);
    
    ra.addAttribute("postno", postno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);

    // return "redirect:/burgerpost/read?postno=" + postno;
    return "redirect:/burgerpost/read";
  }

  /**
   * 수정 폼 http:// localhost:9092/burgerpost/update_text?postno=1
   *
   */
  @GetMapping(value = "/update_text")
  public String update_text(HttpSession session, Model model, 
      @RequestParam(name="postno", defaultValue = "") int postno, 
      RedirectAttributes ra, 
      @RequestParam(name="word", defaultValue = "") String word,
      @RequestParam(name="now_page", defaultValue = "") int now_page) {
    ArrayList<BurgerVOMenu> menu = this.burgerProc.menu();
    model.addAttribute("menu", menu);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      BurgerpostVO burgerpostVO = this.burgerpostProc.read(postno);
      model.addAttribute("burgerpostVO", burgerpostVO);

      BurgerVO burgerVO = this.burgerProc.read(burgerpostVO.getBurgerno());
      model.addAttribute("burgerVO", burgerVO);

      return "/burgerpost/update_text"; // /templates/burgerpost/update_text.html
      // String content = "장소:\n인원:\n준비물:\n비용:\n기타:\n";
      // model.addAttribute("content", content);

    } else {
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/burgerpost/msg"; // @GetMapping(value = "/read")
    }

  }

  /**
   * 수정 처리 http://localhost:9091/burgerpost/update_text?postno=1
   * 
   * @return
   */
  @PostMapping(value = "/update_text")
  public String update_text(HttpSession session, Model model, 
           @ModelAttribute("burgerpostVO")BurgerpostVO burgerpostVO, 
           RedirectAttributes ra,
           @RequestParam(name="search_word", defaultValue = "")String search_word, // burgerpostVO.word와 구분 필요
           @RequestParam(name="now_page", defaultValue = "") int now_page) {
    ra.addAttribute("word", search_word);
    ra.addAttribute("now_page", now_page);

    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("postno", burgerpostVO.getPostno());
      map.put("passwd", burgerpostVO.getPasswd());

      if (this.burgerpostProc.password_check(map) == 1) { // 패스워드 일치
        this.burgerpostProc.update_text(burgerpostVO); // 글수정

        // mav 객체 이용
        ra.addAttribute("postno", burgerpostVO.getPostno());
        ra.addAttribute("burgerno", burgerpostVO.getBurgerno());
        return "redirect:/burgerpost/read"; // @GetMapping(value = "/read")

      } else { // 패스워드 불일치
        ra.addFlashAttribute("code", "passwd_fail"); // redirect -> forward -> html
        ra.addFlashAttribute("cnt", 0);
        ra.addAttribute("url", "/burgerpost/msg"); // msg.html, redirect parameter 적용

        return "redirect:/burgerpost/post2get"; // @GetMapping(value = "/msg")
      }
    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/burgerpost/post2get"; // @GetMapping(value = "/msg")
    }

  }

  /**
   * 파일 수정 폼 http://localhost:9092/burgerpost/update_file?postno=1
   * 
   * @return
   */
  @GetMapping(value = "/update_file")
  public String update_file(HttpSession session, 
      Model model, 
      @RequestParam(name="postno", defaultValue = "")  int postno,
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "")int now_page) {
    
    ArrayList<BurgerVOMenu> menu = this.burgerProc.menu();
    model.addAttribute("menu", menu);
    
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    BurgerpostVO burgerpostVO = this.burgerpostProc.read(postno);
    model.addAttribute("burgerpostVO", burgerpostVO);

    BurgerVO burgerVO = this.burgerProc.read(burgerpostVO.getBurgerno());
    model.addAttribute("burgerVO", burgerVO);

    return "/burgerpost/update_file";

  }

  /**
   * 파일 수정 처리 http://localhost:9091/burgerpost/update_file
   * 
   * @return
   */
  @PostMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, RedirectAttributes ra,
      @ModelAttribute("burgerpostVO") BurgerpostVO burgerpostVO,
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "")int now_page) {

    if (this.memberProc.isMemberAdmin(session)) {
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      BurgerpostVO burgerpostVO_old = burgerpostProc.read(burgerpostVO.getPostno());

      // -------------------------------------------------------------------
      // 파일 삭제 시작
      // -------------------------------------------------------------------
      String file1saved = burgerpostVO_old.getFile1saved(); // 실제 저장된 파일명
      String thumb1 = burgerpostVO_old.getThumb1(); // 실제 저장된 preview 이미지 파일명
      long size1 = 0;

      String upDir = Burgerpost.getUploadDir(); // C:/kd/deploy/resort_v4sbm3c/burgerpost/storage/

      Tool.deleteFile(upDir, file1saved); // 실제 저장된 파일삭제
      Tool.deleteFile(upDir, thumb1); // preview 이미지 삭제
      // -------------------------------------------------------------------
      // 파일 삭제 종료
      // -------------------------------------------------------------------

      // -------------------------------------------------------------------
      // 파일 전송 시작
      // -------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = burgerpostVO.getFile1MF();

      file1 = mf.getOriginalFilename(); // 원본 파일명
      size1 = mf.getSize(); // 파일 크기

      if (size1 > 0) { // 폼에서 새롭게 올리는 파일이 있는지 파일 크기로 체크 ★
        // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
        file1saved = Upload.saveFileSpring(mf, upDir);

        if (Tool.isImage(file1saved)) { // 이미지인지 검사
          // thumb 이미지 생성후 파일명 리턴됨, width: 250, height: 200
          thumb1 = Tool.preview(upDir, file1saved, 250, 200);
        }

      } else { // 파일이 삭제만 되고 새로 올리지 않는 경우
        file1 = "";
        file1saved = "";
        thumb1 = "";
        size1 = 0;
      }

      burgerpostVO.setFile1(file1);
      burgerpostVO.setFile1saved(file1saved);
      burgerpostVO.setThumb1(thumb1);
      burgerpostVO.setSize1(size1);
      // -------------------------------------------------------------------
      // 파일 전송 코드 종료
      // -------------------------------------------------------------------

      this.burgerpostProc.update_file(burgerpostVO); // Oracle 처리
      ra.addAttribute ("postno", burgerpostVO.getPostno());
      ra.addAttribute("burgerno", burgerpostVO.getBurgerno());
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      
      return "redirect:/burgerpost/read";
    } else {
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "redirect:/burgerpost/msg"; // GET
    }
  }

  /**
   * 파일 삭제 폼
   * http://localhost:9092/burgerpost/delete?postno=1
   * 
   * @return
   */
  @GetMapping(value = "/delete")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
      @RequestParam(name="burgerno", defaultValue = "") int burgerno, 
      @RequestParam(name="postno", defaultValue = "")int postno, 
      @RequestParam(name="word", defaultValue = "")String word, 
      @RequestParam(name="now_page", defaultValue = "")int now_page) {
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      model.addAttribute("burgerno", burgerno);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      
      ArrayList<BurgerVOMenu> menu = this.burgerProc.menu();
      model.addAttribute("menu", menu);
      
      BurgerpostVO burgerpostVO = this.burgerpostProc.read(postno);
      model.addAttribute("burgerpostVO", burgerpostVO);
      
      BurgerVO burgerVO = this.burgerProc.read(burgerpostVO.getBurgerno());
      model.addAttribute("burgerVO", burgerVO);
      
      return "/burgerpost/delete"; // forward
      
    } else {
      ra.addAttribute("url", "/admin/login_cookie_need");
      return "redirect:/burgerpost/msg"; 
    }

  }
  
  /**
   * 삭제 처리 http://localhost:9091/burgerpost/delete
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra,
      @RequestParam(name="postno", defaultValue = "")int postno, 
      @RequestParam(name="burgerno", defaultValue = "")int burgerno, 
      @RequestParam(name="word", defaultValue = "")String word, 
      @RequestParam(name="now_page", defaultValue = "")int now_page) {
    // -------------------------------------------------------------------
    // 파일 삭제 시작
    // -------------------------------------------------------------------
    // 삭제할 파일 정보를 읽어옴.
    BurgerpostVO burgerpostVO_read = burgerpostProc.read(postno);
        
    String file1saved = burgerpostVO_read.getFile1saved();
    String thumb1 = burgerpostVO_read.getThumb1();
    
    String uploadDir = Burgerpost.getUploadDir();
    Tool.deleteFile(uploadDir, file1saved);  // 실제 저장된 파일삭제
    Tool.deleteFile(uploadDir, thumb1);     // preview 이미지 삭제
    // -------------------------------------------------------------------
    // 파일 삭제 종료
    // -------------------------------------------------------------------
        
    this.burgerpostProc.delete(postno); // DBMS 삭제
        
    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
    // -------------------------------------------------------------------------------------    
    // 마지막 페이지의 마지막 10번째 레코드를 삭제후
    // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
    // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("burgerno", burgerno);
    map.put("word", word);
    
    if (this.burgerpostProc.list_by_burgerno_search_count(map) % Burgerpost.RECORD_PER_PAGE == 0) {
      now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
      if (now_page < 1) {
        now_page = 1; // 시작 페이지
      }
    }
    // -------------------------------------------------------------------------------------

    ra.addAttribute("burgerno", burgerno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    
    this.burgerpostProc.dec_cnt_brand(burgerno);
    this.burgerpostProc.dec_cnt_burger(burgerno);
    
    return "redirect:/burgerpost/list_by_burgerno";    
  }   
}