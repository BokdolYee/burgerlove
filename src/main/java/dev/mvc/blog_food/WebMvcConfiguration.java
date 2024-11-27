package dev.mvc.blog_food;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dev.mvc.burgerpost.Burgerpost;
import dev.mvc.tool.Tool;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Windows: path = "C:\kd\deploy\resort\contents\storage"
        // ▶ file:///C:/kd/deploy/resort_v2sbm3c_blog/contents/storage
      
        // Ubuntu: path = "/home/ubuntu/deploy/resort/contents/storage";
        // ▶ file:////home/ubuntu/deploy/resort_v2sbm3c_blog/contents/storage
      
        // C:\kd\deploy\resort\contents\storage -> /contents/storage -> http://localhost:9091/contents/storage
        registry.addResourceHandler("/burgerpost/storage/**").addResourceLocations("file:///" +  Burgerpost.getUploadDir());
        
        // C:\kd\deploy\food\storage -> /food/storage -> http://localhost:9091/food/storage
        // registry.addResourceHandler("/food/storage/**").addResourceLocations("file:///" +  Tool.getOSPath() + "/attachfile/storage/");
        
        // C:\kd\deploy\trip\storage -> /trip/storage -> http://localhost:9091/trip/storage
        // registry.addResourceHandler("/contents/storage/**").addResourceLocations("file:///" +  Tool.getOSPath() + "/member/storage/");
    }
 
}