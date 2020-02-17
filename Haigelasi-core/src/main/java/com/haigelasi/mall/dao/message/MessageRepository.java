package com.haigelasi.mall.dao.message;


import com.haigelasi.mall.bean.entity.message.Message;
import com.haigelasi.mall.dao.BaseRepository;

import java.util.ArrayList;


public interface MessageRepository extends BaseRepository<Message,Long> {
    void deleteAllByIdIn(ArrayList<String> list);
}

