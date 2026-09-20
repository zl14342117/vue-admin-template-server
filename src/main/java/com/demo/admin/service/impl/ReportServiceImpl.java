package com.demo.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demo.admin.exception.BusinessException;
import com.demo.admin.mapper.OrderItemMapper;
import com.demo.admin.mapper.OrderMapper;
import com.demo.admin.mapper.ProductMapper;
import com.demo.admin.pojo.entity.OrderDO;
import com.demo.admin.pojo.entity.OrderItemDO;
import com.demo.admin.pojo.entity.ProductDO;
import com.demo.admin.pojo.vo.ReportCategorySalesVO;
import com.demo.admin.pojo.vo.ReportDailySalesVO;
import com.demo.admin.pojo.vo.ReportSalesVO;
import com.demo.admin.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 销售报表：按已支付订单在内存中按日 / 分类聚合。
 */
@Service
public class ReportServiceImpl implements ReportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private ProductMapper productMapper;

    @Override
    public ReportSalesVO sales(String startDate, String endDate) {
        LocalDate end = parseDateOrDefault(endDate, LocalDate.now());
        LocalDate start = parseDateOrDefault(startDate, end.minusDays(29));
        if (start.isAfter(end)) {
            throw new BusinessException(50000, "开始日期不能晚于结束日期");
        }

        LocalDateTime rangeStart = start.atStartOfDay();
        LocalDateTime rangeEnd = end.plusDays(1).atStartOfDay();

        List<OrderDO> paidOrders = orderMapper.selectList(new LambdaQueryWrapper<OrderDO>()
                .ge(OrderDO::getCreatedAt, rangeStart)
                .lt(OrderDO::getCreatedAt, rangeEnd)
                .eq(OrderDO::getStatus, OrderServiceImpl.STATUS_PAID)
                .orderByAsc(OrderDO::getCreatedAt));

        Map<String, BigDecimal> dailyMap = new LinkedHashMap<String, BigDecimal>();
        for (LocalDate day = start; !day.isAfter(end); day = day.plusDays(1)) {
            dailyMap.put(day.format(DATE_FORMATTER), BigDecimal.ZERO);
        }

        Map<Long, OrderDO> orderById = new HashMap<Long, OrderDO>();
        for (OrderDO order : paidOrders) {
            orderById.put(order.getId(), order);
            if (order.getCreatedAt() == null || order.getTotalAmount() == null) {
                continue;
            }
            String dayKey = order.getCreatedAt().toLocalDate().format(DATE_FORMATTER);
            BigDecimal current = dailyMap.get(dayKey);
            if (current != null) {
                dailyMap.put(dayKey, current.add(order.getTotalAmount()));
            }
        }

        Map<String, BigDecimal> categoryMap = new LinkedHashMap<String, BigDecimal>();
        if (!orderById.isEmpty()) {
            List<OrderItemDO> items = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItemDO>()
                    .in(OrderItemDO::getOrderId, orderById.keySet()));

            Set<Long> productIds = new HashSet<Long>();
            for (OrderItemDO item : items) {
                if (item.getProductId() != null) {
                    productIds.add(item.getProductId());
                }
            }

            Map<Long, String> productCategory = new HashMap<Long, String>();
            if (!productIds.isEmpty()) {
                List<ProductDO> products = productMapper.selectBatchIds(productIds);
                for (ProductDO product : products) {
                    productCategory.put(product.getId(),
                            StringUtils.hasText(product.getCategory()) ? product.getCategory() : "unknown");
                }
            }

            for (OrderItemDO item : items) {
                if (!orderById.containsKey(item.getOrderId())) {
                    continue;
                }
                String category = productCategory.get(item.getProductId());
                if (!StringUtils.hasText(category)) {
                    category = "unknown";
                }
                BigDecimal amount = item.getAmount() == null ? BigDecimal.ZERO : item.getAmount();
                BigDecimal current = categoryMap.get(category);
                categoryMap.put(category, current == null ? amount : current.add(amount));
            }
        }

        ReportSalesVO vo = new ReportSalesVO();
        List<ReportDailySalesVO> dailySales = new ArrayList<ReportDailySalesVO>();
        for (Map.Entry<String, BigDecimal> entry : dailyMap.entrySet()) {
            dailySales.add(new ReportDailySalesVO(entry.getKey(), entry.getValue()));
        }
        vo.setDailySales(dailySales);

        List<ReportCategorySalesVO> categorySales = new ArrayList<ReportCategorySalesVO>();
        for (Map.Entry<String, BigDecimal> entry : categoryMap.entrySet()) {
            categorySales.add(new ReportCategorySalesVO(entry.getKey(), entry.getValue()));
        }
        vo.setCategorySales(categorySales);
        return vo;
    }

    private LocalDate parseDateOrDefault(String value, LocalDate defaultValue) {
        if (!StringUtils.hasText(value)) {
            return defaultValue;
        }
        try {
            return LocalDate.parse(value.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new BusinessException(50000, "日期格式应为 yyyy-MM-dd");
        }
    }
}
