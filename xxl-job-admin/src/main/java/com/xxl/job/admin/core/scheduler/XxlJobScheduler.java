package com.xxl.job.admin.core.scheduler;

import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import com.xxl.job.admin.core.thread.*;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.job.core.biz.client.ExecutorBizClient;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xuxueli 2018-10-28 00:18:17
 */

public class XxlJobScheduler  {
    private static final Logger logger = LoggerFactory.getLogger(XxlJobScheduler.class);


    public void init() throws Exception {
        // init i18n
        // 初始化语言配置（application.properties中的xxl.job.i18n）
        // 初始化新增任务页面中的阻塞处理策略相关的国际化信息。国际化提供中文、英文两种可选语言，默认为中文
        initI18n();

        // admin trigger pool start
        //  配置任务触发线程池
        // admin trigger pool start  初始化快慢线程池
        JobTriggerPoolHelper.toStart();

        // admin registry monitor run
        // 2. 启动注册监控器（将注册到register表中的IP加载到group表）/ 30执行一次
        // 执行器管理：及时新增30s内新注册的执行器，清除90s内未再次注册的执行器（默认心跳保活时间30s）
        //  初始化一个上下线线程池；启动守护线程：清理下线的执行器（3次心跳），增加上线的执行器
        JobRegistryHelper.getInstance().start();

        // admin fail-monitor run
        // 3. 启动失败日志监控器（失败重试，失败邮件发送）
        JobFailMonitorHelper.getInstance().start();

        // admin lose-monitor run ( depend on JobTriggerPoolHelper )
        // 执行器执行任务10min内没有给出结果回复，终止该任务
        //  启动两个守护线程进行任务调度
        // 初始化一个回调线程池，然后一个守护线程:定期扫描10min没有完成且执行器失去心跳的实例，置为失败
        JobCompleteHelper.getInstance().start();

        // admin log report start
        // 任务执行状态归总，用于后台管理”运行报表"数据显示；同时，如果日志到达清除时间，则清除日志（清除数据库中的日志）
        // 启动一个日志处理线程
        JobLogReportHelper.getInstance().start();

        // start-schedule  ( depend on JobTriggerPoolHelper )
        // 5. 启动定时任务调度器（执行任务，缓存任务）
        // 核心中的核心，用于任务触发
        JobScheduleHelper.getInstance().start();

        logger.info(">>>>>>>>> init xxl-job admin success.");
    }

    
    public void destroy() throws Exception {

        // stop-schedule
        JobScheduleHelper.getInstance().toStop();

        // admin log report stop
        JobLogReportHelper.getInstance().toStop();

        // admin lose-monitor stop
        JobCompleteHelper.getInstance().toStop();

        // admin fail-monitor stop
        JobFailMonitorHelper.getInstance().toStop();

        // admin registry stop
        JobRegistryHelper.getInstance().toStop();

        // admin trigger pool stop
        JobTriggerPoolHelper.toStop();

    }

    // ---------------------- I18n ----------------------

    private void initI18n(){
        for (ExecutorBlockStrategyEnum item:ExecutorBlockStrategyEnum.values()) {
            item.setTitle(I18nUtil.getString("jobconf_block_".concat(item.name())));
        }
    }

    // ---------------------- executor-client ----------------------
    private static ConcurrentMap<String, ExecutorBiz> executorBizRepository = new ConcurrentHashMap<String, ExecutorBiz>();
    public static ExecutorBiz getExecutorBiz(String address) throws Exception {
        // valid
        if (address==null || address.trim().length()==0) {
            return null;
        }

        // load-cache
        address = address.trim();
        ExecutorBiz executorBiz = executorBizRepository.get(address);
        if (executorBiz != null) {
            return executorBiz;
        }

        // set-cache
        executorBiz = new ExecutorBizClient(address, XxlJobAdminConfig.getAdminConfig().getAccessToken());

        executorBizRepository.put(address, executorBiz);
        return executorBiz;
    }

}
