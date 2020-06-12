package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMapper {
    //查询产品列表
    List<Product> getProductListByCategory(String categoryId);
    //获取产品
    Product getProduct(String productId);
    //关键词模糊查询
    List<Product> searchProductList(String keywords);
    //获取所有的产品
    List<Product> getAllProducts();
    //删除产品
    void deleteProduct(String productId);

    int updateProduct(Product product);

    int addProduct(Product product);
}
