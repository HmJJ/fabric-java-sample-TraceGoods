package com.springboot.basic.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.Getter;
import lombok.Setter;

public class CommonRequestAttributes {
	
	public CommonRequestAttributes() {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		this.request = ((ServletRequestAttributes)attributes).getRequest();
		this.response = ((ServletRequestAttributes)attributes).getResponse();
	}
	
	@Getter
	@Setter
	private Integer pageindex = 0;

	@Getter
	@Setter
	private Integer pagesize = 20;

	@Getter
	@Setter
	private String searchKey;

	@Getter
	@Setter
	private String param;

	@Getter
	@Setter
	private HttpServletRequest request;

	@Getter
	@Setter
	private HttpServletResponse response;
	
}
