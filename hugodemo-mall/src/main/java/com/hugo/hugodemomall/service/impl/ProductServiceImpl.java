package com.hugo.hugodemomall.service.impl;

import com.hugo.hugodemomall.dao.MemberDao;
import com.hugo.hugodemomall.dao.ProductDao;
import com.hugo.hugodemomall.dto.ProductQueryParams;
import com.hugo.hugodemomall.dto.ProductRequest;
import com.hugo.hugodemomall.model.Member;
import com.hugo.hugodemomall.model.Product;
import com.hugo.hugodemomall.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MemberDao memberDao;

    private final static Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        return productDao.countProduct(productQueryParams);
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        return productDao.getProducts(productQueryParams);
    }

    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }

    @Override
    public Integer createProduct(Integer userId,ProductRequest productRequest) {

        Member member = memberDao.getMemberById(userId);

        if(member ==null){
            log.warn("不存在的 {} userId",userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return productDao.createProduct(productRequest);
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        productDao.updateProduct(productId,productRequest);
    }

    @Override
    public void deleteProductById(Integer productId) {
        productDao.deleteProductById(productId);
    }
}
