package com.springboot.code.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.springboot.basic.support.CommonRequestAttributes;
import com.springboot.basic.support.CommonResponse;
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
	public CommonResponse add(CommonRequestAttributes attributes, List<String> params) {

		CommonResponse retval = new CommonResponse(false);
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
		
		System.out.println(jsonStr);
		
		retval.setData(jsonStr);
		retval.setMessage("添加成功!");
		retval.setResult(true);
		return retval;
		
	}

	public CommonResponse findAll(CommonRequestAttributes attributes) {

		CommonResponse retval = new CommonResponse(false);
		Map<String, Object> map = new HashMap<>();
		map.put("type", "query");
		map.put("fcn", "queryAllLogistic");
		map.put("array", new String[] {});
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		System.out.println(jsonStr);
		
		retval.setData(jsonStr);
		retval.setMessage("查询成功!");
		retval.setResult(true);
		return retval;
		
	}

	/**
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public CommonResponse findByPage(CommonRequestAttributes attributes, List<String> params) {

		CommonResponse retval = new CommonResponse(false);
		Map<String, Object> map = new HashMap<>();
		map.put("type", "query");
		map.put("fcn", "queryLogisticByPage");

		int length = params.size();
        String[] argArray = new String[length];
        for (int i = 0; i < length; i++) {
            argArray[i] = params.get(i);
        }
		
		map.put("array", argArray);
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		System.out.println(jsonStr);
		
		retval.setData(jsonStr);
		retval.setMessage("分页查找成功!");
		retval.setResult(true);
		return retval;
		
	}
	

	public CommonResponse modify(CommonRequestAttributes attributes, List<String> params) {
		
		CommonResponse retval = new CommonResponse(false);
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
		
		System.out.println(jsonStr);
		
		retval.setData(jsonStr);
		retval.setMessage("修改成功!");
		retval.setResult(true);
		return retval;
		
	}

	/**
	 * @param id
	 * @return
	 */
	public CommonResponse delete(CommonRequestAttributes attributes, List<String> params) {

		CommonResponse retval = new CommonResponse(false);
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
		
		System.out.println(jsonStr);
		
		retval.setData(jsonStr);
		retval.setMessage("删除成功!");
		retval.setResult(true);
		return retval;
		
	}
	
}
