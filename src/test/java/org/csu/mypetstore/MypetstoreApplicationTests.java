package org.csu.mypetstore;

import org.csu.mypetstore.controller.CatalogController;
import org.csu.mypetstore.domain.Category;
import org.csu.mypetstore.domain.Item;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
@MapperScan("org.csu.mypetstore.persistence")
class MypetstoreApplicationTests {

    @Autowired
    CatalogService catalogService;

    @Test
    void contextLoads() {
    }

    @Test
    void testCategory() {
        Category c = catalogService.getCategory("BIRDS");
        System.out.println(c.getCategoryId() + " " + c.getName() + " " + c.getDescription());
    }

    @Test
    void testProduct() {
        List<Product> processList = catalogService.getProductListByCategory("BIRDS");
        System.out.println(processList.size());
        for (int i = 0; i < processList.size(); i++) {
            System.out.println(processList.get(i).getDescription());
        }
    }

    @Test
    void testItem() {
        List<Item> itemList = catalogService.getItemListByProduct("FL-DSH-01");
        System.out.println(itemList.size());

        Item item = catalogService.getItem("EST-1");
        System.out.println(item.getItemId() + " " + item.getProductId());

        int QTY = catalogService.getInventoryQuantity("EST-1");
        System.out.println(QTY);
    }
    @Test
    void testSearch() {
        List<Product> productList = catalogService.searchProductList("A");
        System.out.println(productList.size());
        for (int i = 0; i < productList.size(); i++) {
            System.out.println(productList.get(i).getName());
        }
//    @Test
//    void test(){
//        CatalogController catalogController = new CatalogController();
//        Product product = new Product();
//        product.setDescription("<image src=\"images/bird2.gif\">Great companion for up to 75 years");
//        System.out.println(product.getDescription());
//        catalogController.processProductDescription(product);
//        System.out.println(product.getDescriptionImage());
//        System.out.println(product.getDescriptionText());
//    }



}
