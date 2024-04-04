package com.hugo.hugodemomall.dto;

import com.hugo.hugodemomall.constant.ProductCategory;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Data
public class ProductQueryParams {
    private ProductCategory category;
    private String search;
    private List<String> orderBy;
    private String sort;
    private Integer limit;
    private Integer offset;
}
