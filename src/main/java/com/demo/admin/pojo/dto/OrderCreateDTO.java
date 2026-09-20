package com.demo.admin.pojo.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 创建订单入参。
 */
@Data
public class OrderCreateDTO {

    @NotNull(message = "客户不能为空")
    private Long customerId;

    @NotEmpty(message = "请至少选择一个商品")
    @Valid
    private List<OrderItemCreateDTO> items;

    @Data
    public static class OrderItemCreateDTO {

        @NotNull(message = "商品不能为空")
        private Long productId;

        @NotNull(message = "数量不能为空")
        @Min(value = 1, message = "数量至少为 1")
        private Integer quantity;
    }
}
