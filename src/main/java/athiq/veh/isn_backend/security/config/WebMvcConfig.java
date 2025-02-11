package athiq.veh.isn_backend.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Assuming images are uploaded to the 'uploads' folder in the project root directory
        registry.addResourceHandler("/uploads/**");
//	                .addResourceLocations("file:uploads/");
                // or if it's an absolute path on the server
                 registry.addResourceHandler("/uploads/**")
//	        .addResourceLocations("file:src/main/resources/static/uploads");
                .addResourceLocations("classpath:/static/uploads/")
                         .addResourceLocations("file:/uploads/")
                .setCachePeriod(0);
    }
//	   @Override
//	    public void addCorsMappings(CorsRegistry registry) {
//	        registry.addMapping("/**")
//	                .allowedOrigins("http://localhost:5174")
//	                .allowedMethods("GET", "POST", "PUT", "DELETE")
//	                .allowedHeaders("*");
//	    }
}
