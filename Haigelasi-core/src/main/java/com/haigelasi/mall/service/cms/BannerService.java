package com.haigelasi.mall.service.cms;

import com.haigelasi.mall.bean.entity.cms.Banner;
import com.haigelasi.mall.bean.enumeration.cms.BannerTypeEnum;
import com.haigelasi.mall.bean.vo.offcialsite.BannerVo;
import com.haigelasi.mall.dao.cms.BannerRepository;
import com.haigelasi.mall.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerService extends BaseService<Banner,Long,BannerRepository> {
    @Autowired
    private BannerRepository bannerRepository;

    /**
     * 查询首页banner数据
     * @return
     */
    public BannerVo queryIndexBanner(){
    return queryBanner(BannerTypeEnum.INDEX.getValue());
    }

    public BannerVo queryBanner(String type){
        BannerVo banner = new BannerVo();
        List<Banner> bannerList = bannerRepository.findAllByType(type);
        banner.setIndex(0);
        banner.setList(bannerList);
        return  banner;
    }
}
