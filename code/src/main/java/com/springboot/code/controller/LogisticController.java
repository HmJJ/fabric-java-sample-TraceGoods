package com.springboot.code.controller;

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
import com.alibaba.fastjson.JSONObject;
import com.springboot.basic.support.CommonRequestAttributes;
import com.springboot.basic.support.CommonResponse;
import com.springboot.basic.utils.StringUtils;
import com.springboot.basic.utils.Uuid;
import com.springboot.basic.utils.time.DateUtils;
import com.springboot.code.entity.User;
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
	
	Map<String, Object> map = new HashMap<>();
	
	/**
	 * 添加物流信息
	 * @param attributes
	 * @param id
	 * @param goodsId
	 * @param cityName
	 * @return
	 */
	@RequestMapping(value = "add")
	@ResponseBody
	public String add(CommonRequestAttributes attributes,
			@RequestParam(value="id", required=false) String id,
			@RequestParam(value="goodsId") String goodsId,
			@RequestParam(value="cityName") String cityName) {
		CommonResponse retval = new CommonResponse();
		
		HttpSession session = attributes.getRequest().getSession();
		User user = (User)session.getAttribute("user");
		if(user == null) {
			retval.setCode("406");
			retval.setMessage("请登录");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}
		
		if(StringUtils.isBlank(goodsId) || StringUtils.isBlank(cityName)) {
			retval.setCode("500");
			retval.setResult(false);
			retval.setMessage("参数为空");
		}
		List<String> params = new ArrayList<>();
		JSONObject jsonObject = new JSONObject();
		String jsonStr = "";
		
		params.clear();
		params.add(Uuid.getUUID());
		params.add(goodsId);
		params.add(cityName);
		Date date = new Date();
		String createDate = "";
		createDate = DateUtils.format(date, "yyyy/MM/dd");
		params.add(createDate);
		params.add(user.getName());
		
		jsonStr = logisticService.add(attributes, params);
		
		jsonObject = JSONObject.parseObject(jsonStr);
		
		if (Integer.parseInt(jsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约!");
			retval.setCode("40029");
			retval.setResult(false);
		} else if (Integer.parseInt(jsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
			retval.setResult(false);
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
	
	/**
	 * 修改物流信息
	 * @param attributes
	 * @param id
	 * @param goodsId
	 * @param cityName
	 * @param sort
	 * @return
	 */
	@RequestMapping(value = "modify")
	@ResponseBody
	public String modify(CommonRequestAttributes attributes,
			@RequestParam(value="id", required=false) String id,
			@RequestParam(value="goodsId") String goodsId,
			@RequestParam(value="cityName") String cityName,
			@RequestParam(value="sort") String sort) {
		CommonResponse retval = new CommonResponse();
		
		HttpSession session = attributes.getRequest().getSession();
		User user = (User)session.getAttribute("user");
		if(user == null) {
			retval.setCode("406");
			retval.setMessage("请登录");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}
		
		if(StringUtils.isBlank(id)
				|| StringUtils.isBlank(goodsId)
				|| StringUtils.isBlank(cityName)
				|| StringUtils.isBlank(sort)) {
			retval.setCode("500");
			retval.setMessage("参数为空");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}
		
		List<String> params = new ArrayList<>();
		params.add(id);
		params.add(goodsId);
		params.add(cityName);
		params.add(user.getName());
		Date date = new Date();
		String modifyDate = "";
		modifyDate = DateUtils.format(date, "yyyy/MM/dd");
		params.add(modifyDate);
		
		String jsonStr = logisticService.modify(attributes, params);
		
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		
		if (Integer.parseInt(jsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约!");
			retval.setCode("40029");
			retval.setResult(false);
		} else if (Integer.parseInt(jsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
			retval.setResult(false);
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
	
	/**
	 * 删除物流信息
	 * @param attributes
	 * @param model
	 * @param goodsId
	 * @param logisticId
	 * @return
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CommonRequestAttributes attributes, Model model,
			 @RequestParam(value = "goodsId", required = true) String goodsId,
			 @RequestParam(value = "logisticId", required = true) String logisticId) {
		
		CommonResponse retval = new CommonResponse();
		
		HttpSession session = attributes.getRequest().getSession();
		User user = (User)session.getAttribute("user");
		if(user == null) {
			retval.setCode("406");
			retval.setMessage("请登录");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}
		
		if(StringUtils.isBlank(goodsId) || StringUtils.isBlank(logisticId)) {
			retval.setCode("500");
			retval.setMessage("参数为空!");
			retval.setResult(false);
		}

		List<String> params = new ArrayList<>();
		params.add(logisticId);
		params.add(goodsId);
		params.add(user.getName());
		
		String jsonStr = logisticService.delete(attributes, params);
		
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		
		if (Integer.parseInt(jsonObject.getString("status")) == 40029) {
			retval.setCode("40029");
			retval.setMessage("fabric错误，请检查设置以及智能合约!");
			retval.setResult(false);
		} else if (Integer.parseInt(jsonObject.getString("status")) == 8) {
			retval.setCode("8");
			retval.setMessage("fabric用户未登陆!");
			retval.setResult(false);
		} else {
			retval.setCode("200");
			retval.setMessage("删除成功!");
			retval.setResult(true);
		}
		
		return JSON.toJSONString(retval);
	}
}
