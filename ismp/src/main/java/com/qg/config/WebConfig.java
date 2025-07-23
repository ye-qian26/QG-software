package com.qg.config;
import com.qg.config.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class WebConfig implements WebMvcConfigurer {

    //@Autowired
    private TokenInterceptor tokenInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns("/users/password",
                                     "/users/code",
                                     "/users/register",
                                     "/softwares/SearchSoftwareNew",
                                     "/softwares/SearchTypeNew",
                                     "/softwares/SearchSoftwareType",
                                     "/softwares/SearchSoftware",
                                     "/softwares/SearchSoftwareVersion"
                        ); // 排除路径
    }
}
