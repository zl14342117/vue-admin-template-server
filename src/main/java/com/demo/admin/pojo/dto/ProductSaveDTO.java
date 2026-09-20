package com.demo.admin.pojo.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * 商品新增/编辑入参。
 */
@Data
public class ProductSaveDTO {

    @NotBlank(message = "商品名称不能为空")
    private String name;

    @NotBlank(message = "商品编码不能为空")
    private String code;

    @NotBlank(message = "分类不能为空")
    @Pattern(regexp = "^(digital|daily|food)$", message = "分类仅支持 digital/daily/food")
    private String category;

    @NotNull(message = "单价不能为空")
    @DecimalMin(value = "0.01", message = "单价必须大于 0")
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能小于 0")
    private Integer stock;

    @NotNull(message = "状态不能为空")
    private Integer status;
}
