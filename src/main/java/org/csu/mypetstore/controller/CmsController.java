package org.csu.mypetstore.controller;

import com.alipay.api.AlipayApiException;
import org.bouncycastle.math.raw.Mod;
import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Order;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.service.AccountService;
import org.csu.mypetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@SessionAttributes(value = {"cart","order","isLogin","myAccount","controlAccount","orderList","msg","orderOfUpdate","AllOrderList","AllAccountList","languageList","categoryList","mylanguagePreference","myfavouriteCategoryId","myListOpt","myBannerOpt"})//model的attribution通过该注释将对象放在了session作用域中，以后通过model.getAttribute可以取出对象
@RequestMapping("/cms/")
public class CmsController {


    @Autowired
    OrderService orderService;
    @Autowired
    AccountService accountService;
    //暂时保存查看的用户名
    String username;

    @GetMapping("viewMain")
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
        return "cms/main";
    }

    @GetMapping("ViewManageOrder")
    public String manageOrder(Model model){
        List<Order> AllOrderList = orderService.getAllOrders();
        model.addAttribute("AllOrderList",AllOrderList);
        return "cms/manageOrder";
    }

    @GetMapping("viewUpdateOrder")
    public String viewUpdateOrder(String userName,String orderId,Model model){
        //从数据库中拿出当前的order并且将其放入model中，方便前端进行阅览
        Order orderOfUpdate = orderService.getOrder(Integer.parseInt(orderId));
        model.addAttribute("orderOfUpdate",orderOfUpdate);
        return "cms/updateOrder";
    }

    //点击修改订单按钮后访问的方法
    @PostMapping("updateOrder")
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
    @GetMapping("deleteOrder")
    public String deleteOrder(String orderId, String userName, Model model){
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
    public String searchOrders(String keyword,Model model){
        //判断keyword指的是订单号还是用户名，这边默认用户名必须包含字母,所以如果keyword为纯数字的话指的就是订单号
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence)keyword);
        boolean result=matcher.matches();
        if (result == true) {
            List<Order> AllOrderList = new ArrayList<>();
            AllOrderList.add(orderService.getOrdersByOrderId(keyword));
            model.addAttribute("AllOrderList",AllOrderList);
        }else{
            List<Order> AllOrderList = orderService.getOrdersByUsername(keyword);
            model.addAttribute("AllOrderList",AllOrderList);
        }
        return "cms/manageOrder";
    }

    @GetMapping("ajaxSearchOrders")
    @ResponseBody
    public String ajaxSearchOrders(String keyword){
        System.out.println("----------------服务器接受到请求------------------");
        List<Order> searchOrderList = orderService.getOrdersByKeyword('%'+ keyword+ '%');
        StringBuffer result = new StringBuffer();
        for(int i = 0 ; i < searchOrderList.size();i++){
            if(i == searchOrderList.size() -1){
                result.append(((Order)searchOrderList.get(i)).getOrderId() + ",");
                result.append(((Order)searchOrderList.get(i)).getUsername());
            }
            result.append(((Order)searchOrderList.get(i)).getOrderId() + ",");
            result.append(((Order)searchOrderList.get(i)).getUsername() + ",");
            System.out.println("----------------searchOrderList.size(------------------");
            System.out.println(searchOrderList.size());
        }
        return result.toString();
    }

    @GetMapping("ViewManageAccount")
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

    @PostMapping("searchAccounts")
    public String searchAccount(String keyword,Model model){
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
    @GetMapping("deleteAccount")
    public String deleteAccount(String username,Model model){
        accountService.deleteAccount(username);
        List<Account> AllAccountList = accountService.getAllAccount();
        model.addAttribute("AllAccountList",AllAccountList);
        return "cms/manageAccount";
    }
    @GetMapping("viewUpdateAccount")
    public String viewUpdateAccount(String userName,Model model){
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
    @PostMapping("editAccount")//编辑
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
}

