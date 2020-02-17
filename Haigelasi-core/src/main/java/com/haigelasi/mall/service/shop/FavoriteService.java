package com.haigelasi.mall.service.shop;


import com.haigelasi.mall.bean.entity.shop.Favorite;
import com.haigelasi.mall.bean.vo.query.SearchFilter;
import com.haigelasi.mall.dao.shop.FavoriteRepository;
import com.haigelasi.mall.service.BaseService;
import com.haigelasi.mall.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService extends BaseService<Favorite,Long,FavoriteRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private FavoriteRepository favoriteRepository;

    public Favorite get(Long idUser, Long idGoods) {
        return get(Lists.newArrayList(
                SearchFilter.build("idUser",idUser),
                SearchFilter.build("idGoods",idGoods)
        ));

    }
}

