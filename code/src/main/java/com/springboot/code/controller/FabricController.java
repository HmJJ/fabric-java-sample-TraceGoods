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
import com.alibaba.fastjson.JSONArray;
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
		JSONObject jsonObject = new JSONObject();
		String jsonStr = "";
		params.add(id);

		jsonStr = goodsService.findById(attributes, params);

		jsonObject = JSONObject.parseObject(jsonStr);

		String result = jsonObject.getString("result");
		
		JSONArray resultArry = JSONArray.parseArray(result);
		String goodsJson = resultArry.getString(1);
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("result", goodsJson);
		
		retval.setData(map);
		retval.setResult(true);
		
		return JSON.toJSONString(retval);
	}
}
