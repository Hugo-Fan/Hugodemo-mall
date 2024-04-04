package com.hugo.hugodemomall.dto;

import com.hugo.hugodemomall.constant.ProductCategory;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;
@Data
public class ProductQueryParams {
    private ProductCategory category;
    private String search;

}
