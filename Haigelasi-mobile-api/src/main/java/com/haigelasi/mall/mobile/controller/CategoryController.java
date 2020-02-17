package com.haigelasi.mall.mobile.controller;

import com.haigelasi.mall.bean.entity.cms.Banner;
import com.haigelasi.mall.bean.entity.shop.Category;
import com.haigelasi.mall.bean.entity.shop.CategoryBannerRel;
import com.haigelasi.mall.bean.vo.front.Rets;
import com.haigelasi.mall.bean.vo.query.SearchFilter;
import com.haigelasi.mall.service.shop.CategoryBannerRelService;
import com.haigelasi.mall.service.shop.CategoryService;
import com.haigelasi.mall.utils.Lists;
import com.haigelasi.mall.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：enilu
 * @date ：Created in 11/4/2019 9:06 PM
 */
@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryBannerRelService categoryBannerRelService;
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Object list() {
        List<Category> list = categoryService.queryAll();
        list.forEach(item->{
            List<CategoryBannerRel> relList = categoryBannerRelService.queryAll(SearchFilter.build("idCategory",item.getId()));
            List<Banner> bannerList = Lists.newArrayList();
            relList.forEach( relItem->{
                bannerList.add(relItem.getBanner());
            });

            item.setBannerList(bannerList);
        });
        return Rets.success(list);
    }
}
