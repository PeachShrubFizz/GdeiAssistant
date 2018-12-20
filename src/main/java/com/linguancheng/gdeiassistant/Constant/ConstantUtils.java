package com.linguancheng.gdeiassistant.Constant;

/**
 * 广东二师助手团队 林冠成 版权所有
 * All rights reserved © 2016 - 2018
 * Author:林冠成
 * Date:2018/11/5
 */
public class ConstantUtils {

    //请求参数不合法
    public static final int INCORRECT_REQUEST_PARAM = 40001;

    //请求数据摘要不匹配
    public static final int REQUEST_SIGN_INVALID = 40002;

    //请求时间戳校验失败
    public static final int REQUEST_TIMESTAMP_INVALID = 40003;

    //重复提交的请求
    public static final int REPLAY_REQUEST = 40004;

    //权限令牌过期
    public static final int TOKEN_EXPIRED_EXCEPTION = 40101;

    //异常登录地
    public static final int UNUSUAL_LOCATION_EXCEPTION = 40102;

    //令牌校验失败
    public static final int TOKEN_NOT_MATCHING = 40103;

    //令牌校验服务异常
    public static final int TOKEN_SERVER_ERROR = 40104;

    //自定义课程数量超过限制
    public static final int CUSTOM_SCHEDULE_OVER_LIMIT = 40301;

    //查询的数据不存在
    public static final int DATA_NOT_EXIST = 40401;

    //服务器内部异常
    public static final int INTERNAL_SERVER_ERROR = 50001;

    //网络连接超时
    public static final int NETWORK_TIMEOUT = 50301;

}
