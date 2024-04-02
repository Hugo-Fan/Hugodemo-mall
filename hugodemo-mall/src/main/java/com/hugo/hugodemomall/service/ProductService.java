package com.hugo.hugodemomall.service;

import com.hugo.hugodemomall.dto.ProductRequest;
import com.hugo.hugodemomall.model.Product;

public interface ProductService {

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);
}
