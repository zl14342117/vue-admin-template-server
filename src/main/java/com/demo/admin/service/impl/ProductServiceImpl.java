package com.demo.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.admin.common.ApiCodes;
import com.demo.admin.exception.BusinessException;
import com.demo.admin.mapper.ProductMapper;
import com.demo.admin.pojo.dto.ProductSaveDTO;
import com.demo.admin.pojo.entity.ProductDO;
import com.demo.admin.pojo.vo.ProductOptionVO;
import com.demo.admin.pojo.vo.ProductPageVO;
import com.demo.admin.pojo.vo.ProductVO;
import com.demo.admin.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品 MySQL 持久化实现。
 */
@Service
public class ProductServiceImpl implements ProductService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Resource
    private ProductMapper productMapper;

    @Override
    public ProductPageVO list(Integer page, Integer limit, String keyword, String category, Integer status) {
        int pageNo = page == null || page < 1 ? 1 : page;
        int pageSize = limit == null || limit < 1 ? 10 : limit;

        LambdaQueryWrapper<ProductDO> wrapper = new LambdaQueryWrapper<ProductDO>()
                .orderByDesc(ProductDO::getId);
        if (StringUtils.hasText(keyword)) {
            String value = keyword.trim();
            wrapper.and(w -> w.like(ProductDO::getName, value)
                    .or()
                    .like(ProductDO::getCode, value));
        }
        if (StringUtils.hasText(category)) {
            wrapper.eq(ProductDO::getCategory, category.trim());
        }
        if (status != null) {
            wrapper.eq(ProductDO::getStatus, status);
        }

        Page<ProductDO> result = productMapper.selectPage(new Page<ProductDO>(pageNo, pageSize), wrapper);
        List<ProductVO> pageList = result.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return new ProductPageVO(result.getTotal(), pageList);
    }

    @Override
    public List<ProductOptionVO> options() {
        return productMapper.selectList(new LambdaQueryWrapper<ProductDO>()
                        .eq(ProductDO::getStatus, 1)
                        .gt(ProductDO::getStock, 0)
                        .orderByAsc(ProductDO::getId))
                .stream()
                .map(p -> new ProductOptionVO(
                        p.getId(),
                        p.getName() + "（" + p.getCode() + "）",
                        p.getCode(),
                        p.getPrice(),
                        p.getStock()))
                .collect(Collectors.toList());
    }

    @Override
    public ProductVO detail(Long id) {
        return toVO(findById(id));
    }

    @Override
    public ProductVO create(ProductSaveDTO dto) {
        if (findByCode(dto.getCode()) != null) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "商品编码已存在");
        }
        ProductDO product = new ProductDO();
        fillEntity(product, dto);
        product.setCreatedAt(LocalDateTime.now());
        productMapper.insert(product);
        return toVO(product);
    }

    @Override
    public ProductVO update(Long id, ProductSaveDTO dto) {
        ProductDO product = findById(id);
        ProductDO existed = findByCode(dto.getCode());
        if (existed != null && !existed.getId().equals(id)) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "商品编码已存在");
        }
        fillEntity(product, dto);
        productMapper.updateById(product);
        return toVO(product);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        productMapper.deleteById(id);
    }

    private void fillEntity(ProductDO product, ProductSaveDTO dto) {
        product.setName(dto.getName().trim());
        product.setCode(dto.getCode().trim());
        product.setCategory(dto.getCategory().trim());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setStatus(dto.getStatus());
        product.setImageUrl(StringUtils.hasText(dto.getImageUrl()) ? dto.getImageUrl().trim() : null);
    }

    private ProductDO findById(Long id) {
        ProductDO product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException(ApiCodes.NOT_FOUND, "商品不存在");
        }
        return product;
    }

    private ProductDO findByCode(String code) {
        if (!StringUtils.hasText(code)) {
            return null;
        }
        return productMapper.selectOne(new LambdaQueryWrapper<ProductDO>()
                .eq(ProductDO::getCode, code.trim())
                .last("LIMIT 1"));
    }

    private ProductVO toVO(ProductDO product) {
        ProductVO vo = new ProductVO();
        vo.setId(product.getId());
        vo.setName(product.getName());
        vo.setCode(product.getCode());
        vo.setCategory(product.getCategory());
        vo.setPrice(product.getPrice());
        vo.setStock(product.getStock());
        vo.setStatus(product.getStatus());
        vo.setImageUrl(product.getImageUrl());
        if (product.getCreatedAt() != null) {
            vo.setCreatedAt(product.getCreatedAt().format(DATE_TIME_FORMATTER));
        }
        return vo;
    }
}
