package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Cart;
import org.csu.mypetstore.domain.CartItem;
import org.csu.mypetstore.persistence.CartDao;
import org.csu.mypetstore.persistence.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CartService {

    @Autowired
    CartDao cartDao;

    @Autowired
    Cart cart;


    public Cart getCarByUsername(String username){
        cart.setItemList(cartDao.getCartByUsername(username));
        return cart;
    }

    public boolean updateQuantity(String itemName, String userName,int inputQuantity){
        int flag = cartDao.updateQuantity(inputQuantity,userName,itemName);
        if(flag > 0)
            return true;
        else
            return false;
    }

    public int deleteCartByUsernameAndItemid(String userName,String itemId){
        return cartDao.deleteCartByUsernameAndItemid(userName,itemId);
    }

    public void addItemToCart(String userName, String itemId, int quantity){
        CartItem cartItem = cartDao.selectCartItemByUsernameAndItemid(userName, itemId);
        if (cartItem != null)
            cartDao.updateQuantity(cartItem.getQuantity() + quantity, userName, itemId);
        else
            cartDao.addItemToCart(userName, itemId, quantity);
    }

    public CartItem selectCartItemByUsernameAndItemid(String userName, String itemId) {
        return cartDao.selectCartItemByUsernameAndItemid(userName,itemId);
    }

    public int deleteAllCartItemByUsername(String userName){
        return cartDao.deleteAllCartItemByUsername(userName);
    }
}
