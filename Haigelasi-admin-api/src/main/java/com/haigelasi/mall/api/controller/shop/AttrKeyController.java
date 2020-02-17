package com.haigelasi.mall.api.controller.shop;

import com.haigelasi.mall.bean.entity.shop.AttrKey;
import com.haigelasi.mall.service.shop.AttrKeyService;

import com.haigelasi.mall.bean.core.BussinessLog;
import com.haigelasi.mall.bean.constant.factory.PageFactory;
import com.haigelasi.mall.bean.dictmap.CommonDict;
import com.haigelasi.mall.bean.enumeration.BizExceptionEnum;
import com.haigelasi.mall.bean.exception.ApplicationException;
import com.haigelasi.mall.bean.vo.front.Rets;

import com.haigelasi.mall.utils.StringUtil;
import com.haigelasi.mall.utils.factory.Page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop/goods/attr/key")
public class AttrKeyController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AttrKeyService attrKeyService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list() {
	Page<AttrKey> page = new PageFactory<AttrKey>().defaultPage();
		page = attrKeyService.queryPage(page);
		return Rets.success(page);
	}
	@RequestMapping(method = RequestMethod.POST)
	@BussinessLog(value = "编辑商品属性名", key = "name",dict= CommonDict.class)
	public Object save(@ModelAttribute AttrKey tShopGoodsAttrKey){
		if(tShopGoodsAttrKey.getId()==null){
			attrKeyService.insert(tShopGoodsAttrKey);
		}else {
			attrKeyService.update(tShopGoodsAttrKey);
		}
		return Rets.success();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	@BussinessLog(value = "删除商品属性名", key = "id",dict= CommonDict.class)
	public Object remove(Long id){
		if (StringUtil.isEmpty(id)) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		attrKeyService.deleteById(id);
		return Rets.success();
	}
}
