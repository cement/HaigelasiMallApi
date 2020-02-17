package com.haigelasi.mall.api.controller.system;

import com.haigelasi.mall.web.controller.BaseController;
import com.haigelasi.mall.bean.constant.Const;
import com.haigelasi.mall.bean.constant.factory.PageFactory;
import com.haigelasi.mall.bean.constant.state.ManagerStatus;
import com.haigelasi.mall.bean.core.BussinessLog;
import com.haigelasi.mall.bean.dictmap.UserDict;
import com.haigelasi.mall.bean.dto.UserDto;
import com.haigelasi.mall.bean.entity.system.User;
import com.haigelasi.mall.bean.enumeration.BizExceptionEnum;
import com.haigelasi.mall.bean.enumeration.Permission;
import com.haigelasi.mall.bean.exception.ApplicationException;
import com.haigelasi.mall.bean.vo.front.Rets;
import com.haigelasi.mall.bean.vo.query.SearchFilter;
import com.haigelasi.mall.core.factory.UserFactory;
import com.haigelasi.mall.service.system.ManagerService;
import com.haigelasi.mall.utils.*;
import com.haigelasi.mall.utils.factory.Page;
import com.haigelasi.mall.warpper.UserWarpper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * UserController
 *
 * @author enilu
 * @version 2018/9/15 0015
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private ManagerService managerService;
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions(value = {Permission.USER})
    public Object list(@RequestParam(required = false) String account,
                       @RequestParam(required = false) String name){
        Page page = new PageFactory().defaultPage();
        if(StringUtil.isNotEmpty(name)){
            page.addFilter(SearchFilter.build("name", SearchFilter.Operator.LIKE, name));
        }
        if(StringUtil.isNotEmpty(account)){
            page.addFilter(SearchFilter.build("account", SearchFilter.Operator.LIKE, account));
        }
        page.addFilter(SearchFilter.build("status",SearchFilter.Operator.GT,0));
        page = managerService.queryPage(page);
        List list = (List) new UserWarpper(BeanUtil.objectsToMaps(page.getRecords())).warp();
        page.setRecords(list);
        return Rets.success(page);
    }
    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "编辑管理员", key = "name", dict = UserDict.class)
    @RequiresPermissions(value = {Permission.USER_EDIT})
    public Object save( @Valid UserDto user,BindingResult result){
        if(user.getId()==null) {
            // 判断账号是否重复
            User theUser = managerService.findByAccount(user.getAccount());
            if (theUser != null) {
                throw new ApplicationException(BizExceptionEnum.USER_ALREADY_REG);
            }
            // 完善账号信息
            user.setSalt(RandomUtil.getRandomString(5));
            user.setPassword(MD5.md5(user.getPassword(), user.getSalt()));
            user.setStatus(ManagerStatus.OK.getCode());
            managerService.insert(UserFactory.createUser(user, new User()));
        }else{
            User oldUser = managerService.get(user.getId());
            managerService.update(UserFactory.updateUser(user,oldUser));
        }
        return Rets.success();
    }

    @BussinessLog(value = "删除管理员", key = "userId", dict = UserDict.class)
    @RequestMapping(method = RequestMethod.DELETE)
    @RequiresPermissions(value = {Permission.USER_DEL})
    public Object remove(@RequestParam Long userId){
        if (userId == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        if(userId.intValue()<=2){
            return Rets.failure("不能删除初始用户");
        }
        User user = managerService.get(userId);
        user.setStatus(ManagerStatus.DELETED.getCode());
        managerService.update(user);
        return Rets.success();
    }
    @BussinessLog(value="设置用户角色",key="userId",dict=UserDict.class)
    @RequestMapping(value="/setRole",method = RequestMethod.GET)
    @RequiresPermissions(value = {Permission.USER_EDIT})
    public Object setRole(@RequestParam("userId") Long userId, @RequestParam("roleIds") String roleIds) {
        if (BeanUtil.isOneEmpty(userId, roleIds)) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能修改超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new ApplicationException(BizExceptionEnum.CANT_CHANGE_ADMIN);
        }
        User user = managerService.get(userId);
        user.setRoleid(roleIds);
        managerService.update(user);
        return Rets.success();
    }

}
