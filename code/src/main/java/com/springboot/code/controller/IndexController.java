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
		
		String jsonStr = goodsService.findAll(attributes);
		
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		
		String txid = jsonObject.getString("txid");
		String preArray = jsonObject.getString("result");
		
		JSONArray resultArry = JSONArray.parseArray(preArray);		
		String goodsArrayPre = resultArry.getString(1);
		
		JSONArray goodsArray = JSONArray.parseArray(goodsArrayPre);
		
		List<Goods> goodsList = new ArrayList<Goods>();
		
		for(int i=0; i<goodsArray.size();i++) {
			JSONObject goodsObject = goodsArray.getJSONObject(i);
			Goods goods = new Goods();
			goods.setId(goodsObject.getString("Id"));
			goods.setName(goodsObject.getString("Name"));
			goods.setPrice(goodsObject.getString("Price"));
			goods.setRegisterDate(goodsObject.getString("RegisterDate"));
			goodsList.add(goods);
		}
		
		model.addAttribute("txid", txid);
		model.addAttribute("goodsInfo", goodsList);
		
		return "/view/showGoods";
	}
	
	@RequestMapping(value= "showFabric")
	public String tracePage() {
		return "/view/showFabric";
	}
	
}
