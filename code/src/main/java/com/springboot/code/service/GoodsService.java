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


/**
 * @author nott
 * @version 创建时间：2018年12月12日上午11:21:16 类说明
 */
@Service(value = "goodsService")
public class GoodsService {
	
	@Autowired private SimpleService simpleService;

	public CommonResponse add(CommonRequestAttributes attributes, List<String> params) {
		
		CommonResponse retval = new CommonResponse(false);
		Map<String, Object> map = new HashMap<>();
		map.put("type", "invoke");
		map.put("fcn", "addGoods");

		int length = params.size();
        String[] argArray = new String[length];
        for (int i = 0; i < length; i++) {
            argArray[i] = params.get(i);
        }
		
		map.put("array", argArray);
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		JSONObject result = JSONObject.parseObject(jsonStr);
		
		if ((int)result.get("status") != 200) {
			retval.setMessage("添加失败!");
		} else {
			retval.setMessage("添加成功!");
		}
		
		System.out.println(result);
		
		retval.setData(result);
		
		retval.setResult(true);
		return retval;
		
	}

	public JSONObject findAll(CommonRequestAttributes attributes) {

		Map<String, Object> map = new HashMap<>();
		map.put("type", "query");
		map.put("fcn", "queryAllGoods");
		map.put("array", new String[] {});
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		JSONObject result = JSONObject.parseObject(jsonStr);
		
		return result;
		
	}
	
	public CommonResponse findLogisticById(CommonRequestAttributes attributes, List<String> params) {

		CommonResponse retval = new CommonResponse(false);
		Map<String, Object> map = new HashMap<>();
		map.put("type", "query");
		map.put("fcn", "queryLogisticByGoodsId");
		
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
		retval.setMessage("查询成功!");
		retval.setResult(true);
		
		return retval;
		
	}

	public CommonResponse findByPage(CommonRequestAttributes attributes, List<String> params) {

		CommonResponse retval = new CommonResponse(false);
		Map<String, Object> map = new HashMap<>();
		map.put("type", "query");
		map.put("fcn", "queryGoodsByPage");

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
		retval.setMessage("分页查询成功!");
		retval.setResult(true);
		
		return retval;
		
	}

	public CommonResponse modify(CommonRequestAttributes attributes, List<String> params) {
		
		CommonResponse retval = new CommonResponse(false);
		Map<String, Object> map = new HashMap<>();
		map.put("type", "invoke");
		map.put("fcn", "modifyGoods");

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

	public CommonResponse delete(CommonRequestAttributes attributes, List<String> params) {

		CommonResponse retval = new CommonResponse(false);
		Map<String, Object> map = new HashMap<>();
		map.put("type", "invoke");
		map.put("fcn", "deleteGoods");

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
