package com.demo.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.admin.common.ApiCodes;
import com.demo.admin.exception.BusinessException;
import com.demo.admin.mapper.CustomerMapper;
import com.demo.admin.pojo.dto.CustomerSaveDTO;
import com.demo.admin.pojo.entity.CustomerDO;
import com.demo.admin.pojo.vo.CustomerOptionVO;
import com.demo.admin.pojo.vo.CustomerPageVO;
import com.demo.admin.pojo.vo.CustomerVO;
import com.demo.admin.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户 MySQL 持久化实现。
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Resource
    private CustomerMapper customerMapper;

    @Override
    public CustomerPageVO list(Integer page, Integer limit, String keyword, Integer status) {
        int pageNo = page == null || page < 1 ? 1 : page;
        int pageSize = limit == null || limit < 1 ? 10 : limit;

        LambdaQueryWrapper<CustomerDO> wrapper = new LambdaQueryWrapper<CustomerDO>()
                .orderByDesc(CustomerDO::getId);
        if (StringUtils.hasText(keyword)) {
            String value = keyword.trim();
            wrapper.and(w -> w.like(CustomerDO::getName, value)
                    .or()
                    .like(CustomerDO::getPhone, value)
                    .or()
                    .like(CustomerDO::getCustomerNo, value)
                    .or()
                    .like(CustomerDO::getContact, value));
        }
        if (status != null) {
            wrapper.eq(CustomerDO::getStatus, status);
        }

        Page<CustomerDO> result = customerMapper.selectPage(new Page<CustomerDO>(pageNo, pageSize), wrapper);
        List<CustomerVO> pageList = result.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return new CustomerPageVO(result.getTotal(), pageList);
    }

    @Override
    public List<CustomerOptionVO> options() {
        return customerMapper.selectList(new LambdaQueryWrapper<CustomerDO>()
                        .eq(CustomerDO::getStatus, 1)
                        .orderByDesc(CustomerDO::getId))
                .stream()
                .map(c -> new CustomerOptionVO(
                        c.getId(),
                        c.getName() + "（" + c.getCustomerNo() + "）",
                        c.getCustomerNo()))
                .collect(Collectors.toList());
    }

    @Override
    public CustomerVO detail(Long id) {
        return toVO(findById(id));
    }

    @Override
    public CustomerVO create(CustomerSaveDTO dto) {
        if (findByPhone(dto.getPhone()) != null) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "手机号已存在");
        }
        CustomerDO customer = new CustomerDO();
        customer.setCustomerNo(nextCustomerNo());
        customer.setName(dto.getName().trim());
        customer.setContact(dto.getContact().trim());
        customer.setPhone(dto.getPhone().trim());
        customer.setStatus(dto.getStatus());
        customer.setCreatedAt(LocalDateTime.now());
        customerMapper.insert(customer);
        return toVO(customer);
    }

    @Override
    public CustomerVO update(Long id, CustomerSaveDTO dto) {
        CustomerDO customer = findById(id);
        CustomerDO existed = findByPhone(dto.getPhone());
        if (existed != null && !existed.getId().equals(id)) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "手机号已存在");
        }
        customer.setName(dto.getName().trim());
        customer.setContact(dto.getContact().trim());
        customer.setPhone(dto.getPhone().trim());
        customer.setStatus(dto.getStatus());
        customerMapper.updateById(customer);
        return toVO(customer);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        customerMapper.deleteById(id);
    }

    private String nextCustomerNo() {
        LocalDate today = LocalDate.now();
        String day = today.format(DAY_FORMATTER);
        String prefix = "C" + day;
        Long todayCount = customerMapper.selectCount(new LambdaQueryWrapper<CustomerDO>()
                .ge(CustomerDO::getCreatedAt, today.atStartOfDay())
                .lt(CustomerDO::getCreatedAt, today.plusDays(1).atStartOfDay()));
        long seq = (todayCount == null ? 0L : todayCount) + 1L;
        return prefix + String.format("%04d", seq);
    }

    private CustomerDO findById(Long id) {
        CustomerDO customer = customerMapper.selectById(id);
        if (customer == null) {
            throw new BusinessException(ApiCodes.NOT_FOUND, "客户不存在");
        }
        return customer;
    }

    private CustomerDO findByPhone(String phone) {
        if (!StringUtils.hasText(phone)) {
            return null;
        }
        return customerMapper.selectOne(new LambdaQueryWrapper<CustomerDO>()
                .eq(CustomerDO::getPhone, phone.trim())
                .last("LIMIT 1"));
    }

    private CustomerVO toVO(CustomerDO customer) {
        CustomerVO vo = new CustomerVO();
        vo.setId(customer.getId());
        vo.setCustomerNo(customer.getCustomerNo());
        vo.setName(customer.getName());
        vo.setContact(customer.getContact());
        vo.setPhone(customer.getPhone());
        vo.setStatus(customer.getStatus());
        if (customer.getCreatedAt() != null) {
            vo.setCreatedAt(customer.getCreatedAt().format(DATE_TIME_FORMATTER));
        }
        return vo;
    }
}
