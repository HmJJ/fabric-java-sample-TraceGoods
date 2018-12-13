package com.springboot.code.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.springboot.basic.support.CommonRequestAttributes;
import com.springboot.basic.support.CommonResponse;
import com.springboot.basic.utils.StringUtils;
import com.springboot.basic.utils.Uuid;
import com.springboot.code.entity.Logistic;
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
	public String add(CommonRequestAttributes attributes, @RequestBody Logistic entity) {
		CommonResponse retval = new CommonResponse();
		
		if(StringUtils.isBlank(entity.getGoodsId()) || StringUtils.isBlank(entity.getCityName())) {
			retval.setCode("200");
			retval.setMessage("参数为空");
		}
		
		List<String> params = new ArrayList<>();
		params.add(Uuid.getUUID());
		params.add(entity.getGoodsId());
		params.add(entity.getCityName());
		retval = logisticService.add(attributes, params);
		
		return JSON.toJSONString(retval);
	}
	
	@RequestMapping(value = "modify")
	@ResponseBody
	public String modify(CommonRequestAttributes attributes, @RequestBody Logistic entity) {
		CommonResponse retval = new CommonResponse();
		
		if(StringUtils.isBlank(entity.getGoodsId()) || StringUtils.isBlank(entity.getCityName())) {
			retval.setCode("200");
			retval.setMessage("参数为空");
		}
		
		List<String> params = new ArrayList<>();
		params.add(entity.getId());
		params.add(entity.getGoodsId());
		params.add(entity.getCityName());
		retval = logisticService.modify(attributes, params);
		
		return JSON.toJSONString(retval);
	}
	
	@RequestMapping(value = "findAll")
	@ResponseBody
	public String findAll(CommonRequestAttributes attributes, Model model) {
		CommonResponse retval = new CommonResponse();
		
		retval = logisticService.findAll(attributes);
		
		return JSON.toJSONString(retval);
	}
	
	@RequestMapping(value = "findByPage")
	@ResponseBody
	public String findByPage(CommonRequestAttributes attributes, @RequestParam int pageNum, @RequestParam int pageSize) {
		
		CommonResponse retval = new CommonResponse();
		
		if(String.valueOf(pageNum).equals("") || String.valueOf(pageSize).equals("")) {
			retval.setCode("200");
			retval.setMessage("参数为空");
		}

		List<String> params = new ArrayList<>();
		params.add(String.valueOf(pageNum));
		params.add(String.valueOf(pageSize));
		retval = logisticService.modify(attributes, params);
		
		return JSON.toJSONString(retval);
		
	}
	
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CommonRequestAttributes attributes, String id) {
		
		CommonResponse retval = new CommonResponse();
		
		if(StringUtils.isBlank(id)) {
			retval.setCode("200");
			retval.setMessage("参数为空");
		}

		List<String> params = new ArrayList<>();
		params.add(id);
		retval = logisticService.modify(attributes, params);
		
		return JSON.toJSONString(retval);
		
	}
}
