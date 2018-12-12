package com.springboot.basic.repository.datasource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Dynamic DataSource Switcher
 * 
 * <pre>
 * master: write
 * slave : read
 * </pre>
 * 
 * @since repository 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
public class DynamicDataSourceSwitcher {

	protected final static Log logger = LogFactory.getLog(DynamicDataSourceSwitcher.class);

	/**
	 * context is null or Boolean.TRUE = master
	 * other = slave
	 */
	private static final ThreadLocal<Boolean> context = new ThreadLocal<Boolean>();

	/**
	 * all slave datasource keys
	 */
	private static final ThreadLocal<List<Object>> keys = new ThreadLocal<List<Object>>();

	/**
	 * switch master
	 */
	public static void setMasterDataSource() {
		context.set(Boolean.TRUE);
	}

	/**
	 * switch slave
	 */
	public static void setSlaveDataSource() {
		context.set(Boolean.FALSE);
	}

	/**
	 * @param objs
	 * @return
	 */
	public static Object get(Set<Object> objs) {
		if (objs == null || objs.isEmpty()) {
			logger.info("datasource:\t\t\t\tmaster");
			return null;
		}
		if (context.get() == null || context.get()) {
			logger.info("datasource:\t\t\t\tmaster");
			return null;
		}
		if (keys.get() == null || keys.get().isEmpty()) {
			keys.set(new ArrayList<Object>(objs));
		}
		List<Object> temp = keys.get();
		Collections.shuffle(temp);
		keys.set(temp);
		logger.info("datasource: " + (String) temp.get(0));
		return temp.get(0);
	}

	/**
	 * clear
	 */
	public static void clear() {
		context.remove();
	}
}
