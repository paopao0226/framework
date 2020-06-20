package org.csu.mypetstore.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 图片绝对地址与虚拟地址映射
 */

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //文件磁盘图片url 映射
    //配置server虚拟路径，handler为前台访问的目录，locations为files相对应的本地路径
        registry.addResourceHandler("/carts/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/orders/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/catalog/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/cms/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/cms/product/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/cms/item/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/cms/category/BIRDS/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/cms/category/DOGS/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/cms/category/FISH/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/cms/category/CATS/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/cms/category/REPTILES/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/cms/added/**").addResourceLocations("classpath:/static/");
    }
}