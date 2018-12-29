package com.springboot.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.springboot.basic.support.CommonRequestAttributes;
import com.springboot.basic.support.CommonResponse;
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
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "password", required = false) String password) {
		
		CommonResponse retval = new CommonResponse(false);
		
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
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "password", required = false) String password) {
		
		CommonResponse retval = new CommonResponse(false);
		
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
