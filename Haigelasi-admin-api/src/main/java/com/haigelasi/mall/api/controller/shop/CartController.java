package com.haigelasi.mall.api.controller.shop;

import com.haigelasi.mall.bean.constant.factory.PageFactory;
import com.haigelasi.mall.bean.core.BussinessLog;
import com.haigelasi.mall.bean.dictmap.CommonDict;
import com.haigelasi.mall.bean.entity.shop.Cart;
import com.haigelasi.mall.bean.enumeration.BizExceptionEnum;
import com.haigelasi.mall.bean.exception.ApplicationException;
import com.haigelasi.mall.bean.vo.front.Rets;
import com.haigelasi.mall.service.shop.CartService;
import com.haigelasi.mall.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shop/cart")
public class CartController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private CartService cartService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list() {
	Page<Cart> page = new PageFactory<Cart>().defaultPage();
		page = cartService.queryPage(page);
		return Rets.success(page);
	}
	@RequestMapping(method = RequestMethod.POST)
	@BussinessLog(value = "编辑购物车", key = "name",dict= CommonDict.class)
	public Object save(@ModelAttribute Cart tShopCart){
		if(tShopCart.getId()==null){
			cartService.insert(tShopCart);
		}else {
			cartService.update(tShopCart);
		}
		return Rets.success();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	@BussinessLog(value = "删除购物车", key = "id",dict= CommonDict.class)
	public Object remove(Long id){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		cartService.deleteById(id);
		return Rets.success();
	}
}
