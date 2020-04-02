package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.*;
import org.csu.mypetstore.service.CatalogService;
import org.csu.mypetstore.service.DailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/catalog")
@SessionAttributes(value = {"isLogin","myAccount"})
public class CatalogController {

    @Autowired
    private CatalogService catalogService;
    @Autowired
    private DailyService dailyService;

    @GetMapping("viewMain")
    public String viewMain(Model model) {
        System.out.println("main");
        Account account = new Account();
        System.out.println(account);
        model.addAttribute("isLogin",false);
        return "catalog/main";
    }
    @GetMapping("/signon")
    public String signon() {
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
    //这里添加了Username便于进行日志的记录
    @GetMapping("viewItem")
    public String viewItem(String itemId, Model model,String userId){
        Item item = catalogService.getItem(itemId);
        Product product = item.getProduct();
        processProductDescription(product);
        model.addAttribute("item",item);
        model.addAttribute("product",product);
        //记录日志
        boolean isLogin = (boolean) model.getAttribute("isLogin");
        if(isLogin) {
                java.util.Date date = new java.util.Date();
                Timestamp time =  new Timestamp(date.getTime());
                String kind = "browse";
                String items = itemId;
                Daily daily = new Daily();
                daily.setUserId(userId);
                daily.setDatetime(time);
                daily.setKind(kind);
                daily.setItems(items);
                dailyService.insertDaily(daily);
        }
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
