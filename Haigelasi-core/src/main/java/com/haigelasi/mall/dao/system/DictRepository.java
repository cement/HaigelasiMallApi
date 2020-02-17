
package com.haigelasi.mall.dao.system;


import com.haigelasi.mall.bean.entity.system.Dict;
import com.haigelasi.mall.dao.BaseRepository;

import java.util.List;

public interface DictRepository  extends BaseRepository<Dict,Long> {
    List<Dict> findByPid(Long pid);
    List<Dict> findByNameAndPid(String name,Long pid);

    List<Dict> findByNameLike(String name);
}
