package com.springboot.basic.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.criterion.DetachedCriteria;

import com.springboot.basic.entity.SupportModel;
import com.springboot.basic.page.Page;
import com.springboot.basic.page.PageRequest;
import com.springboot.basic.page.Sort;
import com.springboot.basic.repository.PageRepository;

public interface Service<T extends SupportModel, PK extends Serializable> extends SupportService<T, PK> {
	
	DetachedCriteria getDetachedCriteria();
	
	EntityManager getManager();
	
	PageRepository<T, PK> getPageRepository();
	
	T persist(T entity);
	
	T merge(T entity);

	void merge(final Collection<T> entities);
	
	T delete(PK id);
	
	void remove(PK id);
	
	void refresh(final T entity);
	
	void flush();
	
	List<T> findAll();
	
	List<T> findAll(Sort sort);
	
	T findBy(PK id);
	
	List<T> findBy(AssembleCriteriaParamsCallback callback);
	
	List<T> findBy(AssembleCriteriaParamsCallback callback, Sort sort);
	
	Page<T> pageBy(AssembleCriteriaParamsCallback callback, PageRequest pageable);

	Page<T> pageBy(AssembleCriteriaParamsCallback callback, Sort sort, PageRequest pageable);
	
	List<?> findArrayByNativeSql(final StringBuilder sql);
	
	List<?> findArrayByNativeSql(final StringBuilder sql, final Map<String, Object>params);

	List<?> findMapByNativeSql(final StringBuilder sql);

	List<?> findMapByNativeSql(final StringBuilder sql, final Map<String, Object>params);
	
	DetachedCriteria assembleSort(DetachedCriteria criteria, Sort sort);
	
	
	public interface AssembleCriteriaParamsCallback {
		abstract DetachedCriteria assembleParams(DetachedCriteria criteria);
	}
}
