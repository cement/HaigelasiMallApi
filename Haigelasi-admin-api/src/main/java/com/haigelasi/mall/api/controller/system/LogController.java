package com.haigelasi.mall.api.controller.system;

import com.haigelasi.mall.web.controller.BaseController;
import com.haigelasi.mall.bean.constant.factory.PageFactory;
import com.haigelasi.mall.bean.constant.state.BizLogType;
import com.haigelasi.mall.bean.entity.system.OperationLog;
import com.haigelasi.mall.bean.enumeration.Permission;
import com.haigelasi.mall.bean.vo.front.Rets;
import com.haigelasi.mall.bean.vo.query.SearchFilter;
import com.haigelasi.mall.service.system.OperationLogService;
import com.haigelasi.mall.utils.BeanUtil;
import com.haigelasi.mall.utils.DateUtil;
import com.haigelasi.mall.utils.HttpUtil;
import com.haigelasi.mall.utils.factory.Page;
import com.haigelasi.mall.warpper.LogWarpper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LogController
 *
 * @author enilu
 * @version 2018/10/5 0005
 */
@RestController
@RequestMapping("/log")
public class LogController extends BaseController {
    @Autowired
    private OperationLogService operationLogService;

    /**
     * 查询操作日志列表
     */
    @RequestMapping("/list")
    @ResponseBody
    @RequiresPermissions(value = {Permission.LOG})
    public Object list(@RequestParam(required = false) String beginTime,
                       @RequestParam(required = false) String endTime,
                       @RequestParam(required = false) String logName,
                       @RequestParam(required = false) Integer logType) {
        Page<OperationLog> page = new PageFactory<OperationLog>().defaultPage();
        page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parseDate(beginTime));
        page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parseDate(endTime));
        page.addFilter( "logname", SearchFilter.Operator.LIKE, logName);
        if (logType != null) {
            page.addFilter(SearchFilter.build("logtype", SearchFilter.Operator.EQ, BizLogType.valueOf(logType)));
        }
        page = operationLogService.queryPage(page);
        page.setRecords((List<OperationLog>) new LogWarpper(BeanUtil.objectsToMaps(page.getRecords())).warp());
        return Rets.success(page);
    }

    /**
     * 查询指定用户的操作日志列表
     */
    @RequestMapping("/queryByUser")
    @ResponseBody
    @RequiresPermissions(value = {Permission.LOG})
    public Object list() {
        Page<OperationLog> page = new Page<OperationLog>();
        page.addFilter(SearchFilter.build("userid", SearchFilter.Operator.EQ, getIdUser(HttpUtil.getRequest())));
        Page<OperationLog> pageResult = operationLogService.queryPage(page);
        return Rets.success(pageResult.getRecords());
    }

    /**
     * 清空日志
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @RequiresPermissions(value = {Permission.LOG_CLEAR})
    public Object delLog() {
        operationLogService.clear();
        return Rets.success();
    }
}
