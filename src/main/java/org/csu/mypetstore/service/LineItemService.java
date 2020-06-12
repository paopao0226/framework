package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.LineItem;
import org.csu.mypetstore.persistence.ItemDao;
import org.csu.mypetstore.persistence.LineItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LineItemService {

    @Autowired
    LineItemDao lineItemDao;
    @Autowired
    ItemDao itemDao;

    public int insertLineItem(int orderId, int lineNum, String itemId, int quantity, BigDecimal unitPrice){
        return lineItemDao.insertLineItem(orderId,lineNum,itemId,quantity,unitPrice);
    }

    public List<LineItem> getLineItemsByOrderId(int orderId){
        List<LineItem>lineItemList = lineItemDao.getLineItemsByOrderId(orderId);
        //LineItemJava类和数据库里面的不同在于,多了item和quantity属性，所以将在Dao层取出来的lineitemlist进一步封装
        for(int i = 0;i < lineItemList.size();i++){
            String itemId = lineItemList.get(i).getItemId();
            lineItemList.get(i).setItem(itemDao.getItemByItemid(itemId));
        }
        return lineItemList;
    }
}
