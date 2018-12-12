package com.springboot.basic.controller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.springboot.basic.entity.SupportModel;
import com.springboot.basic.support.CommonRequestAttributes;

@CrossOrigin
public abstract class DefaultRestfulController<T extends SupportModel, PK extends Serializable> extends SupportController<T, PK> {

	/**
	 * @param attributes
	 * @param model
	 * @throws UnsupportedEncodingException
	 */
	@ModelAttribute
	public void prepare(CommonRequestAttributes attributes, Model model) throws UnsupportedEncodingException {
		attributes.getRequest().setCharacterEncoding("UTF-8");
		attributes.getResponse().setCharacterEncoding("UTF-8");
	}

}
