package com.hugo.hugodemomall.controller;

import com.hugo.hugodemomall.constant.ProductCategory;
import com.hugo.hugodemomall.dto.ProductQueryParams;
import com.hugo.hugodemomall.dto.ProductRequest;
import com.hugo.hugodemomall.model.Product;
import com.hugo.hugodemomall.service.ProductService;
import com.hugo.hugodemomall.util.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class ProductController {
    @Autowired
    private ProductService productService;

    @Operation(
            summary = "取得商品列表",
            description = """
                    取得商品列表
                    可以在URL加入以下參數
                    category 選擇商品的種類
                    search   商品關鍵字的搜尋
                    orderBy  排序(可以多個欄位排序)
                    sort     排序的升序或降序
                    limit    顯示幾筆資料
                    offset   跳過幾筆資料
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功取得商品列表"
                    )
            }
    )
    @GetMapping("/products/gets")
    public ResponseEntity<Page<Product>> getProducts(
            // 查詢
            @Parameter(description = "商品類型")
            @RequestParam(required = false) ProductCategory category,

            @Parameter(description = "商品關鍵字")
            @RequestParam(required = false) String search,

            //排序
            @Parameter(description = "商品排序條件，可輸入:product_id、price、stock、created_date")
            @RequestParam(defaultValue = "created_date") List<String> orderBy,

            @Parameter(description = "商品降序或升序，可輸入desc、asc")
            @RequestParam(defaultValue = "desc") String sort,

            //分頁
            @Parameter(description = "顯示商品數量")
            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,

            @Parameter(description = "跳過商品數量")
            @RequestParam(defaultValue = "0") @Min(0) Integer offset

    ) {
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        // 取得 product List
        List<Product> productList = productService.getProducts(productQueryParams);

        // 取得 product 總數
        Integer total = productService.countProduct(productQueryParams);

        // 分頁
        Page<Product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(productList);


        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @Operation(
            summary = "取得單一商品資訊",
            description = "取得單一商品資訊",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功取得單一商品資訊"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "不存在的商品",
                            content = @Content()
                    )
            }
    )
    @GetMapping("/products/get/{productId}")
    public ResponseEntity<Product> getProduct(
            @Parameter(description = "商品ID", required = true)
            @PathVariable Integer productId)
    {
        Product product = productService.getProductById(productId);

        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
            summary = "新增商品",
            description = "新增商品，需要有MERCHANT或MALL_MANAGER已上權限才能操作",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "成功新增商品"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "缺少商品資料",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "無新增商品權限",
                            content = @Content()
                    )
            }
    )
    @PostMapping("/products/create/{memberId}")
    public ResponseEntity<Product> createProduct(
            @Parameter(description = "會員ID", required = true)
            @PathVariable Integer memberId,
            @RequestBody @Valid ProductRequest productRequest)
    {
        Integer productId = productService.createProduct(memberId,productRequest);

        Product product = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
    @Operation(
            summary = "修改商品資訊",
            description = "修改商品資訊，需要有MERCHANT或MALL_MANAGER已上權限才能操作",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功修改商品資訊"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "缺少商品資料",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "無修改商品資訊權限",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "沒有此productId",
                            content = @Content()
                    )
            }
    )
    @PutMapping("/products/update/{productId}")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "商品ID", required = true)
            @PathVariable Integer productId,
            @RequestBody @Valid ProductRequest productRequest)
    {
        // 檢查商品是否存在
        Product product = productService.getProductById(productId);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        // 修改商品的數據
        productService.updateProduct(productId, productRequest);

        Product updateProduct = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

    @Operation(
            summary = "刪除商品",
            description = "刪除商品，需要有MERCHANT或MALL_MANAGER已上權限才能操作",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "成功刪除商品",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "無刪除商品權限",
                            content = @Content()
                    )
            }
    )
    @DeleteMapping("/products/delete/{productsId}")
    public ResponseEntity<?> deleteProduct(
            @Parameter(description = "商品ID", required = true)
            @PathVariable Integer productsId)
    {
        productService.deleteProductById(productsId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
