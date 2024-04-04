package com.hugo.hugodemomall.dao.impl;

import com.hugo.hugodemomall.dao.ProductDao;
import com.hugo.hugodemomall.dto.ProductQueryParams;
import com.hugo.hugodemomall.dto.ProductRequest;
import com.hugo.hugodemomall.model.Product;
import com.hugo.hugodemomall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImp implements ProductDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        String sql = """
                SELECT COUNT(*)
                FROM product
                WHERE 1=1
                """;
        Map<String, Object> map = new HashMap<>();

        if(productQueryParams.getCategory() !=null){
            sql += " AND category = :category";
            map.put("category",productQueryParams.getCategory().name()); // 記得使用.name轉成String
        }

        if(productQueryParams.getSearch() !=null){
            sql += " AND product_name LIKE :search ";
            map.put("search","%" + productQueryParams.getSearch() + "%");
        }

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

        return total;
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        // 查詢條件
        String sql = """
                SELECT product_id,product_name, category, image_url, price, stock, description, created_date, last_modified_date 
                FROM product
                WHERE 1=1
                
                """;

        // 查詢條件
        Map<String, Object> map = new HashMap<>();

        if(productQueryParams.getCategory() !=null){
            sql += " AND category = :category";
            map.put("category",productQueryParams.getCategory().name()); // 記得使用.name轉成String
        }

        if(productQueryParams.getSearch() !=null){
            sql += " AND product_name LIKE :search ";
            map.put("search","%" + productQueryParams.getSearch() + "%");
        }

        // 排序
        sql += " ORDER BY ";
        for (int i = 0 ; i<productQueryParams.getOrderBy().size();i++){
            if(i>0){
                sql += " , ";
            }
            sql += productQueryParams.getOrderBy().get(i);
        }

        sql += " " + productQueryParams.getSort();


        // 分頁
        sql += " LIMIT :limit OFFSET :offset";
        map.put("limit",productQueryParams.getLimit());
        map.put("offset",productQueryParams.getOffset());

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        return  productList;
    }

    @Override
    public Product getProductById(Integer productId) {
        String sql = "SELECT product_id,product_name, category, image_url, price, stock, description, created_date, last_modified_date " +
                "FROM product WHERE product_id = :productId";
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        if (productList.size() > 0) {
            return productList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql = "INSERT INTO product(product_name, category, image_url, price, stock, description, created_date, last_modified_date) " +
                "VALUES (:product_name, :category, :image_url, :price, :stock, :description, :created_date, :last_modified_date)";

        Map<String, Object> map = new HashMap<>();
        map.put("product_name", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("image_url", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date now = new Date();
        map.put("created_date", now);
        map.put("last_modified_date", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int productId = keyHolder.getKey().intValue();

        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = """
                    UPDATE product 
                    SET product_name = :product_name ,category = :category,image_url = :image_url ,price = :price,stock = :stock,description = :description , last_modified_date = :last_modified_date 
                    WHERE product_id =:productId 
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("product_name", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("image_url", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        map.put("last_modified_date", new Date());

        namedParameterJdbcTemplate.update(sql, map);

    }

    @Override
    public void deleteProductById(Integer productId) {
        String sql = """
                    DELETE FROM product
                    WHERE product_id = :productId;
                """;
        Map<String,Object> map = new HashMap<>();
        map.put("productId",productId);

        namedParameterJdbcTemplate.update(sql,map);
    }
}
