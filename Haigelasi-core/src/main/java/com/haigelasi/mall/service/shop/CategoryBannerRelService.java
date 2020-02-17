package com.haigelasi.mall.service.shop;


import com.haigelasi.mall.bean.entity.shop.CategoryBannerRel;
import com.haigelasi.mall.dao.shop.CategoryBannerRelRepository;
import com.haigelasi.mall.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryBannerRelService extends BaseService<CategoryBannerRel,Long,CategoryBannerRelRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CategoryBannerRelRepository categoryBannerRelRepository;

}

