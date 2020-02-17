package com.haigelasi.mall.dao.system;


import com.haigelasi.mall.bean.entity.system.Notice;
import com.haigelasi.mall.dao.BaseRepository;

import java.util.List;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
public interface NoticeRepository extends BaseRepository<Notice,Long> {
    List<Notice> findByTitleLike(String name);
}
