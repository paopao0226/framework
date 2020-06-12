package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Daily;
import org.csu.mypetstore.domain.Order;
import org.csu.mypetstore.service.AccountService;
import org.csu.mypetstore.service.CatalogService;
import org.csu.mypetstore.service.DailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/daily")
@SessionAttributes(value = {"isLogin","myAccount","password","languageList","categoryList","mylanguagePreference","myfavouriteCategoryId","myListOpt","myBannerOpt"})
public class DailyController {
    @Autowired
    private DailyService dailyService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/selectDailyRecord")
    public String selectDailyRecord(Model model){
        return "daily/SelectDaily";
    }
    @RequestMapping(value = "/showDailyRecord",method = RequestMethod.GET)
    public String showDailyRecord(@RequestParam("kind") String kind,@RequestParam("username") String username,Model model){
        List<Daily> dailyList;
        if(kind.equals("all")){
            dailyList = dailyService.getDailyByUserId(username);
        }
        else {
            dailyList = dailyService.getDailyByUserIdAndKind(username,kind);
        }

        if(kind.equals("browse")){
            model.addAttribute("topic","My Browse Daily Record");
        }
        else if(kind.equals("cart")){
            model.addAttribute("topic","My Items Add to Cart Daily Record");
        }
        else if(kind.equals("order")){
            model.addAttribute("topic","My Order Daily Record");
        }
        else if(kind.equals("all")){
            model.addAttribute("topic","My Whole Daily Record");
        }
        else{
            model.addAttribute("selectMessage","your choose is wrong");
        }
        model.addAttribute("list",dailyList);
        return "daily/ShowDaily";
    }
}
