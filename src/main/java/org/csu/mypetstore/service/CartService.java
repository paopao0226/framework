package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Cart;
import org.csu.mypetstore.domain.CartItem;
import org.csu.mypetstore.persistence.CartDao;
import org.csu.mypetstore.persistence.InventoryDao;
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

    @Autowired
    InventoryDao inventoryDao;


    public Cart getCarByUsername(String username){
        cart.setItemList(cartDao.getCartByUsername(username));
        return cart;
    }

    public int updateQuantity(String itemName, String userName,int inputQuantity,int preQuantity){
        int remainQuantity = inventoryDao.selectQuantity(itemName);
        //判断是从库存拿东西放入购物车，还是从购物车拿东西放入库存
        if(preQuantity < inputQuantity){//需要从库存拿东西放入购物车
            int needQuantity = inputQuantity - preQuantity;
            if(remainQuantity >= needQuantity){
                //将库存的相应item数量减少
                inventoryDao.updateQuantity(remainQuantity-needQuantity,itemName);
                //将购物车相应item的数量增加
                cartDao.updateQuantity(inputQuantity,userName,itemName);
            }
        }
        else if (preQuantity > inputQuantity){//需要将购物车的东西放入库存
            int overQuantity = preQuantity - inputQuantity;
            inventoryDao.updateQuantity(remainQuantity+overQuantity,itemName);
            cartDao.updateQuantity(inputQuantity,userName,itemName);
        }
        return remainQuantity;
    }

    public int deleteCartByUsernameAndItemid(String userName,String itemId){
        return cartDao.deleteCartByUsernameAndItemid(userName,itemId);
    }

    public int addItemToCart(String userName, String itemId, int quantity){
        //检查是否有库存
        int remainQuantity = inventoryDao.selectQuantity(itemId);
        if(remainQuantity >= quantity){
            //如果有库存则减少库存并且添加到购物车
            inventoryDao.updateQuantity(remainQuantity-quantity,itemId);
            CartItem cartItem = cartDao.selectCartItemByUsernameAndItemid(userName, itemId);
            if (cartItem != null)
                cartDao.updateQuantity(cartItem.getQuantity() + quantity, userName, itemId);
            else
                cartDao.addItemToCart(userName, itemId, quantity);
        }
            return remainQuantity;
    }

    public CartItem selectCartItemByUsernameAndItemid(String userName, String itemId) {
        return cartDao.selectCartItemByUsernameAndItemid(userName,itemId);
    }

    public int deleteAllCartItemByUsername(String userName){
        return cartDao.deleteAllCartItemByUsername(userName);
    }

}
