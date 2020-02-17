package com.haigelasi.mall.service.shop;


import com.haigelasi.mall.bean.entity.shop.Order;
import com.haigelasi.mall.bean.entity.shop.OrderItem;
import com.haigelasi.mall.bean.enumeration.shop.OrderEnum;
import com.haigelasi.mall.bean.vo.query.SearchFilter;
import com.haigelasi.mall.dao.shop.OrderItemRepository;
import com.haigelasi.mall.dao.shop.OrderRepository;
import com.haigelasi.mall.service.BaseService;
import com.haigelasi.mall.utils.DateUtil;
import com.haigelasi.mall.utils.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService extends BaseService<Order, Long, OrderRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    /**
     * 获取唯一订单号
     * 时间戳+随机数<br>
     * 建议生产环境使用redis获取唯一订单号
     *
     * @return
     */
    public String getOrderSn() {
        return DateUtil.getAllTime() + RandomUtil.getRandomNumber(6);
    }

    public void save(Order order, List<OrderItem> itemList) {
        order.setOrderSn(getOrderSn());
        insert(order);
        for(OrderItem item:itemList){
            item.setIdOrder(order.getId());
        }
        orderItemRepository.saveAll(itemList);
    }

    /**
     * 取消订单<br>
     * 目前逻辑简单直接删除，真正上线可以采用逻辑删除而
     * @param orderSn
     */
    public void cancel(String orderSn) {
        Order order = getByOrderSn(orderSn);
        List<OrderItem> itemList = order.getItems();
        delete(order);
        orderItemRepository.deleteInBatch(itemList);

    }

    public Order getByOrderSn(String orderSn) {
        return get(SearchFilter.build("orderSn", SearchFilter.Operator.EQ,orderSn));
    }

    /**
     * 确认收货
     * @param orderSn
     */
    public Order confirmReceive(String orderSn) {
        Order order = getByOrderSn(orderSn);
        order.setStatus(OrderEnum.OrderStatusEnum.FINISHED.getId());
        update(order);
        return order;
    }
}

