package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.*;
import org.csu.mypetstore.service.CartService;
import org.csu.mypetstore.service.DailyService;
import org.csu.mypetstore.service.LineItemService;
import org.csu.mypetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
//这里添加了三个新的session对象
@SessionAttributes(value = {"cart","order","isLogin","myAccount","orderList"})//model的attribution通过该注释将对象放在了session作用域中，以后通过model.getAttribute可以取出对象
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    OrderService orderService;

    @Autowired
    LineItemService lineItemService;
    //dailyService用于日志记录
    @Autowired
    DailyService dailyService;


    @GetMapping("viewCart")//点击购物车图标后访问的servlet
    public String getCart(String username, Model model){//方法里面的参数username可以自动匹配url的参数
        //如果没有登录
        Cart cart = cartService.getCarByUsername(username);
        //在model中添加cart对象
        model.addAttribute("cart",cart);
        return "cart/cart";
    }

    @GetMapping("newOrder")//点击购物车下方的checkOut访问的servlet
    public String newOrder(){//方法里面的参数username可以自动匹配url的参数
        return "order/newOrder";
    }

    @PostMapping("confirmOrder")//点击填写好订单提交后访问的servlet
    public String confirmOrder(Order order,Model model){//方法里面的参数username可以自动匹配url的参数
        //在model中添加order对象
        model.addAttribute("order",order);
        return "order/confirmOrder";
    }
    @GetMapping("viewOrderList")//点击我的订单显示订单信息
    public String viewOrderList(String username,Model model){
        boolean isLogin = (boolean) model.getAttribute("isLogin");
        if(!isLogin) return "account/SignonForm";
        System.out.println(username);
        List<Order> orderList = new ArrayList<>();
        orderList = orderService.getOrdersByUsername(username);
        System.out.println(orderList.size());
        model.addAttribute("orderList",orderList);
        return "order/viewOrderList";
    }
    @GetMapping("viewOrder")//confirm订单后访问的servlet
    public String viewOrder(String orderId, Model model,String username){
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
                lineItemService.insertLineItem(order.getOrderId(), i + 1, cart.getItemList().get(i).getItemId(), cart.getItemList().get(i).getQuantity(), cart.getItemList().get(i).getItem().getUnitCost());
            }
            // 当往数据库插入order以后，再将调用lineItemService层的方法来设置order的LineItemList
            order.setLineItems(lineItemService.getLineItemsByOrderId(order.getOrderId()));
            BigDecimal totalPrice = cart.getSubTotal();
            //确认提交订单后清空购物车
            cartService.deleteAllCartItemByUsername(username);
            ((Cart) model.getAttribute("cart")).setItemList(null);
            List<Order> list = orderService.getOrdersByUsername(username);
            //记录日志
            int orderID = list.get(list.size()-1).getOrderId();
            java.sql.Timestamp time =  new Timestamp(date.getTime());
            String kind = "order";
            Daily daily = new Daily();
            daily.setUserId(username);
            daily.setDatetime(time);
            daily.setKind(kind);
            daily.setOrderId(orderID);
            dailyService.insertDaily(daily);

        }
        else {
            Order order = orderService.getOrder(Integer.parseInt(orderId));
            //这里只读出了一部分的信息，我也不知道为啥
            order.setLineItems(lineItemService.getLineItemsByOrderId(Integer.parseInt(orderId)));
            model.addAttribute("order",order);
        }
        return "order/viewOrder";
    }

    @GetMapping("changeInputQuantity")//前端修改购物车item的quantity的时候，负责处理ajax请求的servlet
    @ResponseBody//这个注释说明了方法的返回值是response的body里面的数据，从而不会被thymeleaf当作网页解析
    public String changeInputQuantity(String inputQuantity, String userName, String itemName, Model model) throws IOException {
        boolean flag = cartService.updateQuantity(itemName,userName,Integer.parseInt(inputQuantity));
        if(flag){
            //更新model中的cart
            Cart cart;
            cart = cartService.getCarByUsername(userName);
            BigDecimal totalPrice = cart.getSubTotal();
            ((Cart)model.getAttribute("cart")).setItemList(cart.getItemList());
            return String.valueOf(totalPrice);
        }
        else {
            return null;
        }
    }

    @GetMapping("deleteCartItem")//在购物车点击remove item后访问的servlet
    public String deleteCartItem(String userName,String itemId,Model model){
        cartService.deleteCartByUsernameAndItemid(userName,itemId);
        Cart cart;
        cart = cartService.getCarByUsername(userName);
        //刷新model里面的cart对象
        ((Cart)model.getAttribute("cart")).setItemList(cart.getItemList());
        return "cart/cart";
    }

    @GetMapping("addItemToCart")
    public String addItemToCart(String userName, String itemId, String quantity, Model model){
        boolean isLogin = (boolean) model.getAttribute("isLogin");
        //如果没有登录则转回main
        if(!isLogin){
            return "catalog/main";
        }
        System.out.println("------------------------------");
        System.out.println("yes");
        cartService.addItemToCart(userName,itemId,Integer.parseInt(quantity));
        if((Cart)model.getAttribute("cart") != null)
            ((Cart)model.getAttribute("cart")).setItemList(cartService.getCarByUsername(userName).getItemList());
        else{
            Cart cart = cartService.getCarByUsername(userName);
            //在model中添加cart对象
            model.addAttribute("cart",cart);
        }
        //记录日志
        if(isLogin) {
            java.util.Date date = new java.util.Date();
            java.sql.Timestamp time =  new Timestamp(date.getTime());
            String kind = "cart";
            String items = itemId;
            Daily daily = new Daily();
            daily.setUserId(userName);
            daily.setDatetime(time);
            daily.setKind(kind);
            daily.setItems(items);
            dailyService.insertDaily(daily);
        }
        return "cart/cart";
    }
}

