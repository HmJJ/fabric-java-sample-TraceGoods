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
import com.springboot.basic.utils.time.DateUtils;
import com.springboot.code.entity.User;
import com.springboot.code.service.UserService;

/**
* @author nott
* @version 创建时间：2018年12月29日下午1:44:13
* 类说明
*/
@Controller
@RequestMapping("user")
public class UserController {
	
	@Autowired private UserService userService;
	Map<String, Object> map = new HashMap<>();
	
	/**
	 * 用户登录
	 * @param attributes
	 * @param model
	 * @param name
	 * @param password
	 * @return
	 */
	@RequestMapping(value= "login")
	@ResponseBody
	public String login(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "password", required = false) String password) {
		
		CommonResponse retval = new CommonResponse(false);
		map.clear();
		
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			retval.setCode("500");
			retval.setMessage("参数为空");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}

 		List<String> params = new ArrayList<>();
		JSONObject prejsonObject = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		String jsonStr = "";
		String result = "";
		Boolean status = false;
		String message = "";
		String data = "";
		params.add(username);
		params.add(password);

		jsonStr = userService.login(params);
		
		prejsonObject = JSONObject.parseObject(jsonStr);

		if (Integer.parseInt(prejsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约！");
			retval.setCode("200");
		} else if (Integer.parseInt(prejsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("200");
		} else {
			result = prejsonObject.getString("result");
			jsonObject = JSONObject.parseObject(result);
			status = jsonObject.getBoolean("Status");
			message = jsonObject.getString("Message");
			data = jsonObject.getString("Data");
			
			if(status) {
				JSONObject userEntity = JSONObject.parseObject(data);
				User user = new User();
				user.setName(userEntity.getString("Name"));
				user.setName(userEntity.getString("Password"));
				user.setLevel(userEntity.getInteger("Level"));
				user.setCreateDate(userEntity.getString("CreateDate"));
				user.setModifyDate(userEntity.getString("ModifyDate"));
				user.setSort(userEntity.getInteger("Sort"));
				
				HttpSession session = attributes.getRequest().getSession();
				session.setAttribute("user", user);
				map.put("user", user);
			}

			map.put("status", status);
			map.put("message", message);
			map.put("result", data);
			
			retval.setData(map);
			retval.setMessage("查询成功!");
			retval.setCode("200");
		}
		
		retval.setResult(status);
		
		
		return JSON.toJSONString(retval);
	}
	
	/**
	 * 用户注册
	 * @param attributes
	 * @param model
	 * @param name
	 * @param password
	 * @return
	 */
	@RequestMapping(value= "register")
	@ResponseBody
	public String register(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "password", required = false) String password) {
		
		CommonResponse retval = new CommonResponse(false);
		map.clear();
		
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			map.put("message", "参数为空");
			retval.setCode("500");
			retval.setMessage("参数为空");
			retval.setResult(false);
			retval.setData(map);
			return JSON.toJSONString(retval);
		}
		
		Date date = new Date();
		String createDate = "";
		createDate = DateUtils.format(date, "yyyy/MM/dd");

 		List<String> params = new ArrayList<>();
		JSONObject prejsonObject = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		String jsonStr = "";
		String result = "";
		Boolean status = false;
		String message = "";
		String data = "";
		params.add(username);
		params.add(password);
		params.add(createDate);

		jsonStr = userService.register(params);
		
		prejsonObject = JSONObject.parseObject(jsonStr);

		if (Integer.parseInt(prejsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约！");
			retval.setCode("200");
		} else if (Integer.parseInt(prejsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("200");
		} else {
			result = prejsonObject.getString("result");
			jsonObject = JSONObject.parseObject(result);
			status = jsonObject.getBoolean("Status");
			message = jsonObject.getString("Message");
			data = jsonObject.getString("Data");
			
			map.put("status", status);
			map.put("message", message);
			map.put("result", data);
			
			retval.setData(map);
			retval.setMessage("查询成功!");
			retval.setCode("200");
		}
		
		retval.setResult(status);
		
		
		return JSON.toJSONString(retval);
	}
	
	/**
	 * 添加管理员(已注册用户)
	 * @param attributes
	 * @param model
	 * @param name
	 * @return
	 */
	@RequestMapping(value= "addManagerForExist")
	@ResponseBody
	public String addManagerForExist(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "name", required = false) String name) {
		
		CommonResponse retval = new CommonResponse(false);
		
		return JSON.toJSONString(retval);
	}
	
	/**
	 * 添加管理员(未注册用户)
	 * @param attributes
	 * @param model
	 * @param name
	 * @param password
	 * @return
	 */
	@RequestMapping(value= "addManagerForNotExist")
	@ResponseBody
	public String addManagerForNotExist(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "password", required = false) String password) {
		
		CommonResponse retval = new CommonResponse(false);
		
		return JSON.toJSONString(retval);
	}
	
	/**
	 * 给用户设置权限等级
	 * @param attributes
	 * @param model
	 * @param name
	 * @param level
	 * @return
	 */
	@RequestMapping(value= "addManager")
	@ResponseBody
	public String addManager(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "level", required = false) String level) {
		
		CommonResponse retval = new CommonResponse(false);
		
		return JSON.toJSONString(retval);
	}
	
	/**
	 * 设置管理员权限等级
	 * @param attributes
	 * @param model
	 * @param name
	 * @param password
	 * @return
	 */
	@RequestMapping(value= "setManagerLevel")
	@ResponseBody
	public String setManagerLevel(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "level", required = false) String level) {
		
		CommonResponse retval = new CommonResponse(false);
		
		return JSON.toJSONString(retval);
	}

	/**
	 * 设置添加权限
	 * @param attributes
	 * @param model
	 * @param level
	 * @return
	 */
	@RequestMapping(value= "setAddLevel")
	@ResponseBody
	public String setAddLevel(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "level", required = false) String level) {
		
		CommonResponse retval = new CommonResponse(false);
		
		return JSON.toJSONString(retval);
	}

	/**
	 * 设置修改权限
	 * @param attributes
	 * @param model
	 * @param level
	 * @return
	 */
	@RequestMapping(value= "setModifyLevel")
	@ResponseBody
	public String setModifyLevel(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "level", required = false) String level) {
		
		CommonResponse retval = new CommonResponse(false);
		
		return JSON.toJSONString(retval);
	}
	
	/**
	 * 设置查看权限
	 * @param attributes
	 * @param model
	 * @param level
	 * @return
	 */
	@RequestMapping(value= "setQueryLevel")
	@ResponseBody
	public String setQueryLevel(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "level", required = false) String level) {
		
		CommonResponse retval = new CommonResponse(false);
		
		return JSON.toJSONString(retval);
	}
	
	/**
	 * 设置删除权限
	 * @param attributes
	 * @param model
	 * @param level
	 * @return
	 */
	@RequestMapping(value= "setDeleteLevel")
	@ResponseBody
	public String setDeleteLevel(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "level", required = false) String level) {
		
		CommonResponse retval = new CommonResponse(false);
		
		return JSON.toJSONString(retval);
	}
	
	/**
	 * 删除用户
	 * @param attributes
	 * @param model
	 * @param name
	 * @return
	 */
	@RequestMapping(value= "deleteUser")
	@ResponseBody
	public String deleteUser(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "name", required = false) String name) {
		
		CommonResponse retval = new CommonResponse(false);
		
		return JSON.toJSONString(retval);
	}
	
}
