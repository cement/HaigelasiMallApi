package com.haigelasi.mall.api.controller.shop;

import com.haigelasi.mall.bean.constant.factory.PageFactory;
import com.haigelasi.mall.bean.core.BussinessLog;
import com.haigelasi.mall.bean.dictmap.CommonDict;
import com.haigelasi.mall.bean.entity.shop.GoodsSku;
import com.haigelasi.mall.bean.enumeration.BizExceptionEnum;
import com.haigelasi.mall.bean.enumeration.Permission;
import com.haigelasi.mall.bean.exception.ApplicationException;
import com.haigelasi.mall.bean.vo.front.Rets;
import com.haigelasi.mall.bean.vo.query.SearchFilter;
import com.haigelasi.mall.service.shop.GoodsSkuService;
import com.haigelasi.mall.utils.Lists;
import com.haigelasi.mall.utils.StringUtil;
import com.haigelasi.mall.utils.factory.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/shop/goods/sku")
public class GoodsSkuController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private GoodsSkuService goodsSkuService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list() {
	Page<GoodsSku> page = new PageFactory<GoodsSku>().defaultPage();
		page = goodsSkuService.queryPage(page);
		return Rets.success(page);
	}
	@RequestMapping(method = RequestMethod.POST)
	@BussinessLog(value = "编辑商品SKU", key = "name",dict= CommonDict.class)
	@RequiresPermissions(value = {Permission.GOODS_EDIT})
	public Object save(@RequestBody GoodsSku sku){
		List<String> codeArr = Arrays.asList(sku.getCode().split(","));
		List<String> codeNameArr = Arrays.asList(sku.getCodeName().split(","));
		Collections.sort(codeArr, new Comparator<String>() {
			@Override
			public int compare(String  s1, String s2) {
				return s1.compareTo(s2);
			}
		});
		Collections.sort(codeNameArr, new Comparator<String>() {
			@Override
			public int compare(String  s1, String s2) {
				return s1.compareTo(s2);
			}
		});
		String code = Lists.concat(codeArr,",");
		String codeName = Lists.concat(codeNameArr,",");
		sku.setCode(code);
		sku.setCodeName(codeName);
		GoodsSku oldSku = goodsSkuService.get(Lists.newArrayList(
				SearchFilter.build("idGoods",sku.getIdGoods()),
				SearchFilter.build("code",code)
		));
		if(oldSku!=null){
			oldSku.setMarketingPrice(sku.getMarketingPrice());
			oldSku.setPrice(sku.getPrice());
			oldSku.setStock(sku.getStock());
			goodsSkuService.update(oldSku);
			return Rets.success(oldSku);
		}else{
			goodsSkuService.insert(sku);
			return Rets.success(sku);
		}

	}
	@RequestMapping(method = RequestMethod.DELETE)
	@BussinessLog(value = "删除商品SKU", key = "id",dict= CommonDict.class)
	@RequiresPermissions(value = {Permission.GOODS_EDIT})
	public Object remove(Long id){
		if (StringUtil.isEmpty(id)) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		goodsSkuService.deleteById(id);
		return Rets.success();
	}
}
