package com.hugo.hugodemomall.service;

import com.hugo.hugodemomall.dto.ProductRequest;
import com.hugo.hugodemomall.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getProducts();

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId,ProductRequest productRequest);

    void deleteProductById(Integer productId);
}
