package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.Item;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDao {
    Item getItemByItemid(String itemId);
}
