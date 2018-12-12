package com.springboot.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
* @author nott
* @version 创建时间：2018年12月12日上午11:49:02
* 类说明
*/
@Controller
public class IndexController {
	
	@RequestMapping(value= {"/",""})
	public String index() {
		return "index";
	}
	
	@RequestMapping(value= "GOODS")
	public String goodsPage() {
		return "index";
	}
	
	@RequestMapping(value= "TRACE")
	public String tracePage() {
		return "index";
	}
	
}
