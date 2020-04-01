package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Order;
import org.csu.mypetstore.persistence.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService  {


    @Autowired
    OrderDao orderDao;

    public int insertOrder(Order order){
        return orderDao.insertOrder(order);
    }
}
