package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.service.AccountService;
import org.csu.mypetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private CatalogService catalogService;
    @GetMapping("/signon")
    public String signon() {
        return "account/SignonForm";
    }
    @PostMapping("/sign")
    public String sign(String username, String password, String verifi, Model model){
        //这块缺验证码的验证
        Account account = accountService.getAccount(username,password);
//        if(!clientCheckcode.equals(serverCheckcode)){
//            System.out.println(clientCheckcode +" " + serverCheckcode);
//            String value = "<ui><li>Failed Verification</li></ui>";
//            session.setAttribute("loginMessage",value);
//            request.getRequestDispatcher(signonFormString).forward(request,response);
//        }
        if(account == null){
            String value = "<ui><li>Invalid username or password. Sign on failed</li></ui>";
            model.addAttribute("loginMessage",value);
            return "account/signonForm";
        }
        else {
            boolean isLogin = true;
            model.addAttribute("loginMessage","");
            model.addAttribute("isLogin",isLogin);
            model.addAttribute("myAccount",account);
            System.out.println(account.getFavouriteCategoryId());
            return "catalog/main";
        }
    }
    @GetMapping("/newAccountForm")
    public String newAccountForm(){
        System.out.println(1);
        return "account/NewAccountForm";
    }
    @GetMapping("/newAccount")
    public String newAcount(String password,String repeadedPassword,String verifi,Account account,Model model) {
        //这里缺验证码的验证功能
//        if (!clientCheckcode.equals(serverCheckcode)) {
//            System.out.println(clientCheckcode + " " + serverCheckcode);
//            String value = "<ui><li>Failed Verification</li></ui>";
//            session.setAttribute("registerMessage", value);
//            request.getRequestDispatcher(newAccountFormString).forward(request, response);
//        }
        if (!password.equals(repeadedPassword)) {
            String value = "<ui><li>Register failed,Please check the password and repeatPassword</li></ui>";
            model.addAttribute("registerMessage", value);
            return "account/NewAccountForm";
        }
//        else {
//            if (account.getFirstName() == null || account.getLastName() == null || request.getParameter("account.address1") == null
//                    || request.getParameter("account.city") == null || request.getParameter("account.state") == null || request.getParameter("account.zip") == null
//                    || request.getParameter("account.country") == null || request.getParameter("account.phone") == null) {
//                String value = "<ui><li>Information missed. Please check the information</li></ui>";
//                session.setAttribute("username", request.getParameter("username"));
//                session.setAttribute("password", request.getParameter("password"));
//                session.setAttribute("repeatedPassword", request.getParameter("password"));
//                session.setAttribute("registerMessage", value);
//                request.getRequestDispatcher(newAccountFormString).forward(request, response);
//            } else {
//                account.setUsername(request.getParameter("username"));
//                account.setPassword(request.getParameter("password"));
//                account.setFirstName(request.getParameter("account.firstName"));
//                account.setLastName(request.getParameter("account.lastName"));
//                account.setEmail(request.getParameter("account.email"));
//                account.setPhone(request.getParameter("account.phone"));
//                account.setAddress1(request.getParameter("account.address1"));
//                account.setAddress2(request.getParameter("account.address2"));
//                account.setCity(request.getParameter("account.city"));
//                account.setState(request.getParameter("account.state"));
//                account.setZip(request.getParameter("account.zip"));
//                account.setCountry(request.getParameter("account.country"));
//                account.setLanguagePreference(request.getParameter("account.language"));
//                account.setFavouriteCategoryId(request.getParameter("account.favorite"));
//                account.setListOption(Boolean.parseBoolean(request.getParameter("account.listOption")));
//                account.setBannerOption(Boolean.parseBoolean(request.getParameter("account.bannerOption")));
//                accountService.insertAccount(account);
//                session.setAttribute("username", request.getParameter("username"));
//                request.getRequestDispatcher(signonFormString).forward(request, response);
//            }
//        }
        return null;
    }
}
