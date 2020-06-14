package org.csu.mypetstore;

//import javafx.application.Application;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
@MapperScan("org.csu.mypetstore.persistence")
public class MypetstoreApplication{
    public static void main(String[] args){
        // 程序启动入口
//        Properties properties = new Properties();
//        InputStream in = MypetstoreApplication.class.getClassLoader().getResourceAsStream("app.properties");
//        properties.load(in);
//        SpringApplication app = new SpringApplication(MypetstoreApplication.class);
//        app.setDefaultProperties(properties);
//        app.run(args);
        SpringApplication.run(MypetstoreApplication.class,args);
        /*EmbeddedServletContainerAutoConfiguration*/
    }
}


