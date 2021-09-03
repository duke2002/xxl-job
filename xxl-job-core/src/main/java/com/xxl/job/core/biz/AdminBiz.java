package com.xxl.job.core.biz;

import com.xxl.job.core.biz.model.HandleCallbackParam;
import com.xxl.job.core.biz.model.RegistryParam;
import com.xxl.job.core.biz.model.ReturnT;

import java.util.List;

/**
 * 调度中心 RESTful API
 *
 * @author xuxueli 2017-07-27 21:52:49
 */
public interface AdminBiz {


    // ---------------------- callback ----------------------

    /**
     * callback 任务回调
     * 说明：执行器执行完任务后，回调任务结果时使用
     * 地址格式：{调度中心跟地址}/callback
     * Header：
     *     XXL-JOB-ACCESS-TOKEN : {请求令牌}
     *
     * 请求数据格式如下，放置在 RequestBody 中，JSON格式：
     *     [{
     *         "logId":1,              // 本次调度日志ID
     *         "logDateTim":0,         // 本次调度日志时间
     *         "executeResult":{
     *             "code": 200,        // 200 表示任务执行正常，500表示失败
     *             "msg": null
     *         }
     *     }]
     * 响应数据格式：
     *     {
     *       "code": 200,      // 200 表示正常、其他失败
     *       "msg": null      // 错误提示消息
     *     }
     * @param callbackParamList
     * @return
     */
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList);


    // ---------------------- registry ----------------------

    /**
     * registry 执行器注册
     * 说明：执行器注册时使用，调度中心会实时感知注册成功的执行器并发起任务调度
     *
     * 地址格式：{调度中心跟地址}/registry
     * Header：
     *     XXL-JOB-ACCESS-TOKEN : {请求令牌}
     * 请求数据格式如下，放置在 RequestBody 中，JSON格式：
     *     {
     *         "registryGroup":"EXECUTOR",                     // 固定值
     *         "registryKey":"xxl-job-executor-example",       // 执行器AppName
     *         "registryValue":"http://127.0.0.1:9999/"        // 执行器地址，内置服务跟地址
     *     }
     * 响应数据格式：
     *     {
     *       "code": 200,      // 200 表示正常、其他失败
     *       "msg": null      // 错误提示消息
     *     }
     * @param registryParam
     * @return
     */
    public ReturnT<String> registry(RegistryParam registryParam);

    /**
     * registry remove 执行器注册摘除
     * 说明：执行器注册摘除时使用，注册摘除后的执行器不参与任务调度与执行
     *
     * 地址格式：{调度中心跟地址}/registryRemove
     * Header：
     *     XXL-JOB-ACCESS-TOKEN : {请求令牌}
     * 请求数据格式如下，放置在 RequestBody 中，JSON格式：
     *     {
     *         "registryGroup":"EXECUTOR",                     // 固定值
     *         "registryKey":"xxl-job-executor-example",       // 执行器AppName
     *         "registryValue":"http://127.0.0.1:9999/"        // 执行器地址，内置服务跟地址
     *     }
     * 响应数据格式：
     *     {
     *       "code": 200,      // 200 表示正常、其他失败
     *       "msg": null      // 错误提示消息
     *     }
     * @param registryParam
     * @return
     */
    public ReturnT<String> registryRemove(RegistryParam registryParam);


    // ---------------------- biz (custome) ----------------------
    // group、job ... manage

}
