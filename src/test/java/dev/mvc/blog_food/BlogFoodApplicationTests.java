package dev.mvc.blog_food;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import dev.mvc.burger.BurgerDAOInter;
import dev.mvc.burger.BurgerProcInter;
import dev.mvc.burger.BurgerVO;
import dev.mvc.burger.BurgerVOMenu;

@SpringBootTest
class BlogFoodApplicationTests {
  @Autowired  // PostDAOInter를 구현한 클래스의 객체를 자동으로 생성하여 postDAO 객체에 할당
  private BurgerDAOInter postDAO;
  
  @Autowired
  @Qualifier("dev.mvc.post.PostProc")
  private BurgerProcInter postProc;
  
	@Test
	void contextLoads() {
	}
	
//	@Test
//  public void testCreate() {
//    PostVO postVO = new PostVO();
//    postVO.setPosttitle("10월 3주차 맛도리!");
//    postVO.setIsvisible("N");
//    int cnt = this.postProc.create(postVO);
//    System.out.println("cnt: " + cnt);
//  }
	
//	@Test
//	public void testCreate() {
//	  PostVO postVO = new PostVO();
//	  postVO.setPosttitle("9월 5주차 맛도리!");
//	  postVO.setIsvisible("F");
//    int cnt = this.postDAO.create(postVO);
//    System.out.println("cnt: " + cnt);
//	}
//	@Test
//  public void list_all() {
//    ArrayList<PostVO> list = this.postProc.list_all();
//    
//    for(PostVO postVO : list) {
//      System.out.println(postVO.toString());
//    }
//  }
//	  @Test
//	  public void read() {
//	    PostVO postVO = this.postProc.read(1);
//	    System.out.println(postVO.toString());
//	  }
	
//	@Test
//	public void menu() {
//	  ArrayList<PostVOMenu> menu = this.postProc.menu();
//	  
//	  for(PostVOMenu postVOMenu:menu) {
//	    System.out.println("->" + postVOMenu.getKind());
//	    
//	    ArrayList<PostVO> list_name = postVOMenu.getList_name();
//	    for(PostVO postVO:list_name) {
//	      System.out.println(" " + postVO.getPosttitle());
//	    }
//	  }
//	}
	
//	@Test
//	public void kindSet() {
//	  ArrayList<String> list = this.postProc.kindset();
//	  
//	  for(String kind:list) {
//	    System.out.println(kind);
//	  }
//	  
//	  System.out.println(String.join("/", list));
//	}
	
//	@Test
//  public void Hangul() {
////    String word = URLEncoder.encode("오버렌딩");
////    System.out.println("원본: %EC%98%A4%EB%B2%84%EB%A0%8C%EB%94%A9");
////    System.out.println("변환: " + word);
////    
////    word = URLDecoder.decode("%EC%98%A4%EB%B2%84%EB%A0%8C%EB%94%A9"); 
////    System.out.println("복원: " + word);
//
//    // JDK 10+, VSCode 사용
//    String word = URLEncoder.encode("오버렌딩", StandardCharsets.UTF_8); //한글 -> 16진수 코드화
//    System.out.println("원본: %EC%98%A4%EB%B2%84%EB%A0%8C%EB%94%A9");
//    System.out.println("변환: " + word);
//      
//    // 16진수 -> 한글
//    word = URLDecoder.decode("%EC%98%A4%EB%B2%84%EB%A0%8C%EB%94%A9", StandardCharsets.UTF_8); 
//    System.out.println("복원: " + word);
//  }
	@Test
	public void list_search_paging() {
	  this.postProc.list_search_paging("치킨", 1, 3);
	  this.postProc.list_search_paging("피자", 1, 3);
	  this.postProc.list_search_paging("햄버거", 1, 3);
	}
}
