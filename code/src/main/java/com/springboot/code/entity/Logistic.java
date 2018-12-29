package com.springboot.code.entity;

import lombok.Data;

/**
* @author nott
* @version 创建时间：2018年12月13日下午3:10:34
* 类说明
*/
@Data
public class Logistic {
	
	// 物流
	private String id;

	// 商品id
	private String goodsId;


	// 城市名称
	private String cityName;

	// 添加时间
	private String createDate;

	// 修改时间
	private String modifyDate;
	
	// 排序
	private Integer sort;
}
