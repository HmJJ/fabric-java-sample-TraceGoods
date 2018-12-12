package com.springboot.basic.controller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.springboot.basic.configure.DefaultMappingConstants;
import com.springboot.basic.entity.SupportModel;
import com.springboot.basic.service.Service;
import com.springboot.basic.support.CommonRequestAttributes;
import com.springboot.basic.support.CommonResponse;

public abstract class DefaultController<T extends SupportModel, PK extends Serializable> extends SupportController<T, PK> {
	
	public static final String SEARCH_KEY = "searchkey";
	public static final String ENTITY = "entity";
	public static final String PARENT = "parent";
	public static final String PAGE = "page";
	
	/**
	 * 获取service
	 */
	public abstract Service<T, PK> service();
	
	/**
	 * 获取页面基本路径
	 */
	public abstract String path();
	
	/**
	 * 
	 */
	@ModelAttribute
	public void prepare(CommonRequestAttributes attributes, Model model) throws UnsupportedEncodingException {
		attributes.getRequest().setCharacterEncoding("UTF-8");
		attributes.getResponse().setCharacterEncoding("UTF-8");
	}
	
	/**
	 * 打开新增页面
	 */
	@RequestMapping(value = DefaultMappingConstants.CREATE)
	public String create(CommonRequestAttributes attributes, Model model) throws Exception {
		createhandler(attributes, model);
		return path()+DefaultMappingConstants.MODIFY;
	}

	protected void createhandler(CommonRequestAttributes attributes, Model model) {
		try {
			model.addAttribute(ENTITY, clazz.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 打开新增页面（带父类ID）
	 */
	@RequestMapping(value = DefaultMappingConstants.CREATE+"/{id}")
	public String create(CommonRequestAttributes attributes, Model model, @PathVariable PK id) throws Exception {
		createhandler(attributes, model, id);
		return path()+DefaultMappingConstants.MODIFY;
	}

	protected void createhandler(CommonRequestAttributes attributes, Model model, PK id) {
		try {
			model.addAttribute(PARENT, service().findBy(id));
			model.addAttribute(ENTITY, clazz.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 逻辑删除操作
	 */
	@ResponseBody
	@RequestMapping(value = DefaultMappingConstants.DELETE+"/{id}", method = RequestMethod.POST)
	public String delete(CommonRequestAttributes attributes, Model model, @PathVariable PK id) throws Exception {
		CommonResponse retval = new CommonResponse();
		
		try {
			service().delete(id);
			retval.setResult(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return JSON.toJSONString(retval);
	}
	
	/**
	 * 物理删除操作
	 */
	@ResponseBody
	@RequestMapping(value = DefaultMappingConstants.REMOVE+"/{id}", method = RequestMethod.POST)
	public String remove(CommonRequestAttributes attributes, Model model, @PathVariable PK id) throws Exception {
		CommonResponse retval = new CommonResponse();
		
		try {
			service().remove(id);
			retval.setResult(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return JSON.toJSONString(retval);
	}
	
	/**
	 * 打开修改页面
	 */
	@RequestMapping(value = DefaultMappingConstants.MODIFY+"/{id}")
	public String modify(CommonRequestAttributes attributes, Model model, @PathVariable PK id) throws Exception {
		modifyhandler(attributes, model, id);
		return path()+DefaultMappingConstants.MODIFY;
	}

	protected void modifyhandler(CommonRequestAttributes attributes, Model model, PK id) {
		model.addAttribute(ENTITY, service().findBy(id));
	}
	
	/**
	 * 查看详情页面
	 */
	@RequestMapping(value = DefaultMappingConstants.DETAIL+"/{id}")
	public String detail(CommonRequestAttributes attributes, Model model, @PathVariable PK id) throws Exception {
		detailhandler(attributes, model, id);
		return path()+DefaultMappingConstants.DELETE;
	}

	protected void detailhandler(CommonRequestAttributes attributes, Model model, PK id) {
		model.addAttribute(ENTITY, service().findBy(id));
	}
	
	/**
	 * 提交操作
	 */
	@ResponseBody
	@RequestMapping(value = DefaultMappingConstants.COMMIT, method = RequestMethod.POST)
	public String commit(CommonRequestAttributes attributes, Model model, T entity) throws Exception {
		return commithandler(attributes, model, entity);
	}

	private String commithandler(CommonRequestAttributes attributes, Model model, T entity) {
		CommonResponse retval = new CommonResponse();
//		try {
//			if(entity instanceof DefaultModel) {
//				((DefaultModel)entity).setModifydate(new Date());
//			}
//			service().merge(entity);
//			retval.setResult(true);
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//		}
		return JSON.toJSONString(retval);
	}
}
