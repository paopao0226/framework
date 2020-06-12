package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Order;
import org.csu.mypetstore.persistence.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService  {


    @Autowired
    OrderDao orderDao;

    public List<Order> getOrdersByUsername(String username) {
        return orderDao.getOrdersByUsername(username);
    }

    public Order getOrdersByOrderId(String orderId) {
        return orderDao.getOrdersByOrderId(orderId);
    }

    public Order getOrder(int orderId){
        return orderDao.getOrder(orderId);
    }

    public int insertOrder(Order order){
        return orderDao.insertOrder(order);
    }

    public int updateOrder(Order order){
        return orderDao.updateOrder(order);
    }

    public int deleteOrder(int orderId){
        return orderDao.deleteOrder(orderId);
    }

    public int insertOrderState(int orderid, int lineNum, Date timeStamp, int status){
        return orderDao.insertOrderState(orderid,lineNum,timeStamp,status);
    }

    public int updateOrderState(int state,int orderId){
        return orderDao.updateOrderState(state,orderId);
    }

    public List<Order> getAllOrders() {
        return orderDao.getAllOrders();
    }

    public List<Order> getOrdersByKeyword(String keyword){
        return orderDao.getOrderByKeyword(keyword);
    }
}
