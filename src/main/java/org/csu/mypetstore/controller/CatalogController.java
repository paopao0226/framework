package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.*;
import org.csu.mypetstore.service.CatalogService;
import org.csu.mypetstore.service.DailyService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
@Controller
@Component
@RequestMapping("/catalog")
@SessionAttributes(value = {"isLogin","myAccount"})
@RunWith(SpringJUnit4ClassRunner.class)
public class CatalogController {

    @Autowired
    private CatalogService catalogService;
    @Autowired
    private DailyService dailyService;
    //这里用来判断是否已经进入系统，用来进行isLogin初始化时的判断
    private int isEnter;

    @GetMapping("/main/{isEnter}")
    public String viewMain(Model model,@PathVariable("isEnter") int isEnter) {
        System.out.println("main");
        Account account = new Account();
        System.out.println(account);
        //转回main时判断是否登录，在进入系统时初始化登录
        if(isEnter == 0) {
            model.addAttribute("isLogin", false);
        }
        return "catalog/main";
    }
    @GetMapping("/signon")
    public String signon(Model model) {
        //这里添了一句话
        isEnter = 1;
        return "account/SignonForm";
    }
    @GetMapping("viewCategory")
    public String viewCategory(String categoryId, Model model) {
        if (categoryId != null) {
            Category category = catalogService.getCategory(categoryId);
            List<Product> productList = catalogService.getProductListByCategory(categoryId);
            model.addAttribute("category", category);
            model.addAttribute("productList", productList);
        }
        return "catalog/category";
    }

    @GetMapping("viewProduct")
    public String viewProduct(String productId, Model model) {

        if (productId != null) {
            Product product = catalogService.getProduct(productId);
            List<Item> itemList = catalogService.getItemListByProduct(productId);
            model.addAttribute("product", product);
            model.addAttribute("itemList", itemList);
        }
        return "catalog/product";
    }
    public void add(){
        System.out.println("成功记录");
    }
    //这里添加了Username便于进行日志的记录
    @GetMapping("viewItem")
    public String viewItem(String itemId, Model model,String userId){
        Item item = catalogService.getItem(itemId);
        Product product = item.getProduct();
        processProductDescription(product);
        model.addAttribute("item",item);
        model.addAttribute("product",product);
        //记录日志
        return "catalog/item";
    }

//    @GetMapping("viewItem")
//    public String viewItem(String itemId, Model model) {
//
//        if (itemId != null) {
//            Item item = catalogService.getItem(itemId);
//            Product product = item.getProduct();
//            processProductDescription(product);
//            model.addAttribute("item", item);
//            model.addAttribute("product", product);
//        }
//        return "catalog/item";
//    }
@PostMapping("searchProducts")
public String searchProducts(String keyword, Model model) {

    if (keyword == null || keyword.length() < 1) {
        String msg = "Please enter a keyword to search for, then press the search button.";
        model.addAttribute("msg", msg);
        return "common/error";
    } else {
        List<Product> productList = catalogService.searchProductList(keyword.toLowerCase());
        processProductDescription(productList);
        model.addAttribute("productList", productList);
        return "catalog/searchProduct";
    }


}
    private void processProductDescription(Product product){
        System.out.println(product.getDescription());
        String [] temp = product.getDescription().split("\"");
        product.setDescriptionImage(temp[1]);
        product.setDescriptionText(temp[2].substring(1));
        System.out.println(product.getDescriptionText());
        System.out.println(product.getDescriptionImage());
    }
    private void processProductDescription(List<Product> productList){
        for(Product product : productList) {
            processProductDescription(product);
        }
    }


}
