
package com.haigelasi.mall.dao.cms;

import com.haigelasi.mall.bean.entity.cms.Banner;
import com.haigelasi.mall.dao.BaseRepository;

import java.util.List;

public interface BannerRepository extends BaseRepository<Banner,Long> {
    /**
     * 查询指定类别的banner列表
     * @param type
     * @return
     */
    List<Banner> findAllByType(String type);
}
