package com.demo.admin.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 配置：分页插件 + Mapper 扫描。
 */
@Configuration
@MapperScan("com.demo.admin.mapper")
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 固定 worker/datacenter，避免启动时通过本机网卡/主机名初始化雪花算法。
     * 本项目主键是数据库 AUTO_INCREMENT，此 Bean 仅消除启动阻塞。
     */
    @Bean
    public IdentifierGenerator identifierGenerator() {
        return new DefaultIdentifierGenerator(1L, 1L);
    }
}
