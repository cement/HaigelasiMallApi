package com.haigelasi.mall.bean.vo.offcialsite;

import com.haigelasi.mall.bean.entity.cms.Banner;
import lombok.Data;

import java.util.List;

@Data
public class BannerVo {
    private Integer index = 0;
    private List<Banner> list;

}
