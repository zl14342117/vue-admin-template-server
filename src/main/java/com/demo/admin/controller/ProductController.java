package com.demo.admin.controller;

import com.demo.admin.common.ApiResponse;
import com.demo.admin.pojo.dto.ProductSaveDTO;
import com.demo.admin.pojo.vo.ProductOptionVO;
import com.demo.admin.pojo.vo.ProductPageVO;
import com.demo.admin.pojo.vo.ProductVO;
import com.demo.admin.service.ProductService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品管理接口，对应前端 src/api/product.js。
 */
@RestController
@RequestMapping("/vue-admin-template/product")
public class ProductController {

    @Resource
    private ProductService productService;

    /** 分页查询商品列表 */
    @GetMapping("/list")
    public ApiResponse<ProductPageVO> list(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "status", required = false) Integer status) {
        return ApiResponse.success(productService.list(page, limit, keyword, category, status));
    }

    /** 上架且有库存的商品选项（订单模块使用） */
    @GetMapping("/options")
    public ApiResponse<List<ProductOptionVO>> options() {
        return ApiResponse.success(productService.options());
    }

    /** 商品详情 */
    @GetMapping("/{id}")
    public ApiResponse<ProductVO> detail(@PathVariable("id") Long id) {
        return ApiResponse.success(productService.detail(id));
    }

    /** 新增商品 */
    @PostMapping
    public ApiResponse<ProductVO> create(@RequestBody @Validated ProductSaveDTO dto) {
        return ApiResponse.success(productService.create(dto));
    }

    /** 编辑商品 */
    @PutMapping("/{id}")
    public ApiResponse<ProductVO> update(@PathVariable("id") Long id,
                                         @RequestBody @Validated ProductSaveDTO dto) {
        return ApiResponse.success(productService.update(id, dto));
    }

    /** 删除商品 */
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return ApiResponse.success("success");
    }
}
