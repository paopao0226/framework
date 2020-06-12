package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.LineItem;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LineItemDao {
    int insertLineItem(int orderId, int lineNum, String itemId, int quantity, BigDecimal unitPrice);

    List<LineItem> getLineItemsByOrderId(int orderId);
}
