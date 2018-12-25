package com.springboot.code.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.springboot.basic.support.CommonRequestAttributes;
import com.springboot.fabric.interactive.service.SimpleService;

@Service(value = "logisticService")
public class LogisticService {

	@Autowired private SimpleService simpleService;

	/**
	 * @param attributes
	 * @param id
	 * @param goodsId
	 * @param cityName
	 * @return
	 */
	public String add(CommonRequestAttributes attributes, List<String> params) {

		Map<String, Object> map = new HashMap<>();
		map.put("type", "invoke");
		map.put("fcn", "addLogistic");

		int length = params.size();
        String[] argArray = new String[length];
        for (int i = 0; i < length; i++) {
            argArray[i] = params.get(i);
        }
		
		map.put("array", argArray);
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		return jsonStr;
	}

	public String findAll(CommonRequestAttributes attributes) {

		Map<String, Object> map = new HashMap<>();
		map.put("type", "query");
		map.put("fcn", "queryAllLogistic");
		map.put("array", new String[] {});
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		return jsonStr;
	}

	public String modify(CommonRequestAttributes attributes, List<String> params) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("type", "invoke");
		map.put("fcn", "modifyLogistic");

		int length = params.size();
        String[] argArray = new String[length];
        for (int i = 0; i < length; i++) {
            argArray[i] = params.get(i);
        }
		
		map.put("array", argArray);
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		return jsonStr;
	}

	/**
	 * @param id
	 * @return
	 */
	public String delete(CommonRequestAttributes attributes, List<String> params) {

		Map<String, Object> map = new HashMap<>();
		map.put("type", "invoke");
		map.put("fcn", "deleteLogistic");

		int length = params.size();
        String[] argArray = new String[length];
        for (int i = 0; i < length; i++) {
            argArray[i] = params.get(i);
        }
		
		map.put("array", argArray);
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		return jsonStr;
		
	}
	
}
