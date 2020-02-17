package com.haigelasi.mall.api.controller.shop;

import com.haigelasi.mall.bean.constant.factory.PageFactory;
import com.haigelasi.mall.bean.core.BussinessLog;
import com.haigelasi.mall.bean.dictmap.CommonDict;
import com.haigelasi.mall.bean.entity.shop.Goods;
import com.haigelasi.mall.bean.entity.shop.GoodsSku;
import com.haigelasi.mall.bean.enumeration.BizExceptionEnum;
import com.haigelasi.mall.bean.enumeration.Permission;
import com.haigelasi.mall.bean.exception.ApplicationException;
import com.haigelasi.mall.bean.vo.front.Rets;
import com.haigelasi.mall.bean.vo.query.SearchFilter;
import com.haigelasi.mall.service.shop.GoodsService;
import com.haigelasi.mall.service.shop.GoodsSkuService;
import com.haigelasi.mall.utils.factory.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/shop/goods")
public class GoodsController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsSkuService goodsSkuService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list(@RequestParam(value = "name",required = false) String name) {
		Page<Goods> page = new PageFactory<Goods>().defaultPage();
		page.addFilter("name", SearchFilter.Operator.LIKE,name);
		page = goodsService.queryPage(page);
		return Rets.success(page);
	}
	@RequestMapping(value = "/saveBaseInfo",method = RequestMethod.POST)
	@BussinessLog(value = "保存商品基本信息", key = "name",dict= CommonDict.class)
	@RequiresPermissions(value = {Permission.GOODS_EDIT})
	public Object saveBaseInfo(@RequestBody Goods goods){
		if(goods.getId()==null){
			goodsService.insert(goods);
		}
		return Rets.success(goods.getId());
	}
	@RequestMapping(method = RequestMethod.POST)
	@BussinessLog(value = "编辑商品", key = "name",dict= CommonDict.class)
	@RequiresPermissions(value = {Permission.GOODS_EDIT})
	public Object save(@RequestBody @Valid Goods goods){
		if(goods.getPrice() ==null){
			//如果配置了price，说明是单规格商品，则将之前配置的sku库存皆设置为0
			List<GoodsSku> skuList = goodsSkuService.queryAll(SearchFilter.build("idGoods",goods.getId()));
			if(!skuList.isEmpty()){
				int stock = 0;
				for(GoodsSku sku:skuList){
					stock+=sku.getStock();
				}
				goods.setStock(stock);
				goods.setPrice(skuList.get(0).getPrice());
				goodsSkuService.update(skuList);
			}else{
				for(GoodsSku sku:skuList){
					goods.setStock(goods.getStock()+sku.getStock());
				}
			}
		}
		if(goods.getId()==null){
			goodsService.insert(goods);
		}else {
			Goods old = goodsService.get(goods.getId());
			goods.setCreateBy(old.getCreateBy());
			goods.setCreateTime(old.getCreateTime());
			goodsService.update(goods);
		}
		return Rets.success();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	@BussinessLog(value = "删除商品", key = "id",dict= CommonDict.class)
	@RequiresPermissions(value = {Permission.GOODS_EDIT})
	public Object remove(Long id){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		goodsService.deleteById(id);
		return Rets.success();
	}

	@RequestMapping(method = RequestMethod.GET)
	public Object get(Long id){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		return Rets.success(goodsService.getDetail(id));
	}
	@RequestMapping(value="/changeIsOnSale",method = RequestMethod.POST)
	@RequiresPermissions(value = {Permission.GOODS_EDIT})
	public Object changeIsOnSale(@RequestParam("id")  Long id,@RequestParam("isOnSale") Boolean isOnSale){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		goodsService.changeIsOnSale(id,isOnSale);
		return Rets.success(goodsService.get(id));
	}

}
