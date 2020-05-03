package org.csu.mypetstore.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import org.csu.mypetstore.domain.AlipayConfig;
import org.csu.mypetstore.domain.Cart;
import org.csu.mypetstore.domain.Order;
import org.csu.mypetstore.service.CartService;
import org.csu.mypetstore.service.LineItemService;
import org.csu.mypetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.alipay.api.AlipayConstants.*;
import static org.apache.catalina.manager.Constants.CHARSET;

@Controller
@SessionAttributes(value = {"cart","order","isLogin","myAccount","orderList","msg","orderOfUpdate"})//model的attribution通过该注释将对象放在了session作用域中，以后通过model.getAttribute可以取出对象
@RequestMapping("/order/")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    CartService cartService;

    @Autowired
    LineItemService lineItemService;

    @GetMapping("newOrder")//点击购物车下方的checkOut访问的servlet
    public String newOrder(){//方法里面的参数username可以自动匹配url的参数
        return "order/newOrder";
    }

    @PostMapping("confirmOrder")//点击填写好订单提交后访问的servlet
    public String confirmOrder(Order order, Model model){//方法里面的参数username可以自动匹配url的参数
        //在model中添加order对象
        model.addAttribute("order",order);
        return "order/confirmOrder";
    }

    @GetMapping("viewOrder")//confirm订单后访问的servlet
    public String viewOrder(Model model,String username,String orderId){
        //使用if-else语句实现order点击来源的判断
        //这个来源是购物车order
        if(orderId.equals("-1")) {
            Cart cart = (Cart) model.getAttribute("cart");
            Order order = (Order) model.getAttribute("order");
            order.setUsername(username);
            SimpleDateFormat sdf = new SimpleDateFormat();//date格式设置
            sdf.applyPattern("yyyy-MM-dd");
            Date date = new Date();
            //给Order的当前时间属性赋值，防止后面数据库插入操作时报错NUll
            order.setOrderDate(date);
            //将购物车中的SubTotal属性赋给order的TotalPrice属性
            order.setTotalPrice(((Cart) model.getAttribute("cart")).getSubTotal());
            //数据库插入一条order
            orderService.insertOrder(order);
            //当往数据库插入一条order以后，再通过返回的orderId将相应的lineItem也插入数据库
            for (int i = 0; i < cart.getItemList().size(); i++) {
                lineItemService.insertLineItem(order.getOrderId(), i + 1, cart.getItemList().get(i).getItemId(), cart.getItemList().get(i).getQuantity(), cart.getItemList().get(i).getItem().getListPrice());
            }
            //增加一个order状态,0代表暂未支付
            orderService.insertOrderState(order.getOrderId(),cart.getItemList().size(),order.getOrderDate(),0);
            // 当往数据库插入order以后，再将调用lineItemService层的方法来设置order的LineItemList
            order.setLineItems(lineItemService.getLineItemsByOrderId(order.getOrderId()));
            BigDecimal totalPrice = cart.getSubTotal();
            //确认提交订单后清空购物车
            cartService.deleteAllCartItemByUsername(username);
            ((Cart) model.getAttribute("cart")).setItemList(null);
        }
        else {
            Order order = orderService.getOrder(Integer.parseInt(orderId));
            //这里只读出了一部分的信息，我也不知道为啥
            order.setLineItems(lineItemService.getLineItemsByOrderId(Integer.parseInt(orderId)));
            model.addAttribute("order",order);
        }
        return "order/viewOrder";
    }

    //查看当前用户的orderHistory
    @GetMapping("viewOrderList")
    public String viewOrderList(String userName,Model model){
        //通过userName在数据库中拿出orderList
        List<Order> orderList = orderService.getOrdersByUsername(userName);
        //将orderlist放入model，并且把作用域设置为session
        model.addAttribute("orderList",orderList);
        return "order/orderList";
    }

    //查看orderHistroy中特定的一个order以便进行操作
    @GetMapping("viewUpdateOrder")
    public String viewUpdateOrder(String userName,String orderId,Model model) throws AlipayApiException {//进入updateOrder的页面
        //从数据库中拿出当前的order并且将其放入model中，方便前端进行阅览
        Order orderOfUpdate = orderService.getOrder(Integer.parseInt(orderId));
        model.addAttribute("orderOfUpdate",orderOfUpdate);
        return "order/updateOrder";
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
        else
            return "order/orderList";
    }

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
            List<Order> orderList = orderService.getOrdersByUsername(userName);
            model.addAttribute("orderList",orderList);
            return  "order/orderList";
        }
    }

}
