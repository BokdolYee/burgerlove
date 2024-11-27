package dev.mvc.blog_food;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"dev.mvc"})
public class BlogFoodApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogFoodApplication.class, args);
	}

}
