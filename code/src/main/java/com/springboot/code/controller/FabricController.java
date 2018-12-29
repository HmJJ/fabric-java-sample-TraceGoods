package com.springboot.code.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.springboot.basic.support.CommonRequestAttributes;
import com.springboot.basic.support.CommonResponse;
import com.springboot.basic.utils.StringUtils;
import com.springboot.code.service.GoodsService;

@Controller
@RequestMapping("fabric")
public class FabricController {
	
	@Autowired
	private GoodsService goodsService;
	
	@RequestMapping(value = "findById")
	@ResponseBody
	public String findById(CommonRequestAttributes attributes, Model model, @RequestParam String id) {
		CommonResponse retval = new CommonResponse();

		if (StringUtils.isBlank(id)) {
			retval.setCode("500");
			retval.setMessage("参数为空");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}

		List<String> params = new ArrayList<>();
		JSONObject prejsonObject = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		String jsonStr = "";
		String result = "";
		Boolean status = false;
		String message = "";
		String data = "";
		params.add(id);

		jsonStr = goodsService.findById(attributes, params);

		prejsonObject = JSONObject.parseObject(jsonStr);

		if (Integer.parseInt(prejsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约！");
			retval.setCode("200");
		} else if (Integer.parseInt(prejsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("200");
		} else {
			result = prejsonObject.getString("result");
			jsonObject = JSONObject.parseObject(result);
			status = jsonObject.getBoolean("Status");
			message = jsonObject.getString("Message");
			data = jsonObject.getString("Data");
			
//			JSONArray resultArry = JSONArray.parseArray(result);
//			String goodsJson = resultArry.getString(1);
			
			Map<String, Object> map = new HashMap<>();

			map.put("status", status);
			map.put("message", message);
			map.put("result", data);
			
			retval.setData(map);
			retval.setMessage("查询成功!");
			retval.setCode("200");
		}
		
		retval.setResult(true);
		
		return JSON.toJSONString(retval);
	}
}
