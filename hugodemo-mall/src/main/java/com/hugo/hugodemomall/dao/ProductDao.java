package com.hugo.hugodemomall.dao;

import com.hugo.hugodemomall.dto.ProductRequest;
import com.hugo.hugodemomall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

}
