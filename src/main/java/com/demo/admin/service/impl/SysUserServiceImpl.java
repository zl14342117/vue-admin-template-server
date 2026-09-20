package com.demo.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.admin.common.ApiCodes;
import com.demo.admin.exception.BusinessException;
import com.demo.admin.mapper.SysUserMapper;
import com.demo.admin.pojo.dto.SysUserSaveDTO;
import com.demo.admin.pojo.entity.SysUserDO;
import com.demo.admin.pojo.vo.SysUserPageVO;
import com.demo.admin.pojo.vo.SysUserVO;
import com.demo.admin.service.SysUserService;
import com.demo.admin.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统用户 MySQL 持久化实现。
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public SysUserPageVO list(Integer page, Integer limit, String keyword) {
        int pageNo = page == null || page < 1 ? 1 : page;
        int pageSize = limit == null || limit < 1 ? 10 : limit;

        LambdaQueryWrapper<SysUserDO> wrapper = new LambdaQueryWrapper<SysUserDO>()
                .orderByAsc(SysUserDO::getId);
        if (StringUtils.hasText(keyword)) {
            String value = keyword.trim();
            wrapper.and(w -> w.like(SysUserDO::getUsername, value)
                    .or()
                    .like(SysUserDO::getNickname, value));
        }

        Page<SysUserDO> result = sysUserMapper.selectPage(new Page<SysUserDO>(pageNo, pageSize), wrapper);
        List<SysUserVO> pageList = result.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return new SysUserPageVO(result.getTotal(), pageList);
    }

    @Override
    public SysUserVO detail(Long id) {
        return toVO(findById(id));
    }

    @Override
    public SysUserVO create(SysUserSaveDTO dto) {
        if (!StringUtils.hasText(dto.getPassword())) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "密码不能为空");
        }
        if (findByUsername(dto.getUsername()) != null) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "用户名已存在");
        }

        SysUserDO user = new SysUserDO();
        user.setUsername(dto.getUsername().trim());
        user.setPassword(dto.getPassword());
        user.setNickname(dto.getNickname().trim());
        user.setRole(dto.getRole());
        user.setStatus(dto.getStatus());
        user.setCreatedAt(LocalDateTime.now());
        sysUserMapper.insert(user);
        return toVO(user);
    }

    @Override
    public SysUserVO update(Long id, SysUserSaveDTO dto) {
        SysUserDO user = findById(id);
        SysUserDO existed = findByUsername(dto.getUsername());
        if (existed != null && !existed.getId().equals(id)) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "用户名已存在");
        }

        user.setUsername(dto.getUsername().trim());
        user.setNickname(dto.getNickname().trim());
        user.setRole(dto.getRole());
        user.setStatus(dto.getStatus());
        if (StringUtils.hasText(dto.getPassword())) {
            user.setPassword(dto.getPassword());
        }
        sysUserMapper.updateById(user);
        return toVO(user);
    }

    @Override
    public void delete(Long id) {
        SysUserDO user = findById(id);
        if ("admin".equals(user.getUsername())) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "内置管理员不能删除");
        }
        sysUserMapper.deleteById(id);
    }

    @Override
    public SysUserDO findActiveUser(String username, String password) {
        SysUserDO user = findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(ApiCodes.LOGIN_FAILED, "账号已禁用");
        }
        return user;
    }

    @Override
    public SysUserDO findByToken(String token) {
        String username = jwtUtil.getUsername(token);
        if (!StringUtils.hasText(username)) {
            return null;
        }
        SysUserDO user = findByUsername(username);
        if (user == null || user.getStatus() == null || user.getStatus() != 1) {
            return null;
        }
        return user;
    }

    @Override
    public SysUserDO findByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        return sysUserMapper.selectOne(new LambdaQueryWrapper<SysUserDO>()
                .eq(SysUserDO::getUsername, username.trim())
                .last("LIMIT 1"));
    }

    private SysUserDO findById(Long id) {
        SysUserDO user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ApiCodes.NOT_FOUND, "用户不存在");
        }
        return user;
    }

    private SysUserVO toVO(SysUserDO user) {
        SysUserVO vo = new SysUserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setRole(user.getRole());
        vo.setStatus(user.getStatus());
        if (user.getCreatedAt() != null) {
            vo.setCreatedAt(user.getCreatedAt().format(DATE_TIME_FORMATTER));
        }
        return vo;
    }
}
