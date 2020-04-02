package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.Cart;
import org.csu.mypetstore.domain.CartItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDao {

    public List<CartItem> getCartByUsername(String username);

    public int updateQuantity(int quantity, String username, String itemId);

    public int deleteCartByUsernameAndItemid(String userName, String itemId);

    public int addItemToCart(String userName, String itemId, int quantity);

    public CartItem selectCartItemByUsernameAndItemid(String userName, String itemId);

    public int deleteAllCartItemByUsername(String userName);
}
