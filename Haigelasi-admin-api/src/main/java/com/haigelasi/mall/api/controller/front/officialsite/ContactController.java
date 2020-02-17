package com.haigelasi.mall.api.controller.front.officialsite;

import com.haigelasi.mall.web.controller.BaseController;
import com.haigelasi.mall.bean.entity.cms.Contacts;
import com.haigelasi.mall.bean.vo.front.Rets;
import com.haigelasi.mall.service.cms.ContactsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/offcialsite/contact")
public class ContactController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ContactsService contactsService;
    @RequestMapping(method = RequestMethod.POST)
    public Object save(@Valid Contacts contacts){
        contactsService.insert(contacts);
        return Rets.success();
    }
}
