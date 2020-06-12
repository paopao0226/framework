package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Category;
import org.csu.mypetstore.domain.Item;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.persistence.CategoryMapper;
import org.csu.mypetstore.persistence.InventoryDao;
import org.csu.mypetstore.persistence.ItemMapper;
import org.csu.mypetstore.persistence.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CatalogService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private InventoryDao inventoryDao;

    public Category getCategory(String categoryId) {
        return categoryMapper.getCategory(categoryId);
    }

    public List<Product> getProductListByCategory(String category) {
        return productMapper.getProductListByCategory(category);
    }

    public Product getProduct(String productId) {
        return productMapper.getProduct(productId);
    }

    public List<Product> searchProductList(String keywords) {
        return productMapper.searchProductList( "%" + keywords + "%");
    }

    public int getInventoryQuantity(String itemId) {
        return itemMapper.getInventoryQuantity(itemId);
    }

    public List<Item> getItemListByProduct(String productId) {
        return itemMapper.getItemListByProduct(productId);
    }

    public Item getItem(String itemId) {
        return itemMapper.getItem(itemId);
    }

    public List<Category> getAllCategorys() { return categoryMapper.getAllCategory(); }

    public List<Product> getAllProducts() { return productMapper.getAllProducts(); }

    public void deleteItemByProductId(String productId) { itemMapper.deleteItemByProductId(productId); }

    public void deleteProduct(String productId) { productMapper.deleteProduct(productId); }

    public int updateProduct(Product product){ return productMapper.updateProduct(product); }

    public int addProduct(Product product){
        return productMapper.addProduct(product);
    }

    public void deleteItem(String itemId) { itemMapper.deleteItem(itemId); }

    public void deleteInventory(String itemId) { inventoryDao.deleteInventory(itemId); }

    public int updateItem(Item item) { return itemMapper.updateItem(item); }

    public int addItem(Item item) { return itemMapper.addItem(item); }

    public List<Item> searchItemList(String keywords) {
        return itemMapper.searchItemList( "%" + keywords + "%");
    }
}
