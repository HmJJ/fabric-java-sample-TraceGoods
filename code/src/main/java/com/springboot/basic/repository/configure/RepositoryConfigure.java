package com.springboot.basic.repository.configure;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.CriteriaImpl.OrderEntry;
import org.springframework.util.Assert;

@SuppressWarnings(SuppressWarningsParams.UNCHECKED)
public class RepositoryConfigure {

	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * 
	 */
	protected RepositoryConfigure() {

	}

	/**
	 * CRITERIA 最基本的查询条件组装
	 * 
	 * @param criteria  Criteria
	 * @param filterMap Map
	 * @return 组装完成的criteria
	 */
	public static Criteria assemble(Criteria criteria, Map<Object, Object> filterMap) {
		Assert.notNull(criteria, AssertMessage.NotNull);
		Assert.notEmpty(filterMap, AssertMessage.NotEmpty);
		Iterator< ? > iterator = filterMap.entrySet().iterator();
		while (iterator.hasNext()) {
			String propertyName = (String) iterator.next();
			Assert.notNull(propertyName, AssertMessage.NotNull);
			Object value =  filterMap.get(propertyName);
			Assert.notNull(value, AssertMessage.NotNull);
			criteria.add(Restrictions.eq(propertyName, value));
		}
		return criteria;
	}

	/**
	 * 获取Criteria的Projection
	 * 
	 * @param criteria criteria查询
	 * @return projection
	 */
	public static Projection getProjection(Criteria criteria) {
		return ((CriteriaImpl) criteria).getProjection();
	}

	/**
	 * 获取Criteria的Orders
	 * 
	 * @param criteria criteria查询
	 * @return orderEntry[]
	 */
	public static OrderEntry[] getOrders(Criteria criteria) {
		CriteriaImpl impl = (CriteriaImpl) criteria;
		Field field = getOrderEntriesField(criteria);
		try {
			return (OrderEntry[]) ((List<Object>) field.get(impl)).toArray(new OrderEntry[0]);
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility can't throw ");
		}
	}

	/**
	 * 移除Criteria的Orders
	 * 
	 * @param criteria Criteria
	 * @return 移除完Orders的criteria
	 */
	public static Criteria removeOrders(Criteria criteria) {
		CriteriaImpl impl = (CriteriaImpl) criteria;
		try {
			Field field = getOrderEntriesField(criteria);
			field.set(impl, new ArrayList<Object>());
			return impl;
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility can't throw ");
		}
	}

	/**
	 * 添加Criteria的Orders
	 * 
	 * @param criteria     Criteria
	 * @param orderEntries OrderEntry[]
	 * @return 添加完Orders的criteria
	 */
	public static Criteria addOrders(Criteria criteria, OrderEntry[] orderEntries) {
		CriteriaImpl impl = (CriteriaImpl) criteria;
		try {
			Field field = getOrderEntriesField(criteria);
			for (int i = 0; i < orderEntries.length; i++) {
				Object obj = field.get(criteria);
				if (obj instanceof List) {
					List<Object> innerOrderEntries = (List<Object>) obj;
					innerOrderEntries.add(orderEntries[i]);
				}
			}
			return impl;
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility can't throw ");
		}
	}

	/**
	 * 获取OrderEntry的Field对象
	 * 
	 * @param criteria Criteria
	 * @return field
	 */
	protected static Field getOrderEntriesField(Criteria criteria) {
		Assert.notNull(criteria, " criteria is requried! ");
		try {
			Field field = FieldUtils.getField(CriteriaImpl.class, "orderEntries", true);
			field.setAccessible(Boolean.TRUE);
			return field;
		} catch (Exception e) {
			throw new InternalError();
		}
	}

	/**
	 * 去除HQL语句前的select
	 * 
	 * @param query String HQL查询语句
	 * @return String
	 */
	public static String removeSelect(String query) {
		Assert.notNull(query, AssertMessage.NotNull);
		query = removeFetch(query);
		int beginPos = query.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " the hql : " + query + " must has a keyword 'from'");
		return query.substring(beginPos);
	}

	/**
	 * 去除HQL语句后的order by部分
	 * 
	 * @param query String HQL查询语句
	 * @return String
	 */
	public static String removeOrders(String query) {
		Pattern pattern = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		StringBuffer stringBuffer = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(stringBuffer, "");
		}
		matcher.appendTail(stringBuffer);
		return stringBuffer.toString();
	}

	/**
	 * 去除HQL语句后的group by部分
	 * 
	 * @param query String HQL查询语句
	 * @return String
	 */
	public static String removeGroups(String query) {
		Pattern pattern = Pattern.compile("group\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);
		StringBuffer stringBuffer = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(stringBuffer, "");
		}
		matcher.appendTail(stringBuffer);
		return stringBuffer.toString();
	}

	/**
	 * 去除HQL语句内的fetch部分
	 * 
	 * @param query String
	 * @return String
	 */
	public static String removeFetch(String query) {
		Assert.notNull(query, AssertMessage.NotNull);
		return StringUtils.remove(query, " fetch");
	}

	/**
	 * 组装HQL
	 * 
	 * @param query StringBuffer
	 * @param conditions List<String>
	 * @return String
	 */
	public static String assembleHql(StringBuffer query, List<String> conditions) {
		Assert.notNull(query, AssertMessage.NotNull);
		Assert.notNull(conditions, AssertMessage.NotNull);
		for (int i = 0; i < conditions.size(); i++) {
			String condition = (String) conditions.get(i);
			if (i == 0) {
				query.append(" where ");
				query.append(condition);
			} else {
				if (condition.trim().substring(0, 5).equals("order")) {
					query.append(condition);
				} else if (condition.trim().substring(0, 5).equals("group")) {
					query.append(condition);
				} else {
					query.append(" and ");
					query.append(condition);
				}
			}
		}
		return query.toString();
	}

}