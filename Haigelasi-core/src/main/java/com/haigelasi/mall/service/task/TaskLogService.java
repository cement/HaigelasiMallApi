package com.haigelasi.mall.service.task;


import com.haigelasi.mall.bean.entity.system.TaskLog;
import com.haigelasi.mall.dao.system.TaskLogRepository;
import com.haigelasi.mall.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * 定时任务日志服务类
 * @author  enilu
 * @date 2019-08-13
 */
@Service
public class TaskLogService extends BaseService<TaskLog,Long,TaskLogRepository> {
}
