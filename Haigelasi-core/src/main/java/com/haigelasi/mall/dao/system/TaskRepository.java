
package com.haigelasi.mall.dao.system;


import com.haigelasi.mall.bean.entity.system.Task;
import com.haigelasi.mall.dao.BaseRepository;

import java.util.List;

public interface TaskRepository extends BaseRepository<Task,Long> {

    long countByNameLike(String name);

    List<Task> findByNameLike(String name);
    List<Task> findAllByDisabled(boolean disable);
}
