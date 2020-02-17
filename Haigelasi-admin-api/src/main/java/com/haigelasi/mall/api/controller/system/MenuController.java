package com.haigelasi.mall.api.controller.system;

import com.haigelasi.mall.bean.constant.state.MenuStatus;
import com.haigelasi.mall.bean.core.AuthorizationUser;
import com.haigelasi.mall.bean.core.BussinessLog;
import com.haigelasi.mall.bean.dictmap.MenuDict;
import com.haigelasi.mall.bean.entity.system.Menu;
import com.haigelasi.mall.bean.enumeration.BizExceptionEnum;
import com.haigelasi.mall.bean.enumeration.Permission;
import com.haigelasi.mall.bean.exception.ApplicationException;
import com.haigelasi.mall.bean.vo.front.Rets;
import com.haigelasi.mall.bean.vo.node.MenuNode;
import com.haigelasi.mall.bean.vo.node.Node;
import com.haigelasi.mall.bean.vo.node.RouterMenu;
import com.haigelasi.mall.bean.vo.node.ZTreeNode;
import com.haigelasi.mall.cache.TokenCache;
import com.haigelasi.mall.service.system.LogObjectHolder;
import com.haigelasi.mall.service.system.MenuService;
import com.haigelasi.mall.service.system.impl.ConstantFactory;
import com.haigelasi.mall.utils.HttpUtil;
import com.haigelasi.mall.utils.Lists;
import com.haigelasi.mall.utils.Maps;
import com.haigelasi.mall.utils.StringUtil;
import com.haigelasi.mall.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * MenuController
 *
 * @author enilu
 * @version 2018/9/12 0012
 */
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(MenuController.class);
    @Autowired
    private MenuService menuService;
    @Autowired
    private TokenCache tokenCache;

    @RequestMapping(value = "/listForRouter", method = RequestMethod.GET)
    /*此处权限验证将导致没有menu的用户获取不到菜单列表*/
//    @RequiresPermissions(value = {Permission.MENU})
    public Object listForRouter() {
        AuthorizationUser shiroUser = tokenCache.getUser(HttpUtil.getToken());

        List<RouterMenu> list = menuService.getSideBarMenus(shiroUser.getRoleList());
        return Rets.success(list);
    }
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiresPermissions(value = {Permission.MENU})
    public Object list() {
        List<MenuNode> list = menuService.getMenus();
        return Rets.success(list);
    }

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "编辑菜单", key = "name", dict = MenuDict.class)
    @RequiresPermissions(value = {Permission.MENU_EDIT})
    public Object save(@ModelAttribute @Valid Menu menu) {
        //判断是否存在该编号
        if(menu.getId()==null) {
            String existedMenuName = ConstantFactory.me().getMenuNameByCode(menu.getCode());
            if (StringUtil.isNotEmpty(existedMenuName)) {
                throw new ApplicationException(BizExceptionEnum.EXISTED_THE_MENU);
            }
            menu.setStatus(MenuStatus.ENABLE.getCode());
        }

        //设置父级菜单编号
        menuService.menuSetPcode(menu);
        if(menu.getId()==null){
            menuService.insert(menu);
        }else {
            menuService.update(menu);
        }
        return Rets.success();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @BussinessLog(value = "删除菜单", key = "id", dict = MenuDict.class)
    @RequiresPermissions(value = {Permission.MENU_DEL})
    public Object remove(@RequestParam Long id) {
        logger.info("id:{}", id);
        if (id == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        //演示环境不允许删除初始化的菜单
        if(id.intValue()<70){
            return Rets.failure("演示环境不允许删除初始菜单");
        }
        //缓存菜单的名称
        LogObjectHolder.me().set(ConstantFactory.me().getMenuName(id));
        menuService.delMenuContainSubMenus(id);
        return Rets.success();
    }

    /**
     * 获取菜单树
     */
    @RequestMapping(value = "/menuTreeListByRoleId", method = RequestMethod.GET)
    @RequiresPermissions(value = {Permission.MENU})
    public Object menuTreeListByRoleId(Integer roleId) {
        List<Long> menuIds = menuService.getMenuIdsByRoleId(roleId);
        List<ZTreeNode> roleTreeList = null;
        if (menuIds==null||menuIds.isEmpty()) {
            roleTreeList = menuService.menuTreeList(null);
        } else {
            roleTreeList = menuService.menuTreeList(menuIds);

        }
        List<Node> list = menuService.generateMenuTreeForRole(roleTreeList);

        //element-ui中tree控件中如果选中父节点会默认选中所有子节点，所以这里将所有非叶子节点去掉
        Map<Long,ZTreeNode> map = Lists.toMap(roleTreeList,"id");
        Map<Long,List<ZTreeNode>> group = Lists.group(roleTreeList,"pId");
        for(Map.Entry<Long,List<ZTreeNode>> entry:group.entrySet()){
            if(entry.getValue().size()>1){
                roleTreeList.remove(map.get(entry.getKey()));
            }
        }

        List<Long> checkedIds = Lists.newArrayList();
        for (ZTreeNode zTreeNode : roleTreeList) {
            if (zTreeNode.getChecked() != null && zTreeNode.getChecked()
                    &&zTreeNode.getpId().intValue()!=0) {
                checkedIds.add(zTreeNode.getId());
            }
        }
        return Rets.success(Maps.newHashMap("treeData", list, "checkedIds", checkedIds));
    }
}
