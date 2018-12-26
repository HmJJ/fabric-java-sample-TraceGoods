package com.springboot.code.controller;

import java.text.ParseException;
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
	
	Map<String, Object> map = new HashMap<>();

	@RequestMapping("toAdd")
	public String toAdd(CommonRequestAttributes attributes, Model model) {
		model.addAttribute("goodsId", "");
		model.addAttribute("goods", new Goods());
		model.addAttribute("logistics", new ArrayList<Logistic>());
		return "view/add_modify_goods";
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
		
		String jsonStr = goodsService.add(attributes, params);

		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		
		if (Integer.parseInt(jsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约!");
			retval.setCode("40029");
		} else if (Integer.parseInt(jsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
		} else {
			map.clear();
			map.put("id", params.get(0));
			map.put("goods", params);

			retval.setCode("200");
			retval.setMessage("添加成功!");
			retval.setData(map);
			retval.setResult(true);
		}
		
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
		String jsonStr = goodsService.modify(attributes, params);
		
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		
		if (Integer.parseInt(jsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约!");
			retval.setCode("40029");
		} else if (Integer.parseInt(jsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
		} else {
			map.clear();
			map.put("goods", params);

			retval.setCode("200");
			retval.setMessage("添加成功!");
			retval.setData(map);
			retval.setResult(true);
		}

		return JSON.toJSONString(retval);
	}

	@RequestMapping(value = "findById")
	public String findById(CommonRequestAttributes attributes, Model model, @RequestParam String id) {
		CommonResponse retval = new CommonResponse();

		if (StringUtils.isBlank(id)) {
			retval.setCode("200");
			retval.setMessage("参数为空");
			return "view/add_modify_goods";
		}

		List<String> params = new ArrayList<>();
		JSONObject jsonObject = new JSONObject();
		String jsonStr = "";
		String preArray = "";
		JSONArray resultArry = new JSONArray();
		params.add(id);

		jsonStr = goodsService.findById(attributes, params);

		jsonObject = JSONObject.parseObject(jsonStr);
		
		if (Integer.parseInt(jsonObject.getString("status")) == 40029) {
			model.addAttribute("message", "fabric错误，请检查设置以及智能合约！");
			retval.setCode("200");
		} else if (Integer.parseInt(jsonObject.getString("status")) == 8) {
			model.addAttribute("message", "fabric用户未登陆");
			retval.setCode("200");
		} else {
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

			retval.setCode("200");
			model.addAttribute("logistics", logicticList);
		}

		return "view/add_modify_goods";
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CommonRequestAttributes attributes, @RequestParam(value = "id", required = true) String id) {
		CommonResponse retval = new CommonResponse();
		
		if (StringUtils.isBlank(id)) {
			retval.setMessage("参数为空!");
			retval.setCode("40029");
			retval.setResult(true);
			return JSON.toJSONString(retval);
		}

		List<String> params = new ArrayList<>();
		params.add(id);
		String jsonStr = goodsService.delete(attributes, params);
		
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		
		if (Integer.parseInt(jsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约!");
			retval.setCode("40029");
			retval.setResult(true);
			return JSON.toJSONString(retval);
		} else if (Integer.parseInt(jsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
			retval.setResult(true);
			return JSON.toJSONString(retval);
		} else {
			retval.setMessage("删除成功!");
			retval.setCode("200");
		}
		retval.setResult(true);

		return JSON.toJSONString(retval);
	}
}
