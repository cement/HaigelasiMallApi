
package com.haigelasi.mall.dao.system;

import com.haigelasi.mall.bean.entity.system.Cfg;
import com.haigelasi.mall.dao.BaseRepository;

/**
 * 全局参数dao
 *
 * @author ：enilu
 * @date ：Created in 2019/6/29 12:50
 */
public interface CfgRepository extends BaseRepository<Cfg,Long> {

    Cfg findByCfgName(String cfgName);
}
