package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Cart;
import org.csu.mypetstore.domain.CartItem;
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
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@SessionAttributes(value = {"cart","order","msg"})//model的attribution通过该注释将对象放在了session作用域中，以后通过model.getAttribute可以取出对象
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    OrderService orderService;

    @Autowired
    LineItemService lineItemService;


    @GetMapping("viewCart")//点击购物车图标后访问的servlet
    public String getCart(String username, Model model){//方法里面的参数username可以自动匹配url的参数
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

    @GetMapping("viewOrder")//confirm订单后访问的servlet
    public String viewOrder(Model model){
        Cart cart = (Cart) model.getAttribute("cart");
        Order order = (Order) model.getAttribute("order");
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
        for(int i = 0 ; i < cart.getItemList().size();i++){
            lineItemService.insertLineItem(order.getOrderId(),i+1,cart.getItemList().get(i).getItemId(),cart.getItemList().get(i).getQuantity(),cart.getItemList().get(i).getItem().getUnitCost());
        }
        // 当往数据库插入order以后，再将调用lineItemService层的方法来设置order的LineItemList
        order.setLineItems(lineItemService.getLineItemsByOrderId(order.getOrderId()));
        BigDecimal totalPrice = cart.getSubTotal();
        //确认提交订单后清空购物车
        cartService.deleteAllCartItemByUsername("ywj");
        ((Cart)model.getAttribute("cart")).setItemList(null);
        return "order/viewOrder";
    }

    @GetMapping("changeInputQuantity")//前端修改购物车item的quantity的时候，负责处理ajax请求的servlet
    @ResponseBody//这个注释说明了方法的返回值是response的body里面的数据，从而不会被thymeleaf当作网页解析
    public String changeInputQuantity(String inputQuantity, String userName, String itemName, String preQuantity, Model model) throws IOException {
        if(Integer.parseInt(preQuantity) !=  Integer.parseInt(inputQuantity)) {//需要进行库存和购物车的更新
            //进行购物车还有库存的更新，并且返回未更新前库存的剩余数量,方便当库存不足时，发送库存信息
            int remainQuantity = cartService.updateQuantity(itemName,userName,Integer.parseInt(inputQuantity),Integer.parseInt(preQuantity));
            if(Integer.parseInt(preQuantity) <  Integer.parseInt(inputQuantity)){//如何需要从库存拿东西到购物车
                if(remainQuantity >= Integer.parseInt(inputQuantity) -  Integer.parseInt(preQuantity)){//如果库存足够
                    // 更新model中的cart
                    Cart cart;
                    cart = cartService.getCarByUsername(userName);
                    BigDecimal totalPrice = cart.getSubTotal();
                    ((Cart)model.getAttribute("cart")).setItemList(cart.getItemList());
                     return String.valueOf(totalPrice);
                }
                else//如果库存不足
                    return "Add failure caused by no enough remaining quantity and the remain numbers : " + remainQuantity;
            }
            else {//如果需要把购物车的东西放入库存
                // 更新model中的cart
                Cart cart;
                cart = cartService.getCarByUsername(userName);
                BigDecimal totalPrice = cart.getSubTotal();
                ((Cart)model.getAttribute("cart")).setItemList(cart.getItemList());
                return String.valueOf(totalPrice);
            }
        }
        else//如果前后数量一样，根本不需要更新，就返回null
            return null;

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
        int remainQuantity = cartService.addItemToCart(userName,itemId,Integer.parseInt(quantity));
        if(remainQuantity >= Integer.parseInt(quantity)){
            if((Cart)model.getAttribute("cart") != null)
                ((Cart)model.getAttribute("cart")).setItemList(cartService.getCarByUsername(userName).getItemList());
            else{
                Cart cart = cartService.getCarByUsername(userName);
                //在model中添加cart对象
                model.addAttribute("cart",cart);
            }
            return "cart/cart";
        }
        else{
            model.addAttribute("msg","Add failure caused by no enough remaining quantity and the remain numbers : " + remainQuantity);
            return "common/error";
        }
    }
}

