package com.springboot.code.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.fabric.interactive.service.SimpleService;

/**
 * @author nott
 * @version 创建时间：2018年12月12日上午11:21:16 类说明
 */
@Service(value = "goodsService")
public class GoodsService {
	
	@Autowired private SimpleService simpleService;

	public String add() {
		
		simpleService.chainCode(null);
		
		return null;
	}

	public String find() {

		simpleService.chainCode(null);
		
		return null;
	}

	public String modify() {

		simpleService.chainCode(null);
		
		return null;
	}

	public String delete() {

		simpleService.chainCode(null);
		
		return null;
	}

}
