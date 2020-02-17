package com.haigelasi.mall.api.controller.front.officialsite;

import com.haigelasi.mall.bean.entity.cms.Article;
import com.haigelasi.mall.bean.enumeration.cms.BannerTypeEnum;
import com.haigelasi.mall.bean.enumeration.cms.ChannelEnum;
import com.haigelasi.mall.bean.vo.front.Rets;
import com.haigelasi.mall.bean.vo.offcialsite.BannerVo;
import com.haigelasi.mall.bean.vo.offcialsite.Product;
import com.haigelasi.mall.service.cms.ArticleService;
import com.haigelasi.mall.service.cms.BannerService;
import com.haigelasi.mall.utils.factory.Page;
import com.haigelasi.mall.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/offcialsite/case")
public class CaseController extends BaseController {
    @Autowired
    private BannerService bannerService;
    @Autowired
    private ArticleService articleService;

    @RequestMapping(method = RequestMethod.GET)
    public Object index() {
        Map<String, Object> dataMap = new HashMap<>(2);

        BannerVo banner = bannerService.queryBanner(BannerTypeEnum.CASE.getValue());
        dataMap.put("banner", banner);

        List<Product> products = new ArrayList<>();
        Page<Article> articlePage = articleService.query(1, 10, ChannelEnum.PRODUCT.getId());
        for (Article article : articlePage.getRecords()) {
            products.add(new Product(article.getId(), article.getTitle(), article.getImg()));
        }
        dataMap.put("caseList", products);


        Map map = new HashMap(1);

        map.put("data", dataMap);
        return Rets.success(map);

    }
}
