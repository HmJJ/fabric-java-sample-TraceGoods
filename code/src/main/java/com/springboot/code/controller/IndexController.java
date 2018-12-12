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
	
	@RequestMapping(value= {"/","","index"})
	public String indexPage() {
		return "index";
	}
	
	@RequestMapping(value= "main")
	public String mainPage() {
		return "/view/main";
	}
	
	@RequestMapping(value= "showGoods")
	public String goodsPage() {
		return "/view/showGoods";
	}
	
	@RequestMapping(value= "addModify")
	public String addModify() {
		return "/view/add_modify_goods";
	}
	
	@RequestMapping(value= "showFabric")
	public String tracePage() {
		return "/view/showFabric";
	}
	
}
