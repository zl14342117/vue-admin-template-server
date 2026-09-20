package com.demo.admin.service;

import com.demo.admin.pojo.dto.ProductSaveDTO;
import com.demo.admin.pojo.vo.ProductOptionVO;
import com.demo.admin.pojo.vo.ProductPageVO;
import com.demo.admin.pojo.vo.ProductVO;

import java.util.List;

/**
 * 商品管理业务接口。
 */
public interface ProductService {

    ProductPageVO list(Integer page, Integer limit, String keyword, String category, Integer status);

    List<ProductOptionVO> options();

    ProductVO detail(Long id);

    ProductVO create(ProductSaveDTO dto);

    ProductVO update(Long id, ProductSaveDTO dto);

    void delete(Long id);
}
