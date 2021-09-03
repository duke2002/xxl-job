package com.xxl.job.core.biz;

import com.xxl.job.core.biz.model.*;

/**
 * 执行器 RESTful API
 *
 * Created by xuxueli on 17/3/1.
 */
public interface ExecutorBiz {

    /**
     * beat 心跳检测
     *
     * 说明：调度中心检测执行器是否在线时使用
     * ------
     * 地址格式：{执行器内嵌服务跟地址}/beat
     * Header：
     *     XXL-JOB-ACCESS-TOKEN : {请求令牌}
     * 请求数据格式如下，放置在 RequestBody 中，JSON格式：
     * 响应数据格式：
     *     {
     *       "code": 200,      // 200 表示正常、其他失败
     *       "msg": null       // 错误提示消息
     *     }
     * @return
     */
    public ReturnT<String> beat();

    /**
     * idle beat 忙碌检测
     *
     * 说明：调度中心检测指定执行器上指定任务是否忙碌（运行中）时使用
     * ------
     * 地址格式：{执行器内嵌服务跟地址}/idleBeat
     * Header：
     *     XXL-JOB-ACCESS-TOKEN : {请求令牌}
     * 请求数据格式如下，放置在 RequestBody 中，JSON格式：
     *     {
     *         "jobId":1       // 任务ID
     *     }
     * 响应数据格式：
     *     {
     *       "code": 200,      // 200 表示正常、其他失败
     *       "msg": null       // 错误提示消息
     *     }
     *
     * @param idleBeatParam
     * @return
     */
    public ReturnT<String> idleBeat(IdleBeatParam idleBeatParam);

    /**
     * run 触发任务
     *
     * 说明：触发任务执行
     * ------
     * 地址格式：{执行器内嵌服务跟地址}/run
     * Header：
     *     XXL-JOB-ACCESS-TOKEN : {请求令牌}
     * 请求数据格式如下，放置在 RequestBody 中，JSON格式：
     *     {
     *         "jobId":1,                                  // 任务ID
     *         "executorHandler":"demoJobHandler",         // 任务标识
     *         "executorParams":"demoJobHandler",          // 任务参数
     *         "executorBlockStrategy":"COVER_EARLY",      // 任务阻塞策略，可选值参考 com.xxl.job.core.enums.ExecutorBlockStrategyEnum
     *         "executorTimeout":0,                        // 任务超时时间，单位秒，大于零时生效
     *         "logId":1,                                  // 本次调度日志ID
     *         "logDateTime":1586629003729,                // 本次调度日志时间
     *         "glueType":"BEAN",                          // 任务模式，可选值参考 com.xxl.job.core.glue.GlueTypeEnum
     *         "glueSource":"xxx",                         // GLUE脚本代码
     *         "glueUpdatetime":1586629003727,             // GLUE脚本更新时间，用于判定脚本是否变更以及是否需要刷新
     *         "broadcastIndex":0,                         // 分片参数：当前分片
     *         "broadcastTotal":0                          // 分片参数：总分片
     *     }
     * 响应数据格式：
     *     {
     *       "code": 200,      // 200 表示正常、其他失败
     *       "msg": null       // 错误提示消息
     *     }
     * @param triggerParam
     * @return
     */
    public ReturnT<String> run(TriggerParam triggerParam);

    /**
     * kill 终止任务
     *
     * 说明：终止任务
     * ------
     * 地址格式：{执行器内嵌服务跟地址}/kill
     * Header：
     *     XXL-JOB-ACCESS-TOKEN : {请求令牌}
     * 请求数据格式如下，放置在 RequestBody 中，JSON格式：
     *     {
     *         "jobId":1       // 任务ID
     *     }
     * 响应数据格式：
     *     {
     *       "code": 200,      // 200 表示正常、其他失败
     *       "msg": null       // 错误提示消息
     *     }
     * @param killParam
     * @return
     */
    public ReturnT<String> kill(KillParam killParam);

    /**
     * log 查看执行日志
     *
     * 说明：终止任务，滚动方式加载
     * ------
     * 地址格式：{执行器内嵌服务跟地址}/log
     * Header：
     *     XXL-JOB-ACCESS-TOKEN : {请求令牌}
     * 请求数据格式如下，放置在 RequestBody 中，JSON格式：
     *     {
     *         "logDateTim":0,     // 本次调度日志时间
     *         "logId":0,          // 本次调度日志ID
     *         "fromLineNum":0     // 日志开始行号，滚动加载日志
     *     }
     * 响应数据格式：
     *     {
     *         "code":200,         // 200 表示正常、其他失败
     *         "msg": null         // 错误提示消息
     *         "content":{
     *             "fromLineNum":0,        // 本次请求，日志开始行数
     *             "toLineNum":100,        // 本次请求，日志结束行号
     *             "logContent":"xxx",     // 本次请求日志内容
     *             "isEnd":true            // 日志是否全部加载完
     *         }
     *     }
     *
     * @param logParam
     * @return
     */
    public ReturnT<LogResult> log(LogParam logParam);

}
