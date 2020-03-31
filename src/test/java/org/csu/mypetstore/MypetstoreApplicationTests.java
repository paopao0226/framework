package org.csu.mypetstore;

import org.csu.mypetstore.service.CartService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("org.csu.mypetstore.persistence")
class MypetstoreApplicationTests {
    @Autowired
    CartService cartService;

    @Test
    void contextLoads() {
    }

    @Test
    void testMybatis(){
        cartService.addItemToCart("ywj","EST-12",1);
    }
}
