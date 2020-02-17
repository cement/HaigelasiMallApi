package com.haigelasi.mall.api.controller.promotion;

import com.haigelasi.mall.bean.constant.factory.PageFactory;
import com.haigelasi.mall.bean.core.BussinessLog;
import com.haigelasi.mall.bean.dictmap.CommonDict;
import com.haigelasi.mall.bean.entity.promotion.Topic;
import com.haigelasi.mall.bean.entity.shop.Goods;
import com.haigelasi.mall.bean.enumeration.BizExceptionEnum;
import com.haigelasi.mall.bean.enumeration.Permission;
import com.haigelasi.mall.bean.exception.ApplicationException;
import com.haigelasi.mall.bean.vo.front.Rets;
import com.haigelasi.mall.bean.vo.query.SearchFilter;
import com.haigelasi.mall.service.promotion.TopicService;
import com.haigelasi.mall.service.shop.GoodsService;
import com.haigelasi.mall.utils.Convert;
import com.haigelasi.mall.utils.DateUtil;
import com.haigelasi.mall.utils.StringUtil;
import com.haigelasi.mall.utils.factory.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotion/topic")
public class TopicController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private TopicService topicService;
	@Autowired
	private GoodsService goodsService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list(@RequestParam(value = "disabled",required = false) Boolean disabled,
					   @RequestParam(value = "startDate",required = false) String startDate,
					   @RequestParam(value = "endDate",required = false) String endDate) {
		Page<Topic> page = new PageFactory<Topic>().defaultPage();
		page.addFilter( "disabled", disabled);
		page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parse(startDate,"yyyyMMddHHmmss"));
		page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parse(endDate,"yyyyMMddHHmmss"));
		page = topicService.queryPage(page);
		return Rets.success(page);
	}

	@RequestMapping(method = RequestMethod.POST)
	@BussinessLog(value = "编辑专题", key = "name",dict= CommonDict.class)
	@RequiresPermissions(value = {Permission.TOPIC_EDIT})
	public Object save(@ModelAttribute Topic topic){
		if(topic.getId()==null){
			topic.setPv(0L);
			topicService.insert(topic);
		}else {
			topicService.update(topic);
		}
		return Rets.success();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	@BussinessLog(value = "删除专题", key = "id",dict= CommonDict.class)
	@RequiresPermissions(value = {Permission.TOPIC_DEL})
	public Object remove(Long id){
		if (StringUtil.isEmpty(id)) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		topicService.deleteById(id);
		return Rets.success();
	}

	@RequestMapping(value="/changeDisabled",method = RequestMethod.POST)
	@RequiresPermissions(value = {Permission.TOPIC_EDIT})
	public Object changeIsOnSale(@RequestParam("id")  Long id,@RequestParam("disabled") Boolean disabled){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		topicService.changeDisabled(id,disabled);
		return Rets.success();
	}

	@RequestMapping(value="/{id:\\d}",method = RequestMethod.GET)
	public Object get(@PathVariable("id") Long id){
		Topic topic = topicService.get(id);
		topic.setPv(topic.getPv()+1);
		topicService.update(topic);
		String idGoodsList = topic.getIdGoodsList();
		if(StringUtil.isNotEmpty(idGoodsList)){
			Long[] idGoodsArr = Convert.toLongArray(",",idGoodsList);
			List<Goods> goodsList = goodsService.queryAll(SearchFilter.build("id", SearchFilter.Operator.IN,idGoodsArr));
			topic.setGoodsList(goodsList);

		}
		return Rets.success(topic);
	}
}
