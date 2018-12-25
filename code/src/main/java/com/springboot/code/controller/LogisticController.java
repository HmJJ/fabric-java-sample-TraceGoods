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
import com.springboot.basic.utils.Uuid;
import com.springboot.code.entity.Goods;
import com.springboot.code.entity.Logistic;
import com.springboot.code.service.GoodsService;
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
	@Autowired private GoodsService goodsService;
	
	Map<String, Object> map = new HashMap<>();
	
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
		String jsonStr = logisticService.add(attributes, params);
		
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		
		if (Integer.parseInt(jsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约!");
			retval.setCode("40029");
		} else if (Integer.parseInt(jsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
		} else {
			map.clear();
			map.put("logistic", params);

			retval.setCode("200");
			retval.setMessage("添加成功!");
			retval.setData(map);
			retval.setResult(true);
		}
		
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
		String jsonStr = logisticService.modify(attributes, params);
		
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		
		if (Integer.parseInt(jsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约!");
			retval.setCode("40029");
		} else if (Integer.parseInt(jsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
		} else {
			map.clear();
			map.put("logistic", params);

			retval.setCode("200");
			retval.setMessage("修改成功!");
			retval.setData(map);
			retval.setResult(true);
		}
		
		return JSON.toJSONString(retval);
	}
	
	@RequestMapping(value = "delete")
	public String delete(CommonRequestAttributes attributes, Model model,
			 @RequestParam(value = "goodsId", required = true) String goodsId,
			 @RequestParam(value = "logisticId", required = true) String logisticId) {
		
		CommonResponse retval = new CommonResponse();
		
		if(StringUtils.isBlank(goodsId) || StringUtils.isBlank(logisticId)) {
			model.addAttribute("message", "参数为空!");
			model.addAttribute("code", "500");
		}

		List<String> params = new ArrayList<>();
		params.add(goodsId);
		params.add(logisticId);
		String jsonStr = logisticService.delete(attributes, params);
		
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		
		if (Integer.parseInt(jsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约!");
			retval.setCode("40029");
			return "view/add_modify_goods";
		} else if (Integer.parseInt(jsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
			return "view/add_modify_goods";
		} else {
			model.addAttribute("message", "删除成功!");
			model.addAttribute("code", "200");
		}
		
		params.clear();
		jsonObject = new JSONObject();
		params.add(goodsId);
		
		String preArray = "";
		JSONArray resultArry = new JSONArray();

		jsonStr = goodsService.findById(attributes, params);

		jsonObject = JSONObject.parseObject(jsonStr);
		
		preArray = jsonObject.getString("result");

		resultArry = JSONArray.parseArray(preArray);
		String goodsJson = resultArry.getString(1);
		JSONObject goodsObject = JSONObject.parseObject(goodsJson);

		Goods goods = new Goods();
		goods.setId(goodsObject.getString("Id"));
		goods.setName(goodsObject.getString("Name"));
		goods.setPrice(goodsObject.getString("Price"));
		goods.setRegisterDate(goodsObject.getString("RegisterDate").replace("/", "-"));

		model.addAttribute("goods", goods);
		model.addAttribute("goodsId", goodsId);

		jsonStr = goodsService.findLogisticById(attributes, params);

		jsonObject = JSONObject.parseObject(jsonStr);

		preArray = jsonObject.getString("result");

		resultArry = JSONArray.parseArray(preArray);
		String logisticArrayPre = resultArry.getString(1);

		JSONArray logicticArray = JSONArray.parseArray(logisticArrayPre);

		List<Logistic> logicticList = new ArrayList<Logistic>();

		for (int i = 0; i < logicticArray.size(); i++) {
			JSONObject logicticObject = logicticArray.getJSONObject(i);
			Logistic logictic = new Logistic();
			logictic.setId(logicticObject.getString("Id"));
			logictic.setGoodsId(logicticObject.getString("GoodsId"));
			logictic.setCityName(logicticObject.getString("CityName"));
			logictic.setSort(Integer.parseInt(logicticObject.getString("Sort")));
			logicticList.add(logictic);
		}

		for (int i = 0; i < logicticList.size() - 1; i++) {
			for (int j = 0; j < logicticList.size() - 1 - i; j++) {
				if (logicticList.get(j).getSort() > logicticList.get(j + 1).getSort()) {
					Logistic temp = logicticList.get(j);
					logicticList.set(j, logicticList.get(j + 1));
					logicticList.set(j + 1, temp);
				}
			}
		}
		model.addAttribute("logistics", logicticList);
		
		return "view/add_modify_goods";
		
	}
}
