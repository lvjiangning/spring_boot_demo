package com.rihao.property.conf;

import com.rihao.property.shiro.jwt.JwtToken;
import com.rihao.property.util.UUIDUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * @author gaoy
 * 2020/2/19/019
 */
@Configuration
@MapperScan({"com.rihao.property.modules.**.mapper"})
public class MybatisPlusConf {

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new MybatisPaginationInterceptor();
    }

    /**
     * mybatis-plus乐观锁插件
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                JwtToken jwtToken = (JwtToken) SecurityUtils.getSubject().getPrincipal();
                String username = "system";
                if (jwtToken != null) {
                    username = jwtToken.getUsername();
                }
                this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, "createBy", String.class, username);
                this.strictUpdateFill(metaObject, "modifyTime", LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, "modifyBy", String.class, username);
                this.strictInsertFill(metaObject, "rid", String.class, UUIDUtil.get36UUID().toUpperCase());
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                JwtToken jwtToken = (JwtToken) SecurityUtils.getSubject().getPrincipal();
                String username = "system";
                if (jwtToken != null) {
                    username = jwtToken.getUsername();
                }
                this.strictUpdateFill(metaObject, "modifyTime", LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, "modifyBy", String.class, username);
            }
        };
    }

}
