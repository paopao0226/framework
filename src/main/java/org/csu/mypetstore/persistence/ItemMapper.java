package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ItemMapper {

    int getInventoryQuantity(String itemId);

    List<Item> getItemListByProduct(String productId);

    Item getItem(String itemId);

    void deleteItemByProductId(String productId);

    void deleteItem(String itemId);

    int updateItem(Item item);

    int addItem(Item item);

    List<Item> searchItemList(String keywords);
}
