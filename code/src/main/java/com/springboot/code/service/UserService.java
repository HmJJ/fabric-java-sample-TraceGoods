package com.springboot.code.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.springboot.fabric.interactive.service.SimpleService;

/**
* @author nott
* @version 创建时间：2018年12月29日下午1:44:21
* 类说明
*/
@Service(value = "userService")
public class UserService {
	
	@Autowired private SimpleService simpleService;

	Map<String, Object> map = new HashMap<>();
	
	/**
	 * 查看所有用户
	 * @param params
	 * @return
	 */
	public String queryAllUser(List<String> params) {
		
		map.put("type", "invoke");
		map.put("fcn", "queryAllUser");

		int length = params.size();
        String[] argArray = new String[length];
        for (int i = 0; i < length; i++) {
            argArray[i] = params.get(i);
        }
		
		map.put("array", argArray);
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		return jsonStr;
	}

	/**
	 * 用户登录
	 * @param params
	 * @return
	 */
	public String login(List<String> params) {
		
		map.put("type", "invoke");
		map.put("fcn", "login");

		int length = params.size();
        String[] argArray = new String[length];
        for (int i = 0; i < length; i++) {
            argArray[i] = params.get(i);
        }
		
		map.put("array", argArray);
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		return jsonStr;
	}

	/**
	 * 用户注册
	 * @param params
	 * @return
	 */
	public String register(List<String> params) {
		
		map.put("type", "invoke");
		map.put("fcn", "register");

		int length = params.size();
        String[] argArray = new String[length];
        for (int i = 0; i < length; i++) {
            argArray[i] = params.get(i);
        }
		
		map.put("array", argArray);
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		return jsonStr;
	}

	/**
	 * 给用户设置权限（已注册用户）
	 * @param params
	 * @return
	 */
	public String addManagerForExist(List<String> params) {
		
		map.put("type", "invoke");
		map.put("fcn", "addManagerForExist");

		int length = params.size();
        String[] argArray = new String[length];
        for (int i = 0; i < length; i++) {
            argArray[i] = params.get(i);
        }
		
		map.put("array", argArray);
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		return jsonStr;
	}

	/**
	 * 给用户设置权限（未注册用户）
	 * @param params
	 * @return
	 */
	public String addManagerForNotExist(List<String> params) {
		
		map.put("type", "invoke");
		map.put("fcn", "addManagerForNotExist");

		int length = params.size();
        String[] argArray = new String[length];
        for (int i = 0; i < length; i++) {
            argArray[i] = params.get(i);
        }
		
		map.put("array", argArray);
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		return jsonStr;
	}

	/**
	 * 添加管理员
	 * @param params
	 * @return
	 */
	public String addManager(List<String> params) {
		
		map.put("type", "invoke");
		map.put("fcn", "addManager");

		int length = params.size();
        String[] argArray = new String[length];
        for (int i = 0; i < length; i++) {
            argArray[i] = params.get(i);
        }
		
		map.put("array", argArray);
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		return jsonStr;
	}

	/**
	 * 设置管理员权限等级
	 * @param params
	 * @return
	 */
	public String setManagerLevel(List<String> params) {
		
		map.put("type", "invoke");
		map.put("fcn", "setManagerLevel");

		int length = params.size();
        String[] argArray = new String[length];
        for (int i = 0; i < length; i++) {
            argArray[i] = params.get(i);
        }
		
		map.put("array", argArray);
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		return jsonStr;
	}

	/**
	 * 设置添加权限等级
	 * @param params
	 * @return
	 */
	public String setAddLevel(List<String> params) {
		
		map.put("type", "invoke");
		map.put("fcn", "setAddLevel");

		int length = params.size();
        String[] argArray = new String[length];
        for (int i = 0; i < length; i++) {
            argArray[i] = params.get(i);
        }
		
		map.put("array", argArray);
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		return jsonStr;
	}

	/**
	 * 设置修改权限等级
	 * @param params
	 * @return
	 */
	public String setModifyLevel(List<String> params) {
		
		map.put("type", "invoke");
		map.put("fcn", "setModifyLevel");

		int length = params.size();
        String[] argArray = new String[length];
        for (int i = 0; i < length; i++) {
            argArray[i] = params.get(i);
        }
		
		map.put("array", argArray);
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		return jsonStr;
	}

	/**
	 * 设置查询权限等级
	 * @param params
	 * @return
	 */
	public String setQueryLevel(List<String> params) {
		
		map.put("type", "invoke");
		map.put("fcn", "setQueryLevel");

		int length = params.size();
        String[] argArray = new String[length];
        for (int i = 0; i < length; i++) {
            argArray[i] = params.get(i);
        }
		
		map.put("array", argArray);
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		return jsonStr;
	}

	/**
	 * 设置删除权限等级
	 * @param params
	 * @return
	 */
	public String setDeleteLevel(List<String> params) {
		
		map.put("type", "invoke");
		map.put("fcn", "setDeleteLevel");

		int length = params.size();
        String[] argArray = new String[length];
        for (int i = 0; i < length; i++) {
            argArray[i] = params.get(i);
        }
		
		map.put("array", argArray);
		//链中执行
		String jsonStr=simpleService.chainCode(new JSONObject(map));
		
		return jsonStr;
	}
	
}
