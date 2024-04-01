package com.hugo.hugodemomall.service.impl;

import com.hugo.hugodemomall.dao.ProductDao;
import com.hugo.hugodemomall.model.Product;
import com.hugo.hugodemomall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }
}
