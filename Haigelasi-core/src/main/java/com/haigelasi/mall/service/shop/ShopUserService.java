package com.haigelasi.mall.service.shop;


import com.haigelasi.mall.bean.entity.shop.ShopUser;
import com.haigelasi.mall.cache.CacheDao;
import com.haigelasi.mall.dao.shop.ShopUserRepository;
import com.haigelasi.mall.security.JwtUtil;
import com.haigelasi.mall.service.BaseService;
import com.haigelasi.mall.utils.HttpUtil;
import com.haigelasi.mall.utils.MD5;
import com.haigelasi.mall.utils.RandomUtil;
import com.haigelasi.mall.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ShopUserService extends BaseService<ShopUser,Long,ShopUserRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ShopUserRepository shopUserRepository;
    @Autowired
    private CacheDao cacheDao;

    public ShopUser findByMobile(String mobile) {
        return shopUserRepository.findByMobile(mobile);
    }

    public Boolean validateSmsCode(String mobile, String smsCode) {
        //todo 测试验证逻辑，暂不实现
        String smsCode2 = (String) cacheDao.hget(CacheDao.SESSION,mobile+"_smsCode");
        return StringUtil.equals(smsCode,smsCode2);
    }

    public String sendSmsCode(String mobile) {
        //todo 发送短信验证码逻辑，暂不实现
        String smsCode = RandomUtil.getRandomNumber(4);
        cacheDao.hset(CacheDao.SESSION,mobile+"_smsCode",smsCode);
        HttpUtil.getRequest().getSession().setAttribute(mobile+"_smsCode",smsCode);
        return smsCode;
    }

    public String sendSmsCodeForOldMobile(String mobile) {
        //todo 发送短信验证码逻辑，暂不实现
        String smsCode = RandomUtil.getRandomNumber(4);
        cacheDao.hset(CacheDao.SESSION,mobile+"_smsCode",smsCode);
        HttpUtil.getRequest().getSession().setAttribute(mobile+"_smsCode",smsCode);
        return smsCode;
    }


    public ShopUser register(String mobile,String initPwd) {
        ShopUser user = new ShopUser();
        user.setMobile(mobile);
        user.setNickName(mobile);
        user.setCreateTime(new Date());
        user.setSalt(RandomUtil.getRandomString(5));

        user.setPassword(MD5.md5(initPwd, user.getSalt()));

        insert(user);
        return user;
    }

    public ShopUser getCurrentUser() {
        return get(JwtUtil.getUserId());
    }
}

