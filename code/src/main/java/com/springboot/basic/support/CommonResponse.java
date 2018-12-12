package com.springboot.basic.support;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data //一键添加@Getter @Setter
public class CommonResponse {
	
	/**
	 * 是否正确
	 */
	private boolean result = false;
	
	/**
	 * 代码
	 */
	private String code = StringUtils.EMPTY;
	
	/**
	 * 消息
	 */
	private String message = StringUtils.EMPTY;
	
	/**
	 * 数据主体
	 */
	private Object data = null;
	
	/**
	 * 版本号
	 */
	private String version = "v0.01";
	
	/**
	 * 签名
	 */
	private String signature = StringUtils.EMPTY;
	
	/**
	 * <pre>default [ result:false ]</pre> 
	 */
	public CommonResponse() {
		super();
	}
	
	/**
	 * 是否正确
	 */
	public CommonResponse(boolean result) {
		this.result  = result;
		this.data = null;
	}
	
	/**
	 * 是否正确
	 * 代码
	 * 消息
	 */
	public CommonResponse(boolean result, String code, String message) {
		this.result  = result;
		this.data = null;
		this.code = code;
		this.message = message;
	}
	
	/**
	 * 是否正确
	 * 代码
	 * 消息
	 * 数据主体
	 */
	public CommonResponse(boolean result, String code, String message, Object data) {
		this.result  = result;
		this.data = data;
		this.code = code;
		this.message = message;
	}
	
	/**
	 * 是否正确
	 * 代码
	 * 消息
	 * 版本号
	 * 签名
	 * 数据主体
	 */
	public CommonResponse(boolean result, String code, String message, String version, String signature, Object data) {
		this.result  = result;
		this.data = data;
		this.code = code;
		this.message = message;
		this.version = version;
		this.signature = signature;
	}
}
