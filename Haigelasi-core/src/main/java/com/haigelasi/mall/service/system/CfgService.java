package com.haigelasi.mall.service.system;

import com.haigelasi.mall.bean.entity.system.Cfg;
import com.haigelasi.mall.bean.vo.query.SearchFilter;
import com.haigelasi.mall.cache.ConfigCache;
import com.haigelasi.mall.dao.system.CfgRepository;
import com.haigelasi.mall.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CfgService
 *
 * @author enilu
 * @version 2018/11/17 0017
 */

@Service
@Transactional
public class CfgService  extends BaseService<Cfg,Long,CfgRepository> {
    @Autowired
    private ConfigCache configCache;

    /**
     * 切记更新配置的时候调用该方法，而不要调用其父类的insert或者update方法，该方法会连同缓存一起更新
     * @param cfg
     * @return
     */
    public Cfg saveOrUpdate(Cfg cfg) {
        if(cfg.getId()==null){
            insert(cfg);
        }else{
            update(cfg);
        }
        configCache.cache();
        return cfg;
    }
    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
        configCache.cache();
    }

    public Cfg getByCfgName(String cfgName) {
        return get(SearchFilter.build("cfgName",cfgName));
    }
}
