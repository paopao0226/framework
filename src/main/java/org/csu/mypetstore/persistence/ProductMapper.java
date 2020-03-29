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
}
