package com.haigelasi.mall.service.cms;

import com.haigelasi.mall.bean.entity.cms.Article;
import com.haigelasi.mall.bean.enumeration.cms.ChannelEnum;
import com.haigelasi.mall.bean.vo.query.SearchFilter;
import com.haigelasi.mall.dao.cms.ArticleRepository;
import com.haigelasi.mall.service.BaseService;
import com.haigelasi.mall.utils.factory.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService extends BaseService<Article, Long, ArticleRepository> {
    @Autowired
    private ArticleRepository articleRepository;

    /**
     * 查询首页最近5条资讯数据
     *
     * @return
     */
    public List<Article> queryIndexNews() {
        Page<Article> page = query(1, 5, ChannelEnum.NEWS.getId());
        return page.getRecords();
    }

    public Page<Article> query(int currentPage, int size, Long idChannel) {
        Page page = new Page(currentPage, size, "id");
        page.addFilter(SearchFilter.build("idChannel", SearchFilter.Operator.EQ, idChannel));
        return queryPage(page);

    }
}
