package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.service.AccountService;
import org.csu.mypetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
public class OtherController {

    @Autowired
    private CatalogService catalogService;
    @Autowired
    private AccountService accountService;


    @GetMapping("/searchProductUpdate")
    @ResponseBody
    public void completeSearch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String keyword = request.getParameter("keyword");
        //向server层调用相应的业务
        List<Product> productList = catalogService.searchProductList(keyword);

        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();

        //返回结果
        String res = "";
        for(int i=0; i<productList.size(); i++){
            if(i>0){
                res += "," + productList.get(i).getName();
            }else{
                res += productList.get(i).getName();
            }
        }
        out.write(res);

        out.flush();
        out.close();
    }
}
