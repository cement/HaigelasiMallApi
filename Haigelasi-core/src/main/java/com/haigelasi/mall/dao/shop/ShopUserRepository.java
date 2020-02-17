package com.haigelasi.mall.dao.shop;


import com.haigelasi.mall.bean.entity.shop.ShopUser;
import com.haigelasi.mall.dao.BaseRepository;


public interface ShopUserRepository extends BaseRepository<ShopUser,Long> {

    ShopUser findByMobile(String mobile);
}

