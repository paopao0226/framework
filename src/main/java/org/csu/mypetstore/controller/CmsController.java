package org.csu.mypetstore.controller;

import com.alipay.api.AlipayApiException;
import org.apache.tools.ant.taskdefs.Get;
import org.bouncycastle.math.raw.Mod;
import org.csu.mypetstore.domain.*;
import org.csu.mypetstore.service.AccountService;
import org.csu.mypetstore.service.CatalogService;
import org.csu.mypetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@Component
@SessionAttributes(value = {"product","cart","order","isLogin","myAccount","controlAccount","orderList","msg","orderOfUpdate","productOfUpdate","itemOfUpdate","AllOrderList","AllAccountList","languageList","categoryList","mylanguagePreference","myfavouriteCategoryId","myListOpt","myBannerOpt"})//model的attribution通过该注释将对象放在了session作用域中，以后通过model.getAttribute可以取出对象
@RequestMapping("/cms/")
public class CmsController {


    @Autowired
    OrderService orderService;
    @Autowired
    AccountService accountService;
    @Autowired
    CatalogService catalogService;
    //暂时保存查看的用户名
    String username;

    @GetMapping("main")
    public String viewMain(Model model){
        //这里加了一个初始化列表
        //注册与编辑用户时两个列表的初始化
        List<String> languageList = new ArrayList<>();
        List<String> categoryList = new ArrayList<>();
        languageList.add("english");
        languageList.add("japanese");
        categoryList.add("FISH");
        categoryList.add("DOGS");
        categoryList.add("REPTILES");
        categoryList.add("CATS");
        categoryList.add("BIRDS");
        model.addAttribute("languageList",languageList);
        model.addAttribute("categoryList",categoryList);
//        System.out.println(AccountController.roleSetting.toString());
        if(!AccountController.roleSetting.toString().equals("")){
            if(!AccountController.roleSetting.toString().equals("manager")){
                return "cms/failReading";
            }
        }
        return "cms/main";
    }

    @GetMapping("orders")//进入订单管理页面对应的方法
    public String manageOrder(Model model){
        List<Order> AllOrderList = orderService.getAllOrders();
        model.addAttribute("AllOrderList",AllOrderList);
        return "cms/manageOrder";
    }

//    @GetMapping("orders/{orderId}")
    @GetMapping(value = "orders/{orderId}/users/{userName}")
    public String viewUpdateOrder(@PathVariable("userName") String userName,@PathVariable("orderId") String orderId,Model model){//点击update后的方法
        //从数据库中拿出当前的order并且将其放入model中，方便前端进行阅览
        Order orderOfUpdate = orderService.getOrder(Integer.parseInt(orderId));
        model.addAttribute("orderOfUpdate",orderOfUpdate);
        return "cms/updateOrder";
    }

    //点击修改订单按钮后访问的方法
    @PostMapping("onlyOne")
    public String updateOrder(Order order,Model model){
        order.setOrderId(((Order)model.getAttribute("orderOfUpdate")).getOrderId());
        //更新数据库中的order并且返回影响的行数
        int flag = orderService.updateOrder(order);
        if(flag == 0){
            model.addAttribute("msg","update failed,please check again");
            return "common/error";
        }
        else {
            List<Order> AllOrderList = orderService.getAllOrders();
            model.addAttribute("AllOrderList",AllOrderList);
            return "cms/manageOrder";
        }
    }

    //点击remove按钮后访问的方法
//    @GetMapping("deleteOrder")
    @DeleteMapping(value = "orders/{orderId}/users/{userName}")
    public String deleteOrder(@PathVariable("orderId") String orderId,@PathVariable("userName")  String userName, Model model){
        int id = Integer.parseInt(orderId);
        //删除数据库中的order，并且返回影响的行数
        int flag = orderService.deleteOrder(id);
        if(flag == 0){
            model.addAttribute("msg","delete failed,please try again");
            return "common/error";
        }
        else{
            //更新成功后，需要重新设置model中的orderList，方便前端重新渲染
            List<Order> AllOrderList = orderService.getAllOrders();
            model.addAttribute("AllOrderList",AllOrderList);
            return  "cms/manageOrder";
        }
    }

    @PostMapping("searchOrders")
    public String searchOrders(String key,Model model){//搜索订单对应的方法
        //获取input框的关键词，在数据库搜索出相似的订单
        List<Order> AllOrderList = orderService.getOrdersByKeyword('%'+ key+ '%');
        model.addAttribute("AllOrderList",AllOrderList);
        return "cms/manageOrder";
    }

    @GetMapping("ajaxSearchOrders")
    @ResponseBody
    public String ajaxSearchOrders(String keyword){//在搜索订单的输入框输入关键词时对应的异步对象的方法
        System.out.println("----------------服务器接受到请求------------------" + keyword);
        //获取input框的关键词，在数据库搜索出相似的订单
        List<Order> searchOrderList = orderService.getOrdersByKeyword('%'+ keyword+ '%');
        StringBuffer result = new StringBuffer();
        for(int i = 0 ; i < searchOrderList.size();i++){//将与关键词匹配的order输入缓存
            if(i == searchOrderList.size() -1){
                result.append(((Order)searchOrderList.get(i)).getOrderId() + ",");
                result.append(((Order)searchOrderList.get(i)).getUsername());
            }
            else {
                result.append(((Order)searchOrderList.get(i)).getOrderId() + ",");
                result.append(((Order)searchOrderList.get(i)).getUsername() + ",");
            }
            System.out.println(((Order)searchOrderList.get(i)).getOrderId() + "," + ((Order)searchOrderList.get(i)).getUsername());
    }
        System.out.println("----------------searchOrderList.size------------------");
        System.out.println(searchOrderList.size());
        return result.toString();
    }

    @GetMapping("accounts")
    public String viewManageAccount(Model model){
        List<Account> AllAccountList = accountService.getAllAccount();
        int i = 0;
//        while(i < AllAccountList.size()){
//            System.out.println(AllAccountList.get(i).getUsername());
//        }
        model.addAttribute("AllAccountList",AllAccountList);
        return "cms/manageAccount";
//        return "catalog/main";
    }

    @PostMapping("searchaccounts")
    public String searchAccount(@PathVariable("keyword") String keyword,Model model){
//        //判断keyword指的是订单号还是用户名，这边默认用户名必须包含字母,所以如果keyword为纯数字的话指的就是订单号
//        Pattern pattern = Pattern.compile("[0-9]{1,}");
//        Matcher matcher = pattern.matcher((CharSequence)keyword);
//        boolean result=matcher.matches();
//        if (result == true) {
//            List<Order> AllOrderList = new ArrayList<>();
//            AllOrderList.add(orderService.getOrdersByOrderId(keyword));
//            model.addAttribute("AllOrderList",AllOrderList);
//        }else{
        if (keyword == null || keyword.length() < 1) {
            String msg = "Please enter a keyword to search for, then press the search button.";
            model.addAttribute("msg", msg);
            return "common/error";
        } else {
            List<Account> accountsList = accountService.searchAccount(keyword.toLowerCase());
            model.addAttribute("AllAccountList", accountsList);
            return "cms/manageAccount";
        }
    }
    @DeleteMapping("deleteaccounts/{username}")
    public String deleteAccount(@PathVariable("username") String username,Model model){
        accountService.deleteAccount(username);
        List<Account> AllAccountList = accountService.getAllAccount();
        model.addAttribute("AllAccountList",AllAccountList);
        return "cms/manageAccount";
    }
    @GetMapping("updateaccounts/{username}")
    public String viewUpdateAccount(@PathVariable("username") String userName,Model model){
        //从数据库中拿出当前的order并且将其放入model中，方便前端进行阅览
//        Order orderOfUpdate = orderService.getOrder(Integer.parseInt(orderId));
        Account controlAccount = accountService.getAccount(userName);
        username = userName;
        model.addAttribute("controlAccount",controlAccount);
        model.addAttribute("mylanguagePreference",controlAccount.getLanguagePreference());
        model.addAttribute("myfavouriteCategoryId",controlAccount.getFavouriteCategoryId());
        model.addAttribute("myListOpt",controlAccount.isListOption());
        model.addAttribute("myBannerOpt",controlAccount.isBannerOption());
        return "cms/updateAccount";
    }
    @PostMapping("editaccounts")//编辑
    public String editAccount(String password,String repeatedPassword,Account account,Model model){
        if(!password.equals(repeatedPassword)){
            String value = "<ui><li>Edit failed,Please check the password and repeatPassword</li></ui>";
            model.addAttribute("editMessage",value);
            return "cms/updateAccount";
        }
        else{
            if(account.getFirstName().equals("") || account.getLastName().equals("") || account.getAddress1().equals("")
                    || account.getCity().equals("") || account.getState().equals("") || account.getZip().equals("")
                    || account.getCountry().equals("") || account.getPhone().equals("")){
                String value = "<ui><li>Information missed. Please check the information</li></ui>";
                model.addAttribute("username",account.getUsername());
                model.addAttribute("password", account.getPassword());
                model.addAttribute("repeatedPassword", account.getPassword());
                model.addAttribute("editMessage", value);
                return "cms/updateAccount";
            }
            else {
                account.setPassword(KL(password));
                account.setUsername(username);
                accountService.updateAccount(account);
//                System.out.println(account.getUsername());
//                model.addAttribute("username", account.getUsername());
                List<Account> AllAccountList = accountService.getAllAccount();
                model.addAttribute("AllAccountList",AllAccountList);
                return "cms/manageAccount";
            }
        }
    }

    @GetMapping("viewEntry")
    public String viewEntry(){
        return "index";
    }

    @GetMapping("ajaxSearchAccounts")
    @ResponseBody
    public String ajaxSearchAccounts(String keyword){
        System.out.println("----------------服务器接受到请求------------------");
        List<Account> searchAccountList = accountService.searchAccount(keyword.toLowerCase());
        System.out.println(searchAccountList.size());
        StringBuffer result = new StringBuffer();
        for(int i = 0 ; i < searchAccountList.size();i++){
            if(i == searchAccountList.size() -1){
                result.append(((Account)searchAccountList.get(i)).getUsername() + ",");
                result.append(((Account)searchAccountList.get(i)).getFirstName() + ",");
                result.append(((Account)searchAccountList.get(i)).getLastName() + ",");
                result.append(((Account)searchAccountList.get(i)).getCity() + ",");
                result.append(((Account)searchAccountList.get(i)).getPhone());
            }
            result.append(((Account)searchAccountList.get(i)).getUsername() + ",");
            result.append(((Account)searchAccountList.get(i)).getFirstName() + ",");
            result.append(((Account)searchAccountList.get(i)).getLastName() + ",");
            result.append(((Account)searchAccountList.get(i)).getCity() + ",");
            result.append(((Account)searchAccountList.get(i)).getPhone() + ",");
            System.out.println("----------------searchOrderList.size(------------------");
            System.out.println(searchAccountList.size());
        }
        return result.toString();
    }
    //md5密码加密函数
    public String KL(String password){
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        System.out.println(password + " " + md5Password);
        return md5Password;
    }

    @GetMapping("ViewManageCategory")
    public String manageCategory(Model model){
        List<Category> AllCategoryList = catalogService.getAllCategorys();
        model.addAttribute("AllCategoryList", AllCategoryList);

        return "cms/manageCategory";
    }

    @GetMapping("ViewManageProduct")
    public String manageProduct(String categoryId, Model model){
        if (categoryId != null) {
            Category category = catalogService.getCategory(categoryId);
            List<Product> productList = catalogService.getProductListByCategory(categoryId);
            model.addAttribute("category", category);
            model.addAttribute("productList", productList);
        }
        return "cms/manageProduct";
    }

    @GetMapping("ViewManageItem")
    public String manageItem(String productId, Model model){
        if (productId != null) {
            Product product = catalogService.getProduct(productId);
            List<Item> itemList = catalogService.getItemListByProduct(productId);

            model.addAttribute("product", product);
            model.addAttribute("itemList", itemList);
        }
        return "cms/manageItem";
    }

    @GetMapping("DeleteProduct")
    public String deleteProduct(String deleteProductId, String productKeyword, String categoryId, Model model){
        if (deleteProductId != null) {
            if (categoryId != null) {
                catalogService.deleteItemByProductId(deleteProductId);
                catalogService.deleteProduct(deleteProductId);
                List<Item> itemList = catalogService.getItemListByProduct(deleteProductId);
                String tempItemId;
                for(int i = 0; i < itemList.size(); i++){
                    tempItemId = itemList.get(i).getItemId();
                    catalogService.deleteInventory(tempItemId);
                }
                Category category = catalogService.getCategory(categoryId);
                List<Product> productList = catalogService.getProductListByCategory(categoryId);
                model.addAttribute("category", category);
                model.addAttribute("productList", productList);
                return "cms/manageProduct";
            }
            if (productKeyword != null) {
                catalogService.deleteItemByProductId(deleteProductId);
                catalogService.deleteProduct(deleteProductId);
                List<Item> itemList = catalogService.getItemListByProduct(deleteProductId);
                String tempItemId;
                for(int i = 0; i < itemList.size(); i++){
                    tempItemId = itemList.get(i).getItemId();
                    catalogService.deleteInventory(tempItemId);
                }
                List<Product> productList = catalogService.searchProductList(productKeyword.toLowerCase());
                processProductDescription(productList);
                model.addAttribute("productList", productList);
                model.addAttribute("productKeyword", productKeyword);
                return "cms/searchProduct";
            }
        }
        return "cms/manageProduct";
    }

    @GetMapping("ViewUpdateProduct")
    public String viewUpdateProduct(String updateProductId, Model model){
        if (updateProductId != null) {
            Product productOfUpdate = catalogService.getProduct(updateProductId);
            model.addAttribute("productOfUpdate", productOfUpdate);
        }
        return "cms/updateProduct";
    }

    @PostMapping("UpdateProduct")
    public String updateProduct(String categoryType, String productName,Model model){
        Product product = (Product)model.getAttribute("productOfUpdate");
        product.setCategoryId(categoryType);
        product.setName(productName);

        int flag = catalogService.updateProduct(product);
        if(flag == 0){
            model.addAttribute("msg","Update failed,please check again");
            return "common/error";
        }
        else {
            Category category = catalogService.getCategory(categoryType);
            List<Product> productList = catalogService.getProductListByCategory(categoryType);
            model.addAttribute("category", category);
            model.addAttribute("productList", productList);
            return "cms/manageProduct";
        }
    }

    @GetMapping("ViewAddProduct")
    public String viewAddProduct(String categoryId, Model model){
        if (categoryId != null) {
            Category category = catalogService.getCategory(categoryId);
            model.addAttribute("category", category);
        }
        return "cms/addProduct";
    }

    @PostMapping("addProduct")
    public String addProduct(String productId, String categoryType, String productName, Model model){
        Product product = new Product();
        product.setProductId(productId);
        product.setCategoryId(categoryType);
        product.setName(productName);

        int flag = catalogService.addProduct(product);
        if(flag == 0){
            model.addAttribute("msg","Add failed,please check again");
            return "common/error";
        }
        else {
            Category category = catalogService.getCategory(categoryType);
            List<Product> productList = catalogService.getProductListByCategory(categoryType);
            model.addAttribute("category", category);
            model.addAttribute("productList", productList);
            return "cms/manageProduct";
        }
    }

    @GetMapping("DeleteItem")
    public String deleteItem(String deleteItemId, String productId,String itemKeyword, Model model){
        if (deleteItemId != null) {
            if (productId != null) {
                catalogService.deleteItem(deleteItemId);
                catalogService.deleteInventory(deleteItemId);
                Product product = catalogService.getProduct(productId);
                List<Item> itemList = catalogService.getItemListByProduct(productId);
                model.addAttribute("product", product);
                model.addAttribute("itemList", itemList);
                return "cms/manageItem";
            }
            if (itemKeyword != null) {
                catalogService.deleteItem(deleteItemId);
                catalogService.deleteInventory(deleteItemId);
                List<Item> itemList = catalogService.searchItemList(itemKeyword.toLowerCase());
                model.addAttribute("itemList", itemList);
                model.addAttribute("itemKeyword", itemKeyword);
                return "cms/searchItem";
            }
        }
        return "cms/manageItem";
    }

    @GetMapping("ViewUpdateItem")
    public String viewUpdateItem(String updateItemId, Model model){
        if (updateItemId != null) {
            Item itemOfUpdate = catalogService.getItem(updateItemId);
            model.addAttribute("itemOfUpdate", itemOfUpdate);
        }
        return "cms/updateItem";
    }

    @PostMapping("UpdateItem")
    public String updateItem(String itemListPrice, String itemUnitCost,
                             String itemSupplier, String itemStatus,
                             String itemAttribute1, String itemAttribute2,
                             String itemAttribute3, String itemAttribute4,
                             String itemAttribute5, Model model){
        Item item = (Item)model.getAttribute("itemOfUpdate");
        BigDecimal tempItemListPrice = new BigDecimal(itemListPrice);
        BigDecimal tempItemUnitCost = new BigDecimal(itemUnitCost);

        item.setListPrice(tempItemListPrice);
        item.setUnitCost(tempItemUnitCost);
        item.setSupplierId(Integer.parseInt(itemSupplier));
        item.setStatus(itemStatus);
        item.setAttribute1(itemAttribute1);
        item.setAttribute2(itemAttribute2);
        item.setAttribute3(itemAttribute3);
        item.setAttribute4(itemAttribute4);
        item.setAttribute5(itemAttribute5);

        int flag = catalogService.updateItem(item);
        if(flag == 0){
            model.addAttribute("msg","Update failed,please check again");
            return "common/error";
        }
        else {
            String productId = item.getProductId();
            Product product = catalogService.getProduct(productId);
            List<Item> itemList = catalogService.getItemListByProduct(productId);
            model.addAttribute("product", product);
            model.addAttribute("itemList", itemList);
            return "cms/manageItem";
        }
    }

    @GetMapping("ViewAddItem")
    public String viewAddItem(String productId, Model model){
        if (productId != null) {
            Product product = catalogService.getProduct(productId);
            model.addAttribute("product", product);
        }
        return "cms/addItem";
    }

    @PostMapping("addItem")
    public String addItem(String itemId, String itemListPrice, String itemUnitCost,
                          String itemSupplier, String itemStatus,
                          String itemAttribute1, String itemAttribute2,
                          String itemAttribute3, String itemAttribute4,
                          String itemAttribute5, Model model){
        Item item = new Item();
        item.setItemId(itemId);

        Product tempProduct = (Product)model.getAttribute("product");
        String tempProductId = tempProduct.getProductId();

        item.setProductId(tempProductId);
        System.out.println("hahahhaa" + tempProductId);
        BigDecimal tempItemListPrice = new BigDecimal(itemListPrice);
        BigDecimal tempItemUnitCost = new BigDecimal(itemUnitCost);

        item.setListPrice(tempItemListPrice);
        item.setUnitCost(tempItemUnitCost);
        item.setSupplierId(Integer.parseInt(itemSupplier));
        item.setStatus(itemStatus);
        item.setAttribute1(itemAttribute1);
        item.setAttribute2(itemAttribute2);
        item.setAttribute3(itemAttribute3);
        item.setAttribute4(itemAttribute4);
        item.setAttribute5(itemAttribute5);

        int flag = catalogService.addItem(item);
        if(flag == 0){
            model.addAttribute("msg","Add failed,please check again");
            return "common/error";
        }
        else {
            Product product = catalogService.getProduct(tempProductId);
            List<Item> itemList = catalogService.getItemListByProduct(tempProductId);
            model.addAttribute("product", product);
            model.addAttribute("itemList", itemList);
            return "cms/manageItem";
        }
    }

    @PostMapping("searchProducts")
    public String searchProducts(String productKeyword,Model model){

        if (productKeyword == null || productKeyword.length() < 1) {
            String msg = "Please enter a keyword to search for, then press the search button.";
            model.addAttribute("msg", msg);
            return "common/error";
        } else {
            List<Product> productList = catalogService.searchProductList(productKeyword.toLowerCase());
            processProductDescription(productList);
            model.addAttribute("productList", productList);
            model.addAttribute("productKeyword", productKeyword);
            return "cms/searchProduct";
        }
    }

    @PostMapping("searchItems")
    public String searchItems(String itemKeyword,Model model){

        if (itemKeyword == null || itemKeyword.length() < 1) {
            String msg = "Please enter a keyword to search for, then press the search button.";
            model.addAttribute("msg", msg);
            return "common/error";
        } else {
            List<Item> itemList = catalogService.searchItemList(itemKeyword.toLowerCase());
            model.addAttribute("itemList", itemList);
            model.addAttribute("itemKeyword", itemKeyword);
            return "cms/searchItem";
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

