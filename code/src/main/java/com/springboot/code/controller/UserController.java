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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springboot.basic.support.CommonRequestAttributes;
import com.springboot.basic.support.CommonResponse;
import com.springboot.basic.utils.StringUtils;
import com.springboot.basic.utils.time.DateUtils;
import com.springboot.code.entity.User;
import com.springboot.code.service.FabricService;
import com.springboot.code.service.UserService;

/**
 * @author nott
 * @version 创建时间：2018年12月29日下午1:44:13 类说明
 */
@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private FabricService fabricService;
	Map<String, Object> map = new HashMap<>();

	/**
	 * 用户登录
	 * 
	 * @param attributes
	 * @param model
	 * @param name
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "login")
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

			if (status) {
				JSONObject userEntity = JSONObject.parseObject(data);
				User user = new User();
				user.setName(userEntity.getString("Name"));
				user.setPassword(userEntity.getString("Password"));
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
	 * 
	 * @param attributes
	 * @param model
	 * @param name
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "register")
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
	 * 分页查看用户
	 * 
	 * @param attributes
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "findByPage")
	public String findByPage(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {

		HttpSession session = attributes.getRequest().getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
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

			String jsonStr = userService.queryAllUser(params);

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
				String message = result.getString("Message");
				Boolean status = result.getBoolean("Status");
				int num = result.getIntValue("PageNum");
				int size = result.getIntValue("PageSize");
				int total = result.getIntValue("Total");

				if (status) {
					JSONArray resultArry = JSONArray.parseArray(preArry);

					List<User> userList = new ArrayList<User>();

					for (int i = 0; i < resultArry.size(); i++) {
						JSONObject goodsObject = resultArry.getJSONObject(i);
						User u = new User();
						u.setName(goodsObject.getString("Name"));
						u.setPassword(goodsObject.getString("Password"));
						u.setLevel(goodsObject.getInteger("Level"));
						u.setCreateDate(goodsObject.getString("CreateDate").replace("/", "-"));
						u.setModifyDate(goodsObject.getString("ModifyDate").replace("/", "-"));
						u.setSort(goodsObject.getInteger("Sort"));
						userList.add(u);
					}

					model.addAttribute("txid", txid);
					model.addAttribute("usersInfo", userList);
					model.addAttribute("message", message);
					model.addAttribute("code", "200");
					model.addAttribute("pageNum", num);
					model.addAttribute("pageSize", size);
					model.addAttribute("total", total);
				} else {
					model.addAttribute("message", message);
					model.addAttribute("code", "500");
				}
			}
		}

		int usertype = 0;
		int queryLevel = 0;
		int addLevel = 0;
		int modifyLevel = 0;
		int deleteLevel = 0;
		int managerLevel = 0;

		usertype = userService.getUserType(user);

		switch (usertype) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			queryLevel = fabricService.getLevel("sheep_query_level");
			addLevel = fabricService.getLevel("sheep_add_level");
			modifyLevel = fabricService.getLevel("sheep_modify_level");
			break;
		case 3:
			queryLevel = fabricService.getLevel("sheep_query_level");
			addLevel = fabricService.getLevel("sheep_add_level");
			modifyLevel = fabricService.getLevel("sheep_modify_level");
			deleteLevel = fabricService.getLevel("sheep_delete_level");
			managerLevel = fabricService.getLevel("sheep_manager_level");
			break;
		}

		model.addAttribute("queryLevel", queryLevel);
		model.addAttribute("addLevel", addLevel);
		model.addAttribute("modifyLevel", modifyLevel);
		model.addAttribute("deleteLevel", deleteLevel);
		model.addAttribute("managerLevel", managerLevel);
		model.addAttribute("usertype", usertype);
		model.addAttribute("user", user);

		return "view/user";
	}

	/**
	 * 添加管理员(已注册用户)
	 * 
	 * @param attributes
	 * @param model
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "addManagerForExist")
	@ResponseBody
	public String addManagerForExist(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "level", required = false) String level) {

		CommonResponse retval = new CommonResponse(false);
		map.clear();

		HttpSession session = attributes.getRequest().getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			retval.setCode("406");
			retval.setMessage("请先登录!");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}

		if (StringUtils.isBlank(name) || StringUtils.isBlank(level)) {
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
		params.add(level);
		params.add(user.getName());

		jsonStr = userService.addManagerForExist(params);

		prejsonObject = JSONObject.parseObject(jsonStr);

		if (Integer.parseInt(prejsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约！");
			retval.setCode("40029");
			retval.setResult(false);
		} else if (Integer.parseInt(prejsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
			retval.setResult(false);
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
			retval.setMessage(message);
			retval.setCode("200");
			retval.setResult(true);
		}

		return JSON.toJSONString(retval);
	}

	/**
	 * 添加管理员(未注册用户)
	 * 
	 * @param attributes
	 * @param model
	 * @param name
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "addManagerForNotExist")
	@ResponseBody
	public String addManagerForNotExist(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "password", required = false) String password) {

		CommonResponse retval = new CommonResponse(false);
		map.clear();

		HttpSession session = attributes.getRequest().getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			retval.setCode("406");
			retval.setMessage("请先登录!");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}

		if (StringUtils.isBlank(name) || StringUtils.isBlank(password)) {
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
		params.add(name);
		params.add(password);
		params.add(user.getName());
		params.add(user.getPassword());
		Date date = new Date();
		String modifyDate = DateUtils.format(date, "yyyy/MM/dd");
		params.add(modifyDate);

		jsonStr = userService.addManagerForNotExist(params);

		prejsonObject = JSONObject.parseObject(jsonStr);

		if (Integer.parseInt(prejsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约！");
			retval.setCode("40029");
			retval.setResult(false);
		} else if (Integer.parseInt(prejsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
			retval.setResult(false);
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
			retval.setMessage(message);
			retval.setCode("200");
			retval.setResult(true);
		}

		return JSON.toJSONString(retval);
	}

	/**
	 * 给用户设置权限等级
	 * 
	 * @param attributes
	 * @param model
	 * @param name
	 * @param level
	 * @return
	 */
	@RequestMapping(value = "setUserLevel")
	@ResponseBody
	public String setUserLevel(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "level", required = false) String level) {

		CommonResponse retval = new CommonResponse(false);
		map.clear();

		HttpSession session = attributes.getRequest().getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			retval.setCode("406");
			retval.setMessage("请先登录!");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}

		if (StringUtils.isBlank(name) || StringUtils.isBlank(level)) {
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
		params.add(name);
		params.add(user.getName());
		params.add(user.getPassword());
		params.add(level);
		Date date = new Date();
		String modifyDate = DateUtils.format(date, "yyyy/MM/dd");
		params.add(modifyDate);

		jsonStr = userService.addManager(params);

		prejsonObject = JSONObject.parseObject(jsonStr);

		if (Integer.parseInt(prejsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约！");
			retval.setCode("40029");
			retval.setResult(false);
		} else if (Integer.parseInt(prejsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
			retval.setResult(false);
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
			retval.setMessage(message);
			retval.setCode("200");
			retval.setResult(true);
		}

		return JSON.toJSONString(retval);
	}

	/**
	 * 设置管理员权限等级
	 * 
	 * @param attributes
	 * @param model
	 * @param name
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "setManagerLevel")
	@ResponseBody
	public String setManagerLevel(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "level", required = false) String level) {

		CommonResponse retval = new CommonResponse(false);
		map.clear();

		HttpSession session = attributes.getRequest().getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			retval.setCode("406");
			retval.setMessage("请先登录!");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}

		if (StringUtils.isBlank(level)) {
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
		params.add(user.getName());
		params.add(user.getPassword());
		params.add(level);

		jsonStr = userService.setManagerLevel(params);

		prejsonObject = JSONObject.parseObject(jsonStr);

		if (Integer.parseInt(prejsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约！");
			retval.setCode("40029");
			retval.setResult(false);
		} else if (Integer.parseInt(prejsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
			retval.setResult(false);
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
			retval.setMessage(message);
			retval.setCode("200");
			retval.setResult(true);
		}

		return JSON.toJSONString(retval);
	}

	/**
	 * 设置添加权限
	 * 
	 * @param attributes
	 * @param model
	 * @param level
	 * @return
	 */
	@RequestMapping(value = "setAddLevel")
	@ResponseBody
	public String setAddLevel(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "level", required = false) String level) {

		CommonResponse retval = new CommonResponse(false);
		map.clear();

		HttpSession session = attributes.getRequest().getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			retval.setCode("406");
			retval.setMessage("请先登录!");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}

		if (StringUtils.isBlank(level)) {
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
		params.add(user.getName());
		params.add(user.getPassword());
		params.add(level);

		jsonStr = userService.setAddLevel(params);

		prejsonObject = JSONObject.parseObject(jsonStr);

		if (Integer.parseInt(prejsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约！");
			retval.setCode("40029");
			retval.setResult(false);
		} else if (Integer.parseInt(prejsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
			retval.setResult(false);
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
			retval.setMessage(message);
			retval.setCode("200");
			retval.setResult(true);
		}

		return JSON.toJSONString(retval);
	}

	/**
	 * 设置修改权限
	 * 
	 * @param attributes
	 * @param model
	 * @param level
	 * @return
	 */
	@RequestMapping(value = "setModifyLevel")
	@ResponseBody
	public String setModifyLevel(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "level", required = false) String level) {

		CommonResponse retval = new CommonResponse(false);
		map.clear();

		HttpSession session = attributes.getRequest().getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			retval.setCode("406");
			retval.setMessage("请先登录!");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}

		if (StringUtils.isBlank(level)) {
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
		params.add(user.getName());
		params.add(user.getPassword());
		params.add(level);

		jsonStr = userService.setModifyLevel(params);

		prejsonObject = JSONObject.parseObject(jsonStr);

		if (Integer.parseInt(prejsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约！");
			retval.setCode("40029");
			retval.setResult(false);
		} else if (Integer.parseInt(prejsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
			retval.setResult(false);
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
			retval.setMessage(message);
			retval.setCode("200");
			retval.setResult(true);
		}

		return JSON.toJSONString(retval);
	}

	/**
	 * 设置查看权限
	 * 
	 * @param attributes
	 * @param model
	 * @param level
	 * @return
	 */
	@RequestMapping(value = "setQueryLevel")
	@ResponseBody
	public String setQueryLevel(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "level", required = false) String level) {

		CommonResponse retval = new CommonResponse(false);
		map.clear();

		HttpSession session = attributes.getRequest().getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			retval.setCode("406");
			retval.setMessage("请先登录!");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}

		if (StringUtils.isBlank(level)) {
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
		params.add(user.getName());
		params.add(user.getPassword());
		params.add(level);

		jsonStr = userService.setQueryLevel(params);

		prejsonObject = JSONObject.parseObject(jsonStr);

		if (Integer.parseInt(prejsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约！");
			retval.setCode("40029");
			retval.setResult(false);
		} else if (Integer.parseInt(prejsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
			retval.setResult(false);
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
			retval.setMessage(message);
			retval.setCode("200");
			retval.setResult(true);
		}

		return JSON.toJSONString(retval);
	}

	/**
	 * 设置删除权限
	 * 
	 * @param attributes
	 * @param model
	 * @param level
	 * @return
	 */
	@RequestMapping(value = "setDeleteLevel")
	@ResponseBody
	public String setDeleteLevel(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "level", required = false) String level) {

		CommonResponse retval = new CommonResponse(false);
		map.clear();

		HttpSession session = attributes.getRequest().getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			retval.setCode("406");
			retval.setMessage("请先登录!");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}

		if (StringUtils.isBlank(level)) {
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
		params.add(user.getName());
		params.add(user.getPassword());
		params.add(level);

		jsonStr = userService.setDeleteLevel(params);

		prejsonObject = JSONObject.parseObject(jsonStr);

		if (Integer.parseInt(prejsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约！");
			retval.setCode("40029");
			retval.setResult(false);
		} else if (Integer.parseInt(prejsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
			retval.setResult(false);
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
			retval.setMessage(message);
			retval.setCode("200");
			retval.setResult(true);
		}

		return JSON.toJSONString(retval);
	}

	/**
	 * 删除用户
	 * 
	 * @param attributes
	 * @param model
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "deleteUser")
	@ResponseBody
	public String deleteUser(CommonRequestAttributes attributes, Model model,
			@RequestParam(value = "name", required = false) String name) {

		CommonResponse retval = new CommonResponse(false);
		map.clear();

		HttpSession session = attributes.getRequest().getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			retval.setCode("406");
			retval.setMessage("请先登录!");
			retval.setResult(false);
			return JSON.toJSONString(retval);
		}

		if (StringUtils.isBlank(name)) {
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
		params.add(name);
		params.add(user.getName());

		jsonStr = userService.delete(params);

		prejsonObject = JSONObject.parseObject(jsonStr);

		if (Integer.parseInt(prejsonObject.getString("status")) == 40029) {
			retval.setMessage("fabric错误，请检查设置以及智能合约！");
			retval.setCode("40029");
			retval.setResult(false);
		} else if (Integer.parseInt(prejsonObject.getString("status")) == 8) {
			retval.setMessage("fabric用户未登陆!");
			retval.setCode("8");
			retval.setResult(false);
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
			retval.setMessage(message);
			retval.setCode("200");
			retval.setResult(true);
		}

		return JSON.toJSONString(retval);
	}

}
