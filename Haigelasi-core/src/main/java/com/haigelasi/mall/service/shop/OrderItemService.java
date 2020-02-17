package com.haigelasi.mall.service.shop;


import com.haigelasi.mall.bean.entity.shop.OrderItem;
import com.haigelasi.mall.dao.shop.OrderItemRepository;
import com.haigelasi.mall.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService extends BaseService<OrderItem,Long,OrderItemRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OrderItemRepository orderItemRepository;

}

