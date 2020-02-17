package com.haigelasi.mall.service.shop;


import com.haigelasi.mall.bean.entity.shop.Category;
import com.haigelasi.mall.dao.shop.CategoryRepository;
import com.haigelasi.mall.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends BaseService<Category,Long,CategoryRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CategoryRepository categoryRepository;

}

