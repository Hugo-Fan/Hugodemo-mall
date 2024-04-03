package com.hugo.hugodemomall.dao;

import com.hugo.hugodemomall.constant.ProductCategory;
import com.hugo.hugodemomall.dto.ProductRequest;
import com.hugo.hugodemomall.model.Product;

import java.util.List;

public interface ProductDao {

    List<Product> getProducts(ProductCategory category,String search);

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId,ProductRequest productRequest);

    void deleteProductById(Integer productId);
}
