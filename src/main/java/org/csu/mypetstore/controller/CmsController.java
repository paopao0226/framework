package org.csu.mypetstore.controller;

import com.alipay.api.AlipayApiException;
import org.bouncycastle.math.raw.Mod;
import org.csu.mypetstore.domain.Order;
import org.csu.mypetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@SessionAttributes(value = {"cart","order","isLogin","myAccount","orderList","msg","orderOfUpdate","AllOrderList"})//model的attribution通过该注释将对象放在了session作用域中，以后通过model.getAttribute可以取出对象
@RequestMapping("/cms/")
public class CmsController {


    @Autowired
    OrderService orderService;

    @GetMapping("viewMain")
    public String viewMain(){
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
}

