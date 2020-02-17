package com.haigelasi.mall.mobile.controller;

import com.haigelasi.mall.bean.entity.promotion.Topic;
import com.haigelasi.mall.bean.entity.shop.Goods;
import com.haigelasi.mall.bean.vo.front.Rets;
import com.haigelasi.mall.bean.vo.query.SearchFilter;
import com.haigelasi.mall.service.promotion.TopicService;
import com.haigelasi.mall.service.shop.GoodsService;
import com.haigelasi.mall.utils.Convert;
import com.haigelasi.mall.utils.StringUtil;
import com.haigelasi.mall.utils.factory.Page;
import com.haigelasi.mall.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：enilu
 * @date ：Created in 1/9/2020 9:06 PM
 */
@RestController
@RequestMapping("/topic")
public class TopicController extends BaseController {
    @Autowired
    private TopicService topicService;
    @Autowired
    private GoodsService goodsService;
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Object list() {
        Page<Topic> page = new Page<Topic>();
        page.setSize(6);
        page.setSort(Sort.by(Sort.Direction.ASC,"id"));
        page.addFilter(SearchFilter.build("disabled",false));
        page = topicService.queryPage(page);

        return Rets.success(page.getRecords());
    }
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
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
