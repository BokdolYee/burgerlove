package dev.mvc.burger;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BurgerVOMenu {
  
  /** 종류(대분류) */
  private String brand;
  
  /** 종류(대분류) */
  private ArrayList<BurgerVO> list_name;
}
