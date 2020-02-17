package com.haigelasi.mall.api.controller.system;

import com.haigelasi.mall.bean.entity.system.WxUser;
import com.haigelasi.mall.service.system.WxUserService;

import com.haigelasi.mall.bean.core.BussinessLog;
import com.haigelasi.mall.bean.constant.factory.PageFactory;
import com.haigelasi.mall.bean.dictmap.CommonDict;
import com.haigelasi.mall.bean.enumeration.BizExceptionEnum;
import com.haigelasi.mall.bean.exception.ApplicationException;
import com.haigelasi.mall.bean.vo.front.Rets;

import com.haigelasi.mall.utils.Maps;
import com.haigelasi.mall.utils.StringUtil;
import com.haigelasi.mall.utils.factory.Page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wx/user")
public class WxUserController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private WxUserService wxUserService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list() {
	Page<WxUser> page = new PageFactory<WxUser>().defaultPage();
		page = wxUserService.queryPage(page);
		return Rets.success(page);
	}
	@RequestMapping(method = RequestMethod.POST)
	@BussinessLog(value = "编辑微信用户", key = "name",dict= CommonDict.class)
	public Object save(@ModelAttribute WxUser wxUser){
		if(wxUser.getId()==null){
			wxUserService.insert(wxUser);
		}else {
			wxUserService.update(wxUser);
		}
		return Rets.success();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	@BussinessLog(value = "删除微信用户", key = "id",dict= CommonDict.class)
	public Object remove(Long id){
		if (StringUtil.isEmpty(id)) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		wxUserService.deleteById(id);
		return Rets.success();
	}
}
