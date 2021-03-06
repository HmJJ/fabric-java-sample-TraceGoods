package com.springboot.code.entity;

import lombok.Data;

/**
* @author nott
* @version 创建时间：2018年12月13日下午3:10:28
* 类说明
*/
@Data
public class Goods {
	// 商品id
	private String id;
	
	// 商品名称
	private String name;
	
	// 商品价格
	private String price;

	// 添加时间
	private String createDate;

	// 修改时间
	private String modifyDate;
	
	// 排序
	private Integer sort;
}
