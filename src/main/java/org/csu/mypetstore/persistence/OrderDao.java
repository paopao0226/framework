package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderDao  {
    List<Order> getOrdersByUsername(String username);

    Order getOrder(int orderId);

    //这里的int返回的是数据库中影响的行数，并不是所在的行数。
    //如果要返回所在的行数，可以通过Mapper里面的keyProperty设置Java类的主键属性，当数据库insert成功后，会自动将新插入数据的行数返回到这个主键属性中。
    int insertOrder(Order order);

    void insertOrderStatus(Order order);

    int updateOrder(Order order);

    int deleteOrder(int orderId);

    int insertOrderState(int orderid, int lineNum, Date timeStamp, int status);

    int updateOrderState(int state,int orderId);

    List<Order> getAllOrders();

    Order getOrdersByOrderId(String orderId);

    List<Order> getOrderByKeyword(String keyword);
}
