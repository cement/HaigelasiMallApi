package com.haigelasi.mall.api.controller.shop;

import com.haigelasi.mall.bean.constant.factory.PageFactory;
import com.haigelasi.mall.bean.core.BussinessLog;
import com.haigelasi.mall.bean.dictmap.CommonDict;
import com.haigelasi.mall.bean.entity.shop.Order;
import com.haigelasi.mall.bean.enumeration.BizExceptionEnum;
import com.haigelasi.mall.bean.enumeration.shop.OrderEnum;
import com.haigelasi.mall.bean.exception.ApplicationException;
import com.haigelasi.mall.bean.vo.front.Rets;
import com.haigelasi.mall.service.shop.OrderService;
import com.haigelasi.mall.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop/order")
public class OrderController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list() {
	Page<Order> page = new PageFactory<Order>().defaultPage();
		page = orderService.queryPage(page);
		return Rets.success(page);
	}
	@RequestMapping(value="/sendOut/{id}",method = RequestMethod.POST)
	@BussinessLog(value = "发货", key = "name",dict= CommonDict.class)
	public Object sendOut(@PathVariable("id") Long id){
	 	Order order = orderService.get(id);
	 	order.setStatus(OrderEnum.OrderStatusEnum.SENDED.getId());
	 	orderService.update(order);
		return Rets.success();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	@BussinessLog(value = "删除订单", key = "id",dict= CommonDict.class)
	public Object remove(Long id){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		orderService.deleteById(id);
		return Rets.success();
	}

	@RequestMapping(value = "{orderSn}",method = RequestMethod.GET)
	public Object get(@PathVariable("orderSn") String orderSn){
		if (orderSn == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		Order order = orderService.getByOrderSn(orderSn);
		return Rets.success(order);
	}
}
