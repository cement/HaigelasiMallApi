package com.haigelasi.mall.service.shop;


import com.haigelasi.mall.bean.entity.shop.GoodsSku;
import com.haigelasi.mall.dao.shop.GoodsSkuRepository;
import com.haigelasi.mall.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsSkuService extends BaseService<GoodsSku,Long,GoodsSkuRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private GoodsSkuRepository goodsSkuRepository;

}

