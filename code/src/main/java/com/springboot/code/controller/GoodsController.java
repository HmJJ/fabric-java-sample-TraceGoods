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
import com.springboot.code.service.GoodsService;

/**
* @author nott
* @version 创建时间：2018年12月12日上午10:56:15
* 类说明
*/

@Controller
@RequestMapping("goods")
public class GoodsController {
	
	@Autowired private GoodsService goodsService;
	
	@RequestMapping(value = "add")
	@ResponseBody
	public String add(CommonRequestAttributes attributes, @RequestParam String id, @RequestParam String name, @RequestParam String price, @RequestParam String registerDate) {
		CommonResponse retval = new CommonResponse();
		
		if(StringUtils.isBlank(name) || StringUtils.isBlank(price) 
				|| StringUtils.isBlank(registerDate)) {
			retval.setCode("200");
			retval.setMessage("参数为空");
		}
		
		List<String> params = new ArrayList<>();
		if(StringUtils.isBlank(id)) {			
			params.add(Uuid.getUUID());
		}
		params.add(name);
		params.add(price);
		params.add(registerDate);
		retval = goodsService.add(attributes, params);
		
		return JSON.toJSONString(retval);
	}
	
	@RequestMapping(value = "modify")
	@ResponseBody
	public String modify(CommonRequestAttributes attributes, @RequestParam String id, @RequestParam String name, @RequestParam String price, @RequestParam String registerDate) {
		CommonResponse retval = new CommonResponse();
		
		if(StringUtils.isBlank(id) || StringUtils.isBlank(name) || StringUtils.isBlank(price) 
				|| StringUtils.isBlank(registerDate)) {
			retval.setCode("200");
			retval.setMessage("参数为空");
		}
		
		List<String> params = new ArrayList<>();
		params.add(Uuid.getUUID());
		params.add(name);
		params.add(price);
		params.add(registerDate);
		retval = goodsService.modify(attributes, params);
		
		return JSON.toJSONString(retval);
	}
	
	@RequestMapping(value = "findAll")
	@ResponseBody
	public String findAll(CommonRequestAttributes attributes) {
		CommonResponse retval = new CommonResponse();
		
		retval = goodsService.findAll(attributes);
		
		return JSON.toJSONString(retval);
	}
	
	@RequestMapping(value = "findById")
	@ResponseBody
	public String findById(CommonRequestAttributes attributes, @RequestParam String id) {
		CommonResponse retval = new CommonResponse();
		
		if(StringUtils.isBlank(id)) {
			retval.setCode("200");
			retval.setMessage("参数为空");
		}
		
		List<String> params = new ArrayList<>();
		params.add(id);
		retval = goodsService.modify(attributes, params);
		
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
		retval = goodsService.modify(attributes, params);
		
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
		retval = goodsService.modify(attributes, params);
		
		return JSON.toJSONString(retval);
		
	}
}
