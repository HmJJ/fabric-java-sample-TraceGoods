package com.springboot.code.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.springboot.basic.support.CommonRequestAttributes;
import com.springboot.basic.support.CommonResponse;
import com.springboot.basic.utils.StringUtils;
import com.springboot.basic.utils.Uuid;
import com.springboot.code.service.LogisticService;

/**
* @author nott
* @version 创建时间：2018年12月12日上午10:56:15
* 类说明
*/

@Controller
@RequestMapping("logistic")
public class LogisticController {
	
	@Autowired private LogisticService logisticService;
	
	@RequestMapping(value = "add")
	@ResponseBody
	public String add(CommonRequestAttributes attributes,
			@RequestParam(value="id", required=false) String id,
			@RequestParam(value="goodsId") String goodsId,
			@RequestParam(value="cityName") String cityName) {
		CommonResponse retval = new CommonResponse();
		
		if(StringUtils.isBlank(goodsId) || StringUtils.isBlank(cityName)) {
			retval.setCode("500");
			retval.setMessage("参数为空");
		}
		
		List<String> params = new ArrayList<>();
		params.add(Uuid.getUUID());
		params.add(goodsId);
		params.add(cityName);
		retval = logisticService.add(attributes, params);
		
		return JSON.toJSONString(retval);
	}
	
	@RequestMapping(value = "modify")
	@ResponseBody
	public String modify(CommonRequestAttributes attributes,
			@RequestParam(value="id", required=false) String id,
			@RequestParam(value="goodsId") String goodsId,
			@RequestParam(value="cityName") String cityName) {
		CommonResponse retval = new CommonResponse();
		
		if(StringUtils.isBlank(id)
				|| StringUtils.isBlank(goodsId)
				|| StringUtils.isBlank(cityName)) {
			retval.setCode("500");
			retval.setMessage("参数为空");
		}
		
		List<String> params = new ArrayList<>();
		params.add(id);
		params.add(goodsId);
		params.add(cityName);
		retval = logisticService.modify(attributes, params);
		
		return JSON.toJSONString(retval);
	}
	
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CommonRequestAttributes attributes, String id) {
		
		CommonResponse retval = new CommonResponse();
		
		if(StringUtils.isBlank(id)) {
			retval.setCode("500");
			retval.setMessage("参数为空");
		}

		List<String> params = new ArrayList<>();
		params.add(id);
		retval = logisticService.modify(attributes, params);
		
		return JSON.toJSONString(retval);
		
	}
}
