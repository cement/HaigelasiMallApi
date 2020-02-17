package com.haigelasi.mall.dao.message;


import com.haigelasi.mall.bean.entity.message.MessageTemplate;
import com.haigelasi.mall.dao.BaseRepository;

import java.util.List;


public interface MessagetemplateRepository extends BaseRepository<MessageTemplate,Long> {
    MessageTemplate findByCode(String code);

    List<MessageTemplate> findByIdMessageSender(Long idMessageSender);
}

