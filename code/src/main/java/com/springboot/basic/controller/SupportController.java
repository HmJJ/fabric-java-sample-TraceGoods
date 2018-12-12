package com.springboot.basic.controller;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.springboot.basic.entity.SupportModel;

public abstract class SupportController<T extends SupportModel,PK extends Serializable> {
	
	protected final Log logger = LogFactory.getLog(getClass());
	protected Class<T> clazz;
	
	@SuppressWarnings("unchecked")
	public SupportController() {
		try {
			Type type;
			do {
				type = getClass().getGenericSuperclass();
				if(type instanceof ParameterizedType) {
					type = ((ParameterizedType)type).getActualTypeArguments()[0];
					break;
				}
			} while (type instanceof Class<?>);
			if(type instanceof Class) {
				this.clazz = (Class<T>)type;
			}else if (type instanceof ParameterizedType) {
				this.clazz = (Class<T>)((ParameterizedType)type).getRawType();
			}else {
				throw new IllegalArgumentException("problem determining the class of the generic for "+ getClass());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
