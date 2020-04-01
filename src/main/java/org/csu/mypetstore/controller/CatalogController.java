package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Category;
import org.csu.mypetstore.domain.Item;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@RequestMapping("/catalog")
@SessionAttributes({"isLogin"})
public class CatalogController {
    @Autowired
    private CatalogService catalogService;

    @GetMapping("/viewMain")
    public String view(Model model) {
        System.out.println("main");
        Account account = new Account();
        System.out.println(account);
        model.addAttribute("isLogin",false);
        return "/catalog/main";
    }

    @GetMapping("/viewCategory")
    public String viewCategory(Model model, @RequestParam("categoryId") String categoryId) {
        if (categoryId != null) {
            Category category = catalogService.getCategory(categoryId);
            List<Product> list = catalogService.getProductListByCategory(categoryId);
            model.addAttribute("category", category);
            model.addAttribute("productList", list);
            return "catalog/category";
        }
        return "catalog/main";
    }
    @GetMapping("/signon")
    public String signon() {
        return "account/SignonForm";
    }
    //远程
//    @GetMapping("viewProduct")
//    public String viewProduct(String productId, Model model) {
//
//        if (productId != null) {
//            Product product = catalogService.getProduct(productId);
//            List<Item> itemList = catalogService.getItemListByProduct(productId);
//            model.addAttribute("product", product);
//            model.addAttribute("itemList", itemList);
//        }
//        return "catalog/product";
//    }
//
//    @GetMapping("viewItem")
//    public String viewItem(String itemId, Model model){
//        Item item = catalogService.getItem(itemId);
//        Product product = item.getProduct();
//        processProductDescription(product);
//        model.addAttribute("item",item);
//        model.addAttribute("product",product);
//        return "catalog/item";
//    }
//
////    @GetMapping("viewItem")
////    public String viewItem(String itemId, Model model) {
////
////        if (itemId != null) {
////            Item item = catalogService.getItem(itemId);
////            Product product = item.getProduct();
////            processProductDescription(product);
////            model.addAttribute("item", item);
////            model.addAttribute("product", product);
////        }
////        return "catalog/item";
////    }
//
//    private void processProductDescription(Product product){
//        System.out.println(product.getDescription());
//        String [] temp = product.getDescription().split("\"");
//        product.setDescriptionImage(temp[1]);
//        product.setDescriptionText(temp[2].substring(1));
//        System.out.println(product.getDescriptionText());
//        System.out.println(product.getDescriptionImage());
//    }
//    private void processProductDescription(List<Product> productList){
//        for(Product product : productList) {
//            processProductDescription(product);
//        }
//    }
}
