package com.springboot.fabric.base;

import com.alibaba.fastjson.JSONObject;

/**
 * 业务层基类
 * 
 */
public interface BaseService {

	int SUCCESS = 200;
	int FAIL = 40029;
	int UN_LOGIN = 8;

	default String responseSuccess(String result) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", SUCCESS);
		jsonObject.put("result", result);
		return jsonObject.toString();
	}

	default String responseSuccess(String result, String txid) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", SUCCESS);
		jsonObject.put("result", result);
		jsonObject.put("txid", txid);
		System.out.println("result:"+result);
		return jsonObject.toString();
	}

	default String responseSuccess(JSONObject json) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", SUCCESS);
		jsonObject.put("data", json);
		return jsonObject.toString();
	}

	default String responseFail(String result) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", FAIL);
		jsonObject.put("error", result);
		return jsonObject.toString();
	}

	default String responseUnLogin() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", UN_LOGIN);
		return jsonObject.toString();
	}

}
