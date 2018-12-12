package com.springboot.code.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
* @author nott
* @version 创建时间：2018年12月12日上午10:56:15
* 类说明
*/

@Controller
@RequestMapping("goods")
public class GoodsController {
	
	@RequestMapping(value = "add")
	@ResponseBody
	public String add(@RequestBody Map<String, Object> map) {
		return null;
	}
	
	@RequestMapping(value = "find")
	@ResponseBody
	public String find(@RequestParam String id) {
		return null;
	}
	
	@RequestMapping(value = "modify")
	@ResponseBody
	public String modify(@RequestBody Map<String, Object> map) {
		return null;
	}
	
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(String id) {
		return null;
	}
}
