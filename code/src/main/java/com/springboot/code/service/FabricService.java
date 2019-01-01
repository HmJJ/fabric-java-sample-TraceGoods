package com.springboot.code.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.springboot.basic.support.CommonRequestAttributes;
import com.springboot.fabric.interactive.service.SimpleService;

@Service(value = "fabricService")
public class FabricService {
	
	@Autowired private SimpleService simpleService;
	
	Map<String, Object> map = new HashMap<>();
	
	public String findByKey(String key) {

		Map<String, Object> map = new HashMap<>();
		map.put("type", "query");
		map.put("fcn", "query");
		
        String[] argArray = {key};
		
		map.put("array", argArray);
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		return jsonStr;
	}
	
	public String findByKeyWithLevel(CommonRequestAttributes attributes, List<String> params) {

		Map<String, Object> map = new HashMap<>();
		map.put("type", "query");
		map.put("fcn", "queryWithLevel");
		
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
	
	public Integer getLevel(String type) {
		
		int level = 0;
		
		String jsonStr = findByKey(type);
		
		JSONObject prejsonObject = JSONObject.parseObject(jsonStr);

		if (Integer.parseInt(prejsonObject.getString("status")) == 40029) {
			level = 0;
		} else if (Integer.parseInt(prejsonObject.getString("status")) == 8) {
			level = 0;
		} else {
			String result = prejsonObject.getString("result");
			JSONObject jsonObject = JSONObject.parseObject(result);
			String data = jsonObject.getString("Data");
			
			JSONObject QX = JSONObject.parseObject(data);
			
			level = QX.getIntValue("Level");
		}
		
		return level;
	}
	
}
