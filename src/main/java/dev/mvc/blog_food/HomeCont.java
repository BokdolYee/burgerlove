package dev.mvc.blog_food;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import dev.mvc.burger.BurgerProcInter;
import dev.mvc.burger.BurgerVO;
import dev.mvc.burger.BurgerVOMenu;
import dev.mvc.tool.Security;

@Controller
public class HomeCont {
  @Autowired
  @Qualifier("dev.mvc.burger.BurgerProc")
  private BurgerProcInter burgerProc;
  
  @Autowired
  private Security security;
  
  public HomeCont() {
    System.out.println("-> HomeCont created.");
  }
  
  @GetMapping(value={"/", "/index.do"}) // http://localhost:9092
  public String home(Model model) { // 파일명 return
    if(this.security != null) {
      System.out.println("-> 객체 고유 코드: " + security.hashCode());
      System.out.println(security.aesEncode("1234"));
    }
//    ArrayList<burgerVO> menu = this.burgerProc.list_all_kind_y();
//    model.addAttribute("menu", menu);
    ArrayList<BurgerVOMenu> menu = this.burgerProc.menu();
    model.addAttribute("menu", menu);
    
    return "index"; // /templates/index.html  
  }
  
}