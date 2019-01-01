package com.springboot.code.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springboot.basic.support.CommonRequestAttributes;
import com.springboot.code.entity.Goods;
import com.springboot.code.entity.User;
import com.springboot.code.service.FabricService;
import com.springboot.code.service.GoodsService;
import com.springboot.code.service.UserService;

/**
* @author nott
* @version 创建时间：2018年12月12日上午11:49:02
* 类说明
*/
@Controller
public class IndexController {
	
	@Autowired private GoodsService goodsService;
	@Autowired private UserService userService;
	@Autowired private FabricService fabricService;
	
	/**
	 * 登录页
	 * @return
	 */
	@RequestMapping(value= {"/","","index", "goods", "logistic", "user"})
	public String toIndex() {
		return "index";
	}
	
	/**
	 * 登出
	 * @param attributes
	 * @return
	 */
	@RequestMapping(value= "logout")
	public String loginOut(CommonRequestAttributes attributes) {
		
		HttpSession session = attributes.getRequest().getSession();
		session.setAttribute("user", null);
		
		return "index";
	}
	
	/**
	 * 跳到首页
	 * @return
	 */
	@RequestMapping(value= "main")
	public String toMain(CommonRequestAttributes attributes, Model model) {
		HttpSession session = attributes.getRequest().getSession();
		User user = (User)session.getAttribute("user");
		if(user == null) {
			return "redirect:index";
		}
		
		int usertype = userService.getUserType(user);

		model.addAttribute("usertype", usertype);
		
		model.addAttribute("user", user);
		
		return "view/main";
	}
	
	/**
	 * 跳到showGoods页面
	 * @param attributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value= "showGoods")
	public String toShowGoods(CommonRequestAttributes attributes, Model model) {
		
		HttpSession session = attributes.getRequest().getSession();
		User user = (User)session.getAttribute("user");
		if(user == null) {
			return "redirect:index";
		}
		
		List<String> params = new ArrayList<>();
		
		params.add(user.getName());
		params.add("1");
		params.add("5");
		
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
		
		int usertype = userService.getUserType(user);

		model.addAttribute("usertype", usertype);
		
		model.addAttribute("user", user);
		
		return "view/showGoods";
	}
	
	/**
	 * 跳到showFabric页面
	 * @param attributes
	 * @return
	 */
	@RequestMapping(value= "showFabric")
	public String toShowFabric(CommonRequestAttributes attributes, Model model) {
		HttpSession session = attributes.getRequest().getSession();
		User user = (User)session.getAttribute("user");
		if(user == null) {
			return "redirect:index";
		}
		
		int usertype = userService.getUserType(user);

		model.addAttribute("usertype", usertype);
		
		model.addAttribute("user", user);
		
		return "view/showFabric";
	}
	
	/**
	 * 跳到用户页面
	 * @param attributes
	 * @return
	 */
	@RequestMapping(value= "userlist")
	public String toUser(CommonRequestAttributes attributes, Model model) {
		HttpSession session = attributes.getRequest().getSession();
		User user = (User)session.getAttribute("user");
		if(user == null) {
			return "redirect:index";
		}
		
		List<String> params = new ArrayList<>();
		
		params.add(user.getName());
		params.add("1");
		params.add("5");
		
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
			
			if(status) {
				JSONArray resultArry = JSONArray.parseArray(preArry);
				
				List<User> userList = new ArrayList<User>();
				
				for(int i=0; i<resultArry.size();i++) {
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
		
		int usertype = 0;
		int queryLevel = 0;
		int addLevel = 0;
		int modifyLevel = 0;
		int deleteLevel = 0;
		int managerLevel = 0;
		
		usertype = userService.getUserType(user);
		
		switch(usertype) {
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
	 * 跳到personal页面
	 * @param attributes
	 * @return
	 */
	@RequestMapping(value= "personal")
	public String toPersonal(CommonRequestAttributes attributes, Model model) {
		HttpSession session = attributes.getRequest().getSession();
		User user = (User)session.getAttribute("user");
		if(user == null) {
			return "redirect:index";
		}
		
		int usertype = 0;
		int queryLevel = 0;
		int addLevel = 0;
		int modifyLevel = 0;
		int deleteLevel = 0;
		int managerLevel = 0;
		
		usertype = userService.getUserType(user);
		
		switch(usertype) {
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
		
		return "view/personal";
	}
	
}
