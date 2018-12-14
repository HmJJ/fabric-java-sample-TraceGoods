package com.springboot.code.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springboot.basic.support.CommonRequestAttributes;
import com.springboot.code.entity.Goods;
import com.springboot.code.service.GoodsService;

/**
* @author nott
* @version 创建时间：2018年12月12日上午11:49:02
* 类说明
*/
@Controller
public class IndexController {
	
	@Autowired private GoodsService goodsService;
	
	@RequestMapping(value= {"/","","index"})
	public String indexPage() {
		return "index";
	}
	
	@RequestMapping(value= "main")
	public String mainPage() {
		return "/view/main";
	}
	
	@RequestMapping(value= "showGoods")
	public String goodsPage(CommonRequestAttributes attributes, Model model) {
		
		JSONObject result = goodsService.findAll(attributes);
		
		String txid = result.getString("txid");
		
		JSONArray resultArry = JSONArray.parseArray(result.getString("result"));		
//		JSONObject goodsObj = resultArry.getJSONObject(1);
		
//		JSONArray goodsArry = JSONArray.parseArray(goodsObj.toJSONString());

		List<Goods> goodsList = new ArrayList<Goods>();
		
//		for (int i=0;i<goodsArry.size();i++) {
//			JSONObject obj = goodsArry.getJSONObject(i);
//			Goods entity = new Goods();
//			entity.setId(obj.getString("Id"));
//			entity.setName(obj.getString("Name"));
//			entity.setPrice(obj.getString("Price"));
//			entity.setRegisterDate(obj.getString("RegisterDate"));
//			goodsList.add(entity);
//		}
		
		model.addAttribute("txid", txid);
		model.addAttribute("goodsInfo", goodsList);
		
		return "/view/showGoods";
	}
	
	@RequestMapping(value= "showFabric")
	public String tracePage() {
		return "/view/showFabric";
	}
	
}
