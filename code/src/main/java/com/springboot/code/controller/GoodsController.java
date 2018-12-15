package com.springboot.code.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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
import com.springboot.basic.utils.time.DateUtils;
import com.springboot.code.entity.Goods;
import com.springboot.code.entity.Logistic;
import com.springboot.code.service.GoodsService;

/**
 * @author nott
 * @version 创建时间：2018年12月12日上午10:56:15 类说明
 */

@Controller
@RequestMapping("goods")
public class GoodsController {

	@Autowired
	private GoodsService goodsService;

	@RequestMapping("toAdd")
	public String toAdd(CommonRequestAttributes attributes, Model model) {
		model.addAttribute("goodsId", "");
		model.addAttribute("goods", new Goods());
		model.addAttribute("logistics", new ArrayList<Logistic>());
		return "/view/add_modify_goods";
	}

	@RequestMapping("add")
	@ResponseBody
	public String add(CommonRequestAttributes attributes, @RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "name") String name, @RequestParam(value = "price") String price,
			@RequestParam(value = "registerDate") String registerDate) {
		CommonResponse retval = new CommonResponse();

		if (StringUtils.isBlank(name) || StringUtils.isBlank(price) || StringUtils.isBlank(registerDate)) {
			retval.setCode("200");
			retval.setMessage("参数为空");
		}

		List<String> params = new ArrayList<>();
		if (StringUtils.isBlank(id)) {
			params.add(Uuid.getUUID());
		}
		params.add(name);
		params.add(price);
		try {
			registerDate = DateUtils.format(registerDate, "yyyy/MM/dd");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params.add(registerDate);
		retval = goodsService.add(attributes, params);

		return JSON.toJSONString(retval);
	}

	@RequestMapping(value = "modify")
	@ResponseBody
	public String modify(CommonRequestAttributes attributes, @RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "name") String name, @RequestParam(value = "price") String price,
			@RequestParam(value = "registerDate") String registerDate) {
		CommonResponse retval = new CommonResponse();

		if (StringUtils.isBlank(id) || StringUtils.isBlank(name) || StringUtils.isBlank(price)
				|| StringUtils.isBlank(registerDate)) {
			retval.setCode("500");
			retval.setMessage("参数为空");
		}

		List<String> params = new ArrayList<>();
		params.add(id);
		params.add(name);
		params.add(price);
		params.add(registerDate);
		retval = goodsService.modify(attributes, params);

		return JSON.toJSONString(retval);
	}

	@RequestMapping(value = "findById")
	public String findById(CommonRequestAttributes attributes, Model model, @RequestParam String id) {
		CommonResponse retval = new CommonResponse();

		if (StringUtils.isBlank(id)) {
			retval.setCode("200");
			retval.setMessage("参数为空");
		}

		List<String> params = new ArrayList<>();
		JSONObject jsonObject = new JSONObject();
		String jsonStr = "";
		String preArray = "";
		JSONArray resultArry = new JSONArray();
		params.add(id);

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
		model.addAttribute("goodsId", id);

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

	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CommonRequestAttributes attributes, String id) {

		CommonResponse retval = new CommonResponse();

		if (StringUtils.isBlank(id)) {
			retval.setCode("200");
			retval.setMessage("参数为空");
		}

		List<String> params = new ArrayList<>();
		params.add(id);
		retval = goodsService.modify(attributes, params);

		return JSON.toJSONString(retval);

	}
}
