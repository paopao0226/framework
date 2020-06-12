package org.csu.mypetstore.persistence;

import org.springframework.stereotype.Repository;

@Repository
public interface InventoryDao {
    public int selectQuantity(String itemId);

    public int updateQuantity(int quantity,String itemId);

    public void deleteInventory(String itemId);
}
