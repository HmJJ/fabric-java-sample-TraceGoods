package com.springboot.code.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import com.springboot.code.entity.User;
import com.springboot.code.service.GoodsService;
import com.springboot.code.service.UserService;

/**
 * @author nott
 * @version 创建时间：2018年12月12日上午10:56:15 类说明
 */

@Controller
@RequestMapping("goods")
public class GoodsController {

	@Autowired private GoodsService goodsService;
	@Autowired private UserService userService;
	
	Map<String, Object> map = new HashMap<>();

	/**
	 * 跳转到add_modify页面
	 * @param attributes
	 * @param model
	 * @return
	 */
	@RequestMapping("toAdd")
	public String toAdd(CommonRequestAttributes attributes, Model model) {
		
		HttpSession session = attributes.getRequest().getSession();
		User user = (User)session.getAttribute("user");
		if(user == null) {
			return "redirect:index";
		}
		
		model.addAttribute("goodsId", "");
		model.addAttribute("goods", new Goods());
		model.addAttribute("logistics", new ArrayList<Logistic>());
		model.addAttribute("user", user);
		
		int usertype = userService.getUserType(user);

		model.addAttribute("usertype", usertype);
		
		return "view/add_modify_goods";
	}

	/**
	 * 添加商品
	 * @param attributes
	 * @param id
	 * @param name
	 * @param price
	 * @param createDate
	 * @return
	 */
	@RequestMapping("add")
	@ResponseBody
	public String add(CommonRequestAttributes attributes, @RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "name") String name, @RequestParam(value = "price") String price,
			@RequestParam(value = "createDate") String createDate) {
		
		CommonResponse retval = new CommonResponse();

		HttpSession session = attributes.getRequest().getSession();
		User user = (User)session.getAttribute("user");
		if(user == null) {
			retval.setCode("406");
			retval.setMessage("请登录");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}

		if (StringUtils.isBlank(name) || StringUtils.isBlank(price) || StringUtils.isBlank(createDate)) {
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
			createDate = DateUtils.format(createDate, "yyyy/MM/dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		params.add(createDate);
		params.add(user.getName());
		
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
			
			String result = jsonObject.getString("result");
			jsonObject = JSONObject.parseObject(result);
			Boolean status = jsonObject.getBoolean("Status");
			String message = jsonObject.getString("Message");
			String data = jsonObject.getString("Data");
			jsonObject = JSONObject.parseObject(data);
			
			if(status) {
				retval.setData(jsonObject);
				retval.setMessage(message);
				retval.setCode("200");
				retval.setResult(true);
			} else {
				retval.setMessage(message);
				retval.setCode("200");
				retval.setResult(false);
			}
		}
		
		return JSON.toJSONString(retval);
	}

	/**
	 * 修改商品
	 * @param attributes
	 * @param id
	 * @param name
	 * @param price
	 * @param createDate
	 * @return
	 */
	@RequestMapping(value = "modify")
	@ResponseBody
	public String modify(CommonRequestAttributes attributes, @RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "name") String name, @RequestParam(value = "price") String price,
			@RequestParam(value = "createDate") String createDate) {
		
		CommonResponse retval = new CommonResponse();
		
		HttpSession session = attributes.getRequest().getSession();
		User user = (User)session.getAttribute("user");
		if(user == null) {
			retval.setCode("406");
			retval.setMessage("请登录");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}

		if (StringUtils.isBlank(id) || StringUtils.isBlank(name) || StringUtils.isBlank(price)
				|| StringUtils.isBlank(createDate)) {
			retval.setCode("500");
			retval.setMessage("参数为空");
		}

		List<String> params = new ArrayList<>();
		params.add(id);
		params.add(name);
		params.add(price);
		Date date = new Date();
		String modifyDate = "";
		modifyDate = DateUtils.format(date, "yyyy/MM/dd");
		params.add(modifyDate);
		params.add(user.getName());
		
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

			String result = jsonObject.getString("result");
			jsonObject = JSONObject.parseObject(result);
			Boolean status = jsonObject.getBoolean("Status");
			String message = jsonObject.getString("Message");
			String data = jsonObject.getString("Data");
			
			if(status) {
				retval.setData(data);
				retval.setMessage(message);
				retval.setCode("200");
				retval.setResult(true);
			} else {
				retval.setMessage(message);
				retval.setCode("200");
				retval.setResult(false);
			}
		}

		return JSON.toJSONString(retval);
	}

	/**
	 * 分页查看商品
	 * @param attributes
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="findByPage")
	public String findByPage(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {

		HttpSession session = attributes.getRequest().getSession();
		User user = (User)session.getAttribute("user");
		if(user == null) {
			return "redirect:index";
		}

		if (pageNum == null || pageSize == null) {
			model.addAttribute("message", "参数不能为空!");
			model.addAttribute("code", "500");
		} else {
			List<String> params = new ArrayList<>();
			
			params.add(user.getName());
			params.add(String.valueOf(pageNum));
			params.add(String.valueOf(pageSize));
			
			String jsonStr = goodsService.findAll(attributes, params);
			
			JSONObject prejsonObject = JSONObject.parseObject(jsonStr);
			
			if (Integer.parseInt(prejsonObject.getString("status")) == 40029) {
				model.addAttribute("message", "fabric错误，请检查设置以及智能合约");
				model.addAttribute("code", "40029");
			} else if (Integer.parseInt(prejsonObject.getString("status")) == 8) {
				model.addAttribute("message", "fabric用户未登陆");
				model.addAttribute("code", "8");
			} else {
				String txid = prejsonObject.getString("txid");
				String preresult = prejsonObject.getString("result");
				
				JSONObject result = JSONObject.parseObject(preresult);
				
				String preArry = result.getString("Data");
				int num = result.getIntValue("PageNum");
				int size = result.getIntValue("PageSize");
				int total = result.getIntValue("Total");
				
				JSONArray resultArry = JSONArray.parseArray(preArry);
				
				List<Goods> goodsList = new ArrayList<Goods>();
				
				for(int i=0; i<resultArry.size();i++) {
					JSONObject goodsObject = resultArry.getJSONObject(i);
					Goods goods = new Goods();
					goods.setId(goodsObject.getString("Id"));
					goods.setName(goodsObject.getString("Name"));
					goods.setPrice(goodsObject.getString("Price"));
					goods.setCreateDate(goodsObject.getString("CreateDate"));
					goods.setModifyDate(goodsObject.getString("ModifyDate"));
					goods.setSort(goodsObject.getInteger("Sort"));
					goodsList.add(goods);
				}
				
				model.addAttribute("txid", txid);
				model.addAttribute("goodsInfo", goodsList);
				model.addAttribute("message", "查询成功!");
				model.addAttribute("code", "200");
				model.addAttribute("pageNum", num);
				model.addAttribute("pageSize", size);
				model.addAttribute("total", total);
			}
		}
		
		int usertype = userService.getUserType(user);

		model.addAttribute("usertype", usertype);
		
		model.addAttribute("user", user);
		
		return "view/showGoods";
	}
	
	/**
	 * 根据商品id查看商品详情
	 * @param attributes
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "findById")
	public String findById(CommonRequestAttributes attributes, Model model, @RequestParam String id) {
		
		HttpSession session = attributes.getRequest().getSession();
		User user = (User)session.getAttribute("user");
		if(user == null) {
			return "redirect:/";
		}
		
		CommonResponse retval = new CommonResponse();

		if (StringUtils.isBlank(id)) {
			retval.setCode("200");
			retval.setMessage("参数为空");
			retval.setResult(false);
			return "view/add_modify_goods";
		}

		List<String> params = new ArrayList<>();
		JSONObject jsonObject = new JSONObject();
		JSONObject result = new JSONObject();
		String jsonStr = "";
		String preresult = "";
		params.add(id);
		params.add(user.getName());

		jsonStr = goodsService.findById(attributes, params);

		jsonObject = JSONObject.parseObject(jsonStr);
		
		if (Integer.parseInt(jsonObject.getString("status")) == 40029) {
			model.addAttribute("message", "fabric错误，请检查设置以及智能合约！");
			retval.setCode("40029");
			retval.setResult(false);
		} else if (Integer.parseInt(jsonObject.getString("status")) == 8) {
			model.addAttribute("message", "fabric用户未登陆");
			retval.setCode("8");
			retval.setResult(false);
		} else {
			preresult = jsonObject.getString("result");
			
			result = JSONObject.parseObject(preresult);
			
			Boolean status = result.getBoolean("Status");
			String message = result.getString("Message");
			String data = result.getString("Data");
			
			if(status) {
				JSONObject goodsObject = JSONObject.parseObject(data);

				Goods goods = new Goods();
				goods.setId(goodsObject.getString("Id"));
				goods.setName(goodsObject.getString("Name"));
				goods.setPrice(goodsObject.getString("Price"));
				goods.setCreateDate(goodsObject.getString("CreateDate").replace("/", "-"));
				goods.setModifyDate(goodsObject.getString("ModifyDate").replace("/", "-"));
				goods.setSort(goodsObject.getInteger("Sort"));

				model.addAttribute("goods", goods);
				model.addAttribute("goodsId", id);

				jsonStr = goodsService.findLogisticById(attributes, params);

				jsonObject = JSONObject.parseObject(jsonStr);

				preresult = jsonObject.getString("result");
				
				result = JSONObject.parseObject(preresult);

				String logisticArrayPre = result.getString("Data");

				JSONArray logicticArray = JSONArray.parseArray(logisticArrayPre);

				List<Logistic> logicticList = new ArrayList<Logistic>();

				for (int i = 0; i < logicticArray.size(); i++) {
					JSONObject logicticObject = logicticArray.getJSONObject(i);
					Logistic logictic = new Logistic();
					logictic.setId(logicticObject.getString("Id"));
					logictic.setGoodsId(logicticObject.getString("GoodsId"));
					logictic.setCityName(logicticObject.getString("CityName"));
					logictic.setCreateDate(goodsObject.getString("CreateDate").replace("/", "-"));
					logictic.setModifyDate(goodsObject.getString("ModifyDate").replace("/", "-"));
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
				retval.setResult(true);
				model.addAttribute("logistics", logicticList);
			} else {
				retval.setCode("500");
				retval.setResult(false);
			}
			retval.setData(data);
			retval.setMessage(message);
		}
		
		model.addAttribute("user", user);
		
		int usertype = userService.getUserType(user);

		model.addAttribute("usertype", usertype);

		return "view/add_modify_goods";
	}

	/**
	 * 删除商品信息
	 * @param attributes
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CommonRequestAttributes attributes, @RequestParam(value = "id", required = true) String id) {
		CommonResponse retval = new CommonResponse();
		
		HttpSession session = attributes.getRequest().getSession();
		User user = (User)session.getAttribute("user");
		if(user == null) {
			retval.setCode("406");
			retval.setMessage("请登录");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}
		
		if (StringUtils.isBlank(id)) {
			retval.setMessage("参数为空!");
			retval.setCode("40029");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}

		List<String> params = new ArrayList<>();
		params.add(id);
		params.add(user.getName());
		String jsonStr = goodsService.delete(attributes, params);
		
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		
		if (Integer.parseInt(jsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约!");
			retval.setCode("40029");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		} else if (Integer.parseInt(jsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		} else {
			String result = jsonObject.getString("result");
			jsonObject = JSONObject.parseObject(result);
			Boolean status = jsonObject.getBoolean("Status");
			String message = jsonObject.getString("Message");
			String data = jsonObject.getString("Data");
			
			retval.setData(data);
			retval.setMessage(message);
			if(status) {
				retval.setCode("200");
				retval.setResult(true);
			} else {
				retval.setCode("500");
				retval.setResult(false);
			}
		}
		retval.setResult(true);

		return JSON.toJSONString(retval);
	}
}
