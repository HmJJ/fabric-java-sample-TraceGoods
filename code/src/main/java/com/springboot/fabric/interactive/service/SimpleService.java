package com.springboot.fabric.interactive.service;

import com.alibaba.fastjson.JSONObject;
import com.springboot.fabric.base.BaseService;


/**
 * 描述：
 *
 */
public interface SimpleService extends BaseService {

    /**
     * 合约接口
     *
     * @param json 合约JSON
     * @return 请求返回
     */
    String chainCode(JSONObject json);

    /**
     * 溯源接口
     *
     * @param json 溯源JSON
     * @return 请求返回
     */
    String trace(JSONObject json);

}
