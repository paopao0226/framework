package org.csu.mypetstore;

import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootApplication
@MapperScan("org.csu.mypetstore.persistence")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class MypetstoreApplication {
//	public MypetstoreApplication(CatalogController 	t){
//		t.signon();
//	}
	public static void main(String[] args) {
		SpringApplication.run(MypetstoreApplication.class, args);
	}
}
