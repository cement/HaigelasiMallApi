package com.haigelasi.mall.api;

import com.haigelasi.mall.dao.BaseRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * ApiApplication
 *
 * @author enilu
 * @version 2018/9/11 0011
 */
@EnableCaching
@SpringBootApplication
@ComponentScan(basePackages = "com.haigelasi.mall")
@EntityScan(basePackages="com.haigelasi.mall.bean.entity")
@EnableJpaRepositories(basePackages = "com.haigelasi.mall.dao", repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
@EnableJpaAuditing
public class AdminApiApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AdminApiApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(AdminApiApplication.class);
    }
}
