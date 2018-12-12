package com.springboot.basic.repository;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import com.springboot.basic.entity.SupportModel;

public interface CrudRepository<T extends SupportModel, PK extends Serializable> extends SupportRepository<T, PK> {
	
	EntityManager getManager();
	
	T persist(T entity);
	
	T merge(T entity);
	
	void remove(Class<T> clazz, PK id);
	
	void remove(T entity);
	
	boolean contains(T entity);
	
	void refresh(T entity);
	
	void flush();
	
	void clear();
	
	void lock(T entity, LockModeType lockMode);
	
	void lock(T entity, LockModeType lockMode, Map<String, Object>properties);
	
	T findBy(Class<T>clazz, PK id);
	
	void createSequence(String name, int initial, int increment);
	
	void dropSequence(String name);
	
	Long getSequenceNextValue(String name);
	
	void execute(String sql);
	
}
