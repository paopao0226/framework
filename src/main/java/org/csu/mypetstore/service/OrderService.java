package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Order;
import org.csu.mypetstore.persistence.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService  {


    @Autowired
    OrderDao orderDao;

    public List<Order> getOrdersByUsername(String username) {
        return orderDao.getOrdersByUsername(username);
    }
    public Order getOrder(int orderId){
        return orderDao.getOrder(orderId);
    }
    public int insertOrder(Order order){
        return orderDao.insertOrder(order);
    }
}
