package org.csu.mypetstore;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import org.csu.mypetstore.controller.AccountController;
import org.csu.mypetstore.domain.Category;
import org.csu.mypetstore.domain.Item;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Path;
import java.util.List;

import static org.csu.mypetstore.domain.SmsTool.sendSms;


@SpringBootTest
@MapperScan("org.csu.mypetstore.persistence")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
class MypetstoreApplicationTests {

    private static int newcode;

    @Autowired
    CatalogService catalogService;

    //以下为测试代码，随机生成验证码
    public static int getNewcode() {
        return newcode;
    }
    public static void setNewcode(){
        newcode = (int)(Math.random()*9999)+100;  //每次调用生成一次四位数的随机数
    }

//    @Test
//    void contextLoads() {
//    }

//    @Test
//    void testCategory() {
//        Category c = catalogService.getCategory("BIRDS");
//        System.out.println(c.getCategoryId() + " " + c.getName() + " " + c.getDescription());
//    }
//
//    @Test
//    void testProduct() {
//        List<Product> processList = catalogService.getProductListByCategory("BIRDS");
//        System.out.println(processList.size());
//        for (int i = 0; i < processList.size(); i++) {
//            System.out.println(processList.get(i).getDescription());
//        }
//    }
//
//    @Test
//    void testItem() {
//        List<Item> itemList = catalogService.getItemListByProduct("FL-DSH-01");
//        System.out.println(itemList.size());
//
//        Item item = catalogService.getItem("EST-1");
//        System.out.println(item.getItemId() + " " + item.getProductId());
//
//        int QTY = catalogService.getInventoryQuantity("EST-1");
//        System.out.println(QTY);
//    }
//    @Test
//    void testSearch() {
//        List<Product> productList = catalogService.searchProductList("A");
//        System.out.println(productList.size());
//        for (int i = 0; i < productList.size(); i++) {
//            System.out.println(productList.get(i).getName());
//        }
//    }
//
    @Test
    public void temp(){
        AccountController controller = new AccountController();
        System.out.println(controller.KL(""));
    }
//
//    @Test
//    public static void main(String[] args) throws Exception {
//        setNewcode();
//        String code = Integer.toString(getNewcode());
//        System.out.println(code);
//        SendSmsResponse sendSms =sendSms("15333251606",code);//填写你需要测试的手机号码
//        System.out.println("短信接口返回的数据----------------");
//        System.out.println("Code=" + sendSms.getCode());
//        System.out.println("Message=" + sendSms.getMessage());
//        System.out.println("RequestId=" + sendSms.getRequestId());
//        System.out.println("BizId=" + sendSms.getBizId());
//    }
//
//    @Test
//    public void testGetAllProducts(){
//        List<Product> productList = catalogService.getAllProducts();
//        for(int i = 0; i < productList.size(); i++){
//            System.out.println(productList.get(i).getProductId());
//            System.out.println(productList.get(i).getName());
//            System.out.println(productList.get(i).getDescription());
//        }
//    }
//
//    @Test
//    public void testGetAllCategorys(){
//        List<Category> categoryList = catalogService.getAllCategorys();
//        for(int i = 0; i < categoryList.size(); i++){
//            System.out.println(categoryList.get(i).getCategoryId());
//            System.out.println(categoryList.get(i).getName());
//            System.out.println(categoryList.get(i).getDescription());
//        }
//    }
//
//    @Test
//    public void testUpdateProduct(){
//        Product product = new Product();
//        product.setName("test1");
//        product.setCategoryId("FISH");
//        product.setProductId("testP");
//        int flag = catalogService.updateProduct(product);
//        System.out.println(flag);
//    }
//
//    @Test
//    public void testSearchItem(){
//        List<Item> itemList = catalogService.searchItemList("a");
//        for(int i = 0; i < itemList.size(); i++){
//            System.out.println(itemList.get(i).getItemId());
//        }
//
//    }
//    @Test
//    public void testInventory(){
//        catalogService.deleteInventory("testI");
//
//    }
//    @Test
//    public void testWebRoot(){
//        String s = getWebRootAbsolutePath();
//        System.out.println(s);
//    }
//    public static String getWebRootAbsolutePath() {
//        String path = null;
//        String folderPath = Path.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        if (folderPath.indexOf("WEB-INF") > 0) {
//            path = folderPath.substring(0, folderPath
//                    .indexOf("WEB-INF/classes"));
//        }
//        return path;
//    }
}
