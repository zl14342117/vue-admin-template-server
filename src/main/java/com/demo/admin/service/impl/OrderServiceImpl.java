package com.demo.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.admin.common.ApiCodes;
import com.demo.admin.exception.BusinessException;
import com.demo.admin.mapper.CustomerMapper;
import com.demo.admin.mapper.OrderItemMapper;
import com.demo.admin.mapper.OrderMapper;
import com.demo.admin.mapper.ProductMapper;
import com.demo.admin.pojo.dto.OrderCreateDTO;
import com.demo.admin.pojo.entity.CustomerDO;
import com.demo.admin.pojo.entity.OrderDO;
import com.demo.admin.pojo.entity.OrderItemDO;
import com.demo.admin.pojo.entity.ProductDO;
import com.demo.admin.pojo.vo.OrderItemVO;
import com.demo.admin.pojo.vo.OrderPageVO;
import com.demo.admin.pojo.vo.OrderVO;
import com.demo.admin.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单业务实现：创建扣库存，取消回滚库存。
 */
@Service
public class OrderServiceImpl implements OrderService {

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_PAID = 1;
    public static final int STATUS_CANCELLED = 2;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private ProductMapper productMapper;

    @Override
    public OrderPageVO list(Integer page, Integer limit, String keyword, Integer status) {
        int pageNo = page == null || page < 1 ? 1 : page;
        int pageSize = limit == null || limit < 1 ? 10 : limit;

        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<OrderDO>()
                .orderByDesc(OrderDO::getId);
        if (StringUtils.hasText(keyword)) {
            String value = keyword.trim();
            wrapper.and(w -> w.like(OrderDO::getOrderNo, value)
                    .or()
                    .like(OrderDO::getCustomerName, value));
        }
        if (status != null) {
            wrapper.eq(OrderDO::getStatus, status);
        }

        Page<OrderDO> result = orderMapper.selectPage(new Page<OrderDO>(pageNo, pageSize), wrapper);
        List<OrderVO> pageList = result.getRecords().stream()
                .map(order -> toVO(order, false))
                .collect(Collectors.toList());
        return new OrderPageVO(result.getTotal(), pageList);
    }

    @Override
    public OrderVO detail(Long id) {
        return toVO(findById(id), true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO create(OrderCreateDTO dto) {
        CustomerDO customer = customerMapper.selectById(dto.getCustomerId());
        if (customer == null) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "客户不存在");
        }
        if (customer.getStatus() == null || customer.getStatus() != 1) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "客户已禁用，无法下单");
        }

        Map<Long, Integer> quantityMap = mergeQuantities(dto.getItems());
        List<OrderItemDO> itemEntities = new ArrayList<OrderItemDO>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : quantityMap.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();
            ProductDO product = productMapper.selectById(productId);
            if (product == null) {
                throw new BusinessException(ApiCodes.BAD_REQUEST, "商品不存在: " + productId);
            }
            if (product.getStatus() == null || product.getStatus() != 1) {
                throw new BusinessException(ApiCodes.BAD_REQUEST, "商品未上架: " + product.getName());
            }
            if (product.getStock() == null || product.getStock() < quantity) {
                throw new BusinessException(ApiCodes.BAD_REQUEST, "库存不足: " + product.getName());
            }

            product.setStock(product.getStock() - quantity);
            productMapper.updateById(product);

            BigDecimal amount = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            OrderItemDO item = new OrderItemDO();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setProductCode(product.getCode());
            item.setPrice(product.getPrice());
            item.setQuantity(quantity);
            item.setAmount(amount);
            itemEntities.add(item);
            total = total.add(amount);
        }

        OrderDO order = new OrderDO();
        order.setOrderNo(nextOrderNo());
        order.setCustomerId(customer.getId());
        order.setCustomerName(customer.getName());
        order.setTotalAmount(total);
        order.setStatus(STATUS_PENDING);
        order.setCreatedAt(LocalDateTime.now());
        orderMapper.insert(order);

        for (OrderItemDO item : itemEntities) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }
        return toVO(order, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO pay(Long id) {
        OrderDO order = findById(id);
        if (order.getStatus() == null || order.getStatus() != STATUS_PENDING) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "仅待支付订单可支付");
        }
        order.setStatus(STATUS_PAID);
        orderMapper.updateById(order);
        return toVO(order, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO cancel(Long id) {
        OrderDO order = findById(id);
        if (order.getStatus() != null && order.getStatus() == STATUS_CANCELLED) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "订单已取消");
        }
        List<OrderItemDO> items = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItemDO>()
                .eq(OrderItemDO::getOrderId, order.getId()));
        for (OrderItemDO item : items) {
            ProductDO product = productMapper.selectById(item.getProductId());
            if (product != null) {
                int stock = product.getStock() == null ? 0 : product.getStock();
                product.setStock(stock + item.getQuantity());
                productMapper.updateById(product);
            }
        }
        order.setStatus(STATUS_CANCELLED);
        orderMapper.updateById(order);
        return toVO(order, true);
    }

    private Map<Long, Integer> mergeQuantities(List<OrderCreateDTO.OrderItemCreateDTO> items) {
        Map<Long, Integer> map = new LinkedHashMap<Long, Integer>();
        for (OrderCreateDTO.OrderItemCreateDTO item : items) {
            Integer old = map.get(item.getProductId());
            map.put(item.getProductId(), (old == null ? 0 : old) + item.getQuantity());
        }
        return map;
    }

    private String nextOrderNo() {
        LocalDate today = LocalDate.now();
        String prefix = "O" + today.format(DAY_FORMATTER);
        Long todayCount = orderMapper.selectCount(new LambdaQueryWrapper<OrderDO>()
                .ge(OrderDO::getCreatedAt, today.atStartOfDay())
                .lt(OrderDO::getCreatedAt, today.plusDays(1).atStartOfDay()));
        long seq = (todayCount == null ? 0L : todayCount) + 1L;
        return prefix + String.format("%04d", seq);
    }

    private OrderDO findById(Long id) {
        OrderDO order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(ApiCodes.NOT_FOUND, "订单不存在");
        }
        return order;
    }

    private OrderVO toVO(OrderDO order, boolean withItems) {
        OrderVO vo = new OrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setCustomerId(order.getCustomerId());
        vo.setCustomerName(order.getCustomerName());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setStatus(order.getStatus());
        if (order.getCreatedAt() != null) {
            vo.setCreatedAt(order.getCreatedAt().format(DATE_TIME_FORMATTER));
        }
        if (withItems) {
            List<OrderItemVO> items = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItemDO>()
                            .eq(OrderItemDO::getOrderId, order.getId())
                            .orderByAsc(OrderItemDO::getId))
                    .stream()
                    .map(this::toItemVO)
                    .collect(Collectors.toList());
            vo.setItems(items);
        }
        return vo;
    }

    private OrderItemVO toItemVO(OrderItemDO item) {
        OrderItemVO vo = new OrderItemVO();
        vo.setId(item.getId());
        vo.setProductId(item.getProductId());
        vo.setProductName(item.getProductName());
        vo.setProductCode(item.getProductCode());
        vo.setPrice(item.getPrice());
        vo.setQuantity(item.getQuantity());
        vo.setAmount(item.getAmount());
        return vo;
    }
}
