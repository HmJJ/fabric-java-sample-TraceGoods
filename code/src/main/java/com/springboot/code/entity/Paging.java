package com.springboot.code.entity;

import lombok.Data;

/**
* @author nott
* @version 创建时间：2018年12月29日下午1:38:18
* 类说明
*/
@Data
public class Paging {
	
	// 页码
	private String pageNum;

	// 每页的数量
	private String pageSize;
	
	// 总页数
	private String pageCount;
	
}
