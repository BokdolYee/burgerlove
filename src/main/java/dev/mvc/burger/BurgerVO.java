package dev.mvc.burger;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

//CREATE TABLE burger(
//    burgerNO NUMERIC(10) NOT NULL PRIMARY KEY,
//    name VARCHAR(50) NOT NULL,
//    SEQNO NUMERIC(5) DEFAULT 1 NOT NULL,
//    ISVISIBLE CHAR(1) DEFAULT 'T' NOT NULL,
//    CNT NUMBER(7) DEFAULT 0 NOT NULL,
//    RDATE DATE NOT NULL
//);

@Setter @Getter @ToString
public class BurgerVO {

  /** 햄버거 번호, Sequence에서 자동 생성 **/
  private Integer burgerno = 0;
  
  /** 햄버거 이름 **/
  @NotEmpty(message="햄버거 이름 입력은 필수 항목입니다.")
  @Size(min=2, max=50, message="제목은 최소 2자에서 최대 50자입니다.")
  private String name;
  
  /** 햄버거 브랜드 **/
  @NotEmpty(message="햄버거 브랜드 입력은 필수 항목입니다.")
  @Size(min=2, max=50, message="이름은 최소 2자에서 최대 50자입니다.")
  private String brand;
  
  /** 관련 자료수 **/
  @NotNull(message="관련 자료수는 필수 입력 항목입니다.")
  @Min(value=0)
  @Max(value=1000000)
  private Integer cnt=0;
  
  /** 출력 순서 **/
  @NotNull(message="출력 순서는 필수 입력 항목입니다.")
  @Min(value=0)
  @Max(value=1000000)
  private Integer seqno;
  
  /** 출력 모드 **/
  @NotEmpty(message="출력 모드는 필수 항목입니다.")
  @Pattern(regexp="^[TF]$", message="T 또는 F만 입력 가능합니다.")
  private String isvisible;
  
  /** 등록일, sysdate 자동 생성 **/
  private String rdate = "";
}
