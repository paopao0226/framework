package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Category;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.persistence.CategoryMapper;
import org.csu.mypetstore.persistence.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService{

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ProductMapper productMapper;

    public Category getCategory(String categoryId){
        return categoryMapper.getCategory(categoryId);
    }
    public Product getProduct(String productId){
        return productMapper.getProduct(productId);
    }
    public List<Product> getProductListByCategory(String categoryId){
        return productMapper.getProductListByCategory(categoryId);
    }
}
