package com.springboot.basic.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {
	protected static final Log logger = LogFactory.getLog(BeanUtils.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, Object> bean2map(Object bean) {
		HashMap description = new HashMap();
		if (bean == null) {
			return description;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Describing bean: " + bean.getClass().getName());
			}

			try {
				PropertyUtilsBean e = BeanUtilsBean.getInstance().getPropertyUtils();
				if (bean instanceof DynaBean) {
					DynaProperty[] arg7 = ((DynaBean) bean).getDynaClass().getDynaProperties();

					for (int arg8 = 0; arg8 < arg7.length; ++arg8) {
						String arg9 = arg7[arg8].getName();
						description.put(arg9, e.getNestedProperty(bean, arg9));
					}

					return description;
				}

				PropertyDescriptor[] descriptors = e.getPropertyDescriptors(bean);
				Class clazz = bean.getClass();

				for (int i = 0; i < descriptors.length; ++i) {
					String name = descriptors[i].getName();
					if (MethodUtils.getAccessibleMethod(clazz, descriptors[i].getReadMethod()) != null) {
						description.put(name, e.getNestedProperty(bean, name));
					}
				}
			} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException arg6) {
				logger.error(arg6.getMessage(), arg6);
			}

			return description;
		}
	}

	public static void map2bean(Object bean, Map<String, ? extends Object> properties) {
		try {
			org.apache.commons.beanutils.BeanUtils.populate(bean, properties);
		} catch (InvocationTargetException | IllegalAccessException arg2) {
			logger.error(arg2.getMessage(), arg2);
		}

	}
}