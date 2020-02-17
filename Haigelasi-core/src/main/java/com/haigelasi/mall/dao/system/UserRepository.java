package com.haigelasi.mall.dao.system;


import com.haigelasi.mall.bean.entity.system.User;
import com.haigelasi.mall.dao.BaseRepository;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
public interface UserRepository extends BaseRepository<User,Long> {
    User findByAccount(String account);

    User findByAccountAndStatusNot(String account, Integer status);
}
