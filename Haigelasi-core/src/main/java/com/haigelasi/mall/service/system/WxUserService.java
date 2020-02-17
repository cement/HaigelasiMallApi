package com.haigelasi.mall.service.system;


import com.haigelasi.mall.bean.entity.system.WxUser;
import com.haigelasi.mall.dao.system.WxUserRepository;
import com.haigelasi.mall.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WxUserService extends BaseService<WxUser,Long,WxUserRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private WxUserRepository wxUserRepository;

}

