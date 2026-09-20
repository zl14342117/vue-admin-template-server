package com.demo.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demo.admin.mapper.CustomerMapper;
import com.demo.admin.mapper.OrderMapper;
import com.demo.admin.mapper.ProductMapper;
import com.demo.admin.mapper.SysUserMapper;
import com.demo.admin.pojo.entity.OrderDO;
import com.demo.admin.pojo.vo.DashboardSummaryVO;
import com.demo.admin.pojo.vo.DashboardTrendItemVO;
import com.demo.admin.service.DashboardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 工作台汇总：客户/商品/今日订单与销售额/近7日趋势走真实数据。
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private OrderMapper orderMapper;

    @Override
    public DashboardSummaryVO summary() {
        DashboardSummaryVO vo = new DashboardSummaryVO();
        Long userCount = sysUserMapper.selectCount(null);
        Long customerCount = customerMapper.selectCount(null);
        Long productCount = productMapper.selectCount(null);
        vo.setUserCount(userCount == null ? 0L : userCount);
        vo.setCustomerCount(customerCount == null ? 0L : customerCount);
        vo.setProductCount(productCount == null ? 0L : productCount);

        LocalDate today = LocalDate.now();
        LocalDateTime dayStart = today.atStartOfDay();
        LocalDateTime dayEnd = today.plusDays(1).atStartOfDay();

        Long todayOrderCount = orderMapper.selectCount(new LambdaQueryWrapper<OrderDO>()
                .ge(OrderDO::getCreatedAt, dayStart)
                .lt(OrderDO::getCreatedAt, dayEnd)
                .ne(OrderDO::getStatus, OrderServiceImpl.STATUS_CANCELLED));
        vo.setTodayOrderCount(todayOrderCount == null ? 0L : todayOrderCount);

        List<OrderDO> todayPaid = orderMapper.selectList(new LambdaQueryWrapper<OrderDO>()
                .ge(OrderDO::getCreatedAt, dayStart)
                .lt(OrderDO::getCreatedAt, dayEnd)
                .eq(OrderDO::getStatus, OrderServiceImpl.STATUS_PAID));
        BigDecimal sales = BigDecimal.ZERO;
        for (OrderDO order : todayPaid) {
            if (order.getTotalAmount() != null) {
                sales = sales.add(order.getTotalAmount());
            }
        }
        vo.setTodaySalesAmount(sales);
        vo.setTrend(buildLast7DaysTrend());
        return vo;
    }

    private List<DashboardTrendItemVO> buildLast7DaysTrend() {
        List<DashboardTrendItemVO> list = new ArrayList<DashboardTrendItemVO>();
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate day = today.minusDays(i);
            Long count = orderMapper.selectCount(new LambdaQueryWrapper<OrderDO>()
                    .ge(OrderDO::getCreatedAt, day.atStartOfDay())
                    .lt(OrderDO::getCreatedAt, day.plusDays(1).atStartOfDay())
                    .ne(OrderDO::getStatus, OrderServiceImpl.STATUS_CANCELLED));
            list.add(new DashboardTrendItemVO(day.format(DATE_FORMATTER), count == null ? 0 : count.intValue()));
        }
        return list;
    }
}
