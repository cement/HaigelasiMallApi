package com.haigelasi.mall.service.cms;

import com.haigelasi.mall.bean.entity.cms.Contacts;
import com.haigelasi.mall.dao.cms.ContactsRepository;
import com.haigelasi.mall.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ContactsService extends BaseService<Contacts,Long,ContactsRepository> {
}
