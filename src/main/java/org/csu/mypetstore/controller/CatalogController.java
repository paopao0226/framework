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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/catalog/")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

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

    @GetMapping("viewItem")
    public String viewItem(String itemId, Model model){
        Item item = catalogService.getItem(itemId);
        Product product = item.getProduct();
        processProductDescription(product);
        model.addAttribute("item",item);
        model.addAttribute("product",product);
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
