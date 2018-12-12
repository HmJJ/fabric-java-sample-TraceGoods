package com.springboot.basic.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;

import com.springboot.basic.entity.SupportModel;

public interface FindRepository<T extends SupportModel, PK extends Serializable> extends CrudRepository<T, PK> {
	
	List<T> findBy(final DetachedCriteria criteria);
	
	List<T> findByHsql(final StringBuilder hsql);
	
	List<T> findByHsql(final StringBuilder hsql, final Map<String, Object>params);
	
	List<?> findArrayByNativeSql(final StringBuilder sql);

	List<?> findArrayByNativeSql(final StringBuilder sql, final Map<String, Object>params);
	
	List<?> findMapByNativeSql(final StringBuilder sql);
	
	List<?> findMapByNativeSql(final StringBuilder sql, final Map<String, Object>params);
	
}
