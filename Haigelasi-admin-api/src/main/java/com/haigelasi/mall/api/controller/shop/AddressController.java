package com.haigelasi.mall.api.controller.shop;

import com.haigelasi.mall.bean.constant.factory.PageFactory;
import com.haigelasi.mall.bean.core.BussinessLog;
import com.haigelasi.mall.bean.dictmap.CommonDict;
import com.haigelasi.mall.bean.entity.shop.Address;
import com.haigelasi.mall.bean.enumeration.BizExceptionEnum;
import com.haigelasi.mall.bean.exception.ApplicationException;
import com.haigelasi.mall.bean.vo.front.Rets;
import com.haigelasi.mall.service.shop.AddressService;
import com.haigelasi.mall.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop/address")
public class AddressController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AddressService addressService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list(@RequestParam(required = false)Long idUser) {
		Page<Address> page = new PageFactory<Address>().defaultPage();
		page.addFilter("idUser",idUser);
		page = addressService.queryPage(page);
		return Rets.success(page);
	}
	@RequestMapping(method = RequestMethod.POST)
	@BussinessLog(value = "编辑收货地址", key = "name",dict= CommonDict.class)
	public Object save(@ModelAttribute Address tShopAddress){
		if(tShopAddress.getId()==null){
			addressService.insert(tShopAddress);
		}else {
			addressService.update(tShopAddress);
		}
		return Rets.success();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	@BussinessLog(value = "删除收货地址", key = "id",dict= CommonDict.class)
	public Object remove(Long id){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		addressService.deleteById(id);
		return Rets.success();
	}
}
