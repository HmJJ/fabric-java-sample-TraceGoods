package com.springboot.basic.service;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.basic.entity.DefaultModel;
import com.springboot.basic.entity.SupportModel;
import com.springboot.basic.page.Page;
import com.springboot.basic.page.PageRequest;
import com.springboot.basic.page.Sort;
import com.springboot.basic.page.Sort.Direction;
import com.springboot.basic.repository.PageRepository;

@Transactional(readOnly = true)
public class DefaultService<T extends SupportModel, PK extends Serializable> extends DefaultSupportService<T, PK> implements Service<T, PK> {
	
	protected Class<T> clazz;
	@Autowired @Qualifier("pageRepository") PageRepository<T, PK> pageRepository;
	
	@SuppressWarnings("unchecked")
	public DefaultService() {
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
	
	public DetachedCriteria getDetachedCriteria() {
		return DetachedCriteria.forClass(this.clazz);
	}
	
	public EntityManager getManager() {
		return pageRepository.getManager();
	}
	
	public PageRepository<T, PK> getPageRepository(){
		return pageRepository;
	}
	
	@Transactional(readOnly = true)
	public T persist(T entity) {
		return getPageRepository().persist(entity);
	}
	
	@Transactional(readOnly = true)
	public void persist(Collection<T> entities) {
		for(T entity : entities) {
			persist(entity);
		}
	}
	
	@Transactional(readOnly = true)
	public T merge(T entity) {
		return getPageRepository().merge(entity);
	}
	
	@Transactional(readOnly = true)
	public void merge(Collection<T> entities) {
		for(T entity : entities) {
			merge(entity);
		}
	}
	
	@Transactional(readOnly = true)
	public T delete(PK id) {
		T entity = findBy(id);
		if(!(entity instanceof DefaultModel)) {
			return null;
		}
		((DefaultModel)entity).setDelete(Boolean.TRUE);
		getPageRepository().merge(entity);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public void remove(PK id) {
		T entity = findBy(id);
		getPageRepository().remove(entity);
	}
	
	public void refresh(T entity) {
		getPageRepository().refresh(entity);
	}
	
	public void flush() {
		getPageRepository().flush();
	}
	
	public List<T> findAll() {
		return getPageRepository().findBy(getDetachedCriteria());
	}
	
	public List<T> findAll(Sort sort) {
		return getPageRepository().findBy(assembleSort(getDetachedCriteria(), sort));
	}
	
	public T findBy(PK id) {
		return getPageRepository().findBy(clazz, id);
	}
	
	public List<T> findBy(AssembleCriteriaParamsCallback callback) {
		return findBy(callback, null);
	}
	
	public List<T> findBy(AssembleCriteriaParamsCallback callback, Sort sort) {
		return getPageRepository().findBy(assembleSort(callback.assembleParams(getDetachedCriteria()), sort));
	}
	
	public Page<T> pageBy(AssembleCriteriaParamsCallback callback, PageRequest pageable) {
		return pageBy(callback, null, pageable);
	}
	
	public Page<T> pageBy(AssembleCriteriaParamsCallback callback, Sort sort, PageRequest pageable) {
		return getPageRepository().pageBy(assembleSort(callback.assembleParams(getDetachedCriteria()), sort), pageable);
	}
	
	public List<?> findArrayByNativeSql(StringBuilder sql) {
		return getPageRepository().findArrayByNativeSql(sql);
	}
	
	public List<?> findArrayByNativeSql(StringBuilder sql, Map<String, Object>params) {
		return getPageRepository().findArrayByNativeSql(sql, params);
	}
	
	public List<?> findMapByNativeSql(StringBuilder sql) {
		return getPageRepository().findArrayByNativeSql(sql);
	}
	
	public List<?> findMapByNativeSql(StringBuilder sql, Map<String, Object>params) {
		return getPageRepository().findArrayByNativeSql(sql, params);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public DetachedCriteria assembleSort(DetachedCriteria criteria, Sort sort) {
		if(criteria == null)
			criteria = getDetachedCriteria();
		if(sort == null)
			return criteria;
		for(Sort.Order order : sort) {
			if(Direction.DESC.equals(order.getProperty()))
				criteria.addOrder(Order.desc(order.getProperty()));
			else
				criteria.addOrder(Order.asc(order.getProperty()));
		}
		return criteria;
	}

}
