package org.csu.mypetstore.AOP;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.csu.mypetstore.domain.Daily;
import org.csu.mypetstore.service.DailyService;
import org.csu.mypetstore.service.OrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.sql.Timestamp;

@Aspect
@Component
public class AspectJ {
    @Autowired
    DailyService dailyService;
    @Autowired
    OrderService orderService;
    @Before(value = "execution(public * org.csu.mypetstore.controller.CatalogController.viewItem(..))")
    public void beforeBrowser(JoinPoint joinPoint){
        //1.这里获取到所有的参数值的数组
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        //2.最关键的一步:通过这获取到方法的所有参数名称的字符串数组
        String[] parameterNames = methodSignature.getParameterNames();
        int usernameIndex = ArrayUtils.indexOf(parameterNames, "userId");
        int itemIndex = ArrayUtils.indexOf(parameterNames,"itemId");
        String username = (String) args[usernameIndex];
        System.out.println("username: " + username.getClass());
        if(!username.equals("null")){
            String itemId = (String) args[itemIndex];
            java.util.Date date = new java.util.Date();
            Timestamp time =  new Timestamp(date.getTime());
            String kind = "browse";
            String items = itemId;
            Daily daily = new Daily();
            daily.setUserId(username);
            daily.setDatetime(time);
            daily.setKind(kind);
            daily.setItems(items);
            dailyService.insertDaily(daily);
        }
    }

    @Before(value = "execution(public * org.csu.mypetstore.controller.CartController.addItemToCart(..))")
    public void beforeCart(JoinPoint joinPoint){
        //1.这里获取到所有的参数值的数组
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        //2.最关键的一步:通过这获取到方法的所有参数名称的字符串数组
        String[] parameterNames = methodSignature.getParameterNames();
        int usernameIndex = ArrayUtils.indexOf(parameterNames, "userName");
        int itemIndex = ArrayUtils.indexOf(parameterNames,"itemId");
        String username = (String) args[usernameIndex];
        if(!username.equals("null")){
            String itemId = (String) args[itemIndex];
            //记录日志
            java.util.Date date = new java.util.Date();
            java.sql.Timestamp time =  new Timestamp(date.getTime());
            String kind = "cart";
            String items = itemId;
            Daily daily = new Daily();
            daily.setUserId(username);
            daily.setDatetime(time);
            daily.setKind(kind);
            daily.setItems(items);
            dailyService.insertDaily(daily);
        }
    }

    @Before(value = "execution(public * org.csu.mypetstore.controller.CartController.viewOrder(..))")
    public void beforeOrder(JoinPoint joinPoint){
        //1.这里获取到所有的参数值的数组
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        //2.最关键的一步:通过这获取到方法的所有参数名称的字符串数组
        String[] parameterNames = methodSignature.getParameterNames();
        int usernameIndex = ArrayUtils.indexOf(parameterNames, "username");
        int orderIdIndex = ArrayUtils.indexOf(parameterNames,"orderId");
        String username = (String) args[usernameIndex];
        System.out.println(username);
        if(!username.equals("null")){
            String orderId = (String) args[orderIdIndex];
                //记录日志
                java.util.Date date = new java.util.Date();
                Timestamp time = new Timestamp(date.getTime());
                String kind = "order";
                int orderID = orderService.getOrdersByUsername(username).size() + 1;
                Daily daily = new Daily();
                daily.setUserId(username);
                daily.setDatetime(time);
                daily.setKind(kind);
                daily.setOrderId(orderID);
                dailyService.insertDaily(daily);
            }
    }
}

