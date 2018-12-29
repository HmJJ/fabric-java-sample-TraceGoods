package com.springboot.code.entity;

import lombok.Data;

/**
* @author nott
* @version 创建时间：2018年12月29日下午1:37:53
* 类说明
*/
@Data
public class User {
	
	// 用户名
	private String name;
	
	// 用户密码
	private String password;
	
	// 权限等级
	private Integer level;
	
	// 注册时间
	private String createDate;
	
	// 修改时间
	private String modifyDate;
	
	// 排序
	private Integer sort;
	
}
