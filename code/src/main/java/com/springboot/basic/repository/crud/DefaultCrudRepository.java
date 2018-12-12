package com.springboot.basic.repository.crud;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import com.springboot.basic.entity.SupportModel;
import com.springboot.basic.repository.CrudRepository;
import com.springboot.basic.repository.support.DefaultSupportRepository;

import lombok.Getter;
import lombok.Setter;

/**
 * Default C/R/U/D Repository
 * 
 * @param <T> SupportModel
 * @param <PK> Serializable
 * 
 * @since repository 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
public class DefaultCrudRepository<T extends SupportModel, PK extends Serializable> extends DefaultSupportRepository<T, PK> implements CrudRepository<T, PK> {

	@PersistenceContext private EntityManager manager;
	@Getter @Setter private String license;
	static Integer day;
	static String date;

	@Override
	public EntityManager getManager() {
		return this.manager;
	}

	@Override
	public T persist(T entity) {
		getManager().persist(entity);
		return entity;
	}

	@Override
	public T merge(T entity) {
		return getManager().< T >merge(entity);
	}

	@Override
	public void remove(Class<T> clazz, PK id) {
		remove(findBy(clazz, id));
	}

	@Override
	public void remove(T entity) {
		getManager().remove(entity);
	}

	@Override
	public boolean contains(T entity) {
		return getManager().contains(entity);
	}

	@Override
	public void refresh(T entity) {
		getManager().refresh(entity);
	}

	@Override
	public void flush() {
		getManager().flush();
	}

	@Override
	public void clear() {
		getManager().clear();
	}

	@Override
	public void lock(T entity, LockModeType lockMode) {
		getManager().lock(entity, lockMode);
	}

	@Override
	public void lock(T entity, LockModeType lockMode, Map<String, Object> properties) {
		getManager().lock(entity, lockMode, properties);
	}

	@Override
	public T findBy(Class<T> clazz, PK id) {
		return getManager().< T >find(clazz, id);
	}

	@Override
	public void createSequence(String name, int initial, int increment) {
		execute(" CREATE SEQUENCE " + name + " START WITH " + initial + " INCREMENT BY " + increment);
	}

	@Override
	public void dropSequence(String name) {
		execute(" DROP SEQUENCE " + name);
	}

	@Override
	public Long getSequenceNextValue(String name) {
		String sql = " SELECT " + name + ".NEXTVAL FROM DUAL ";
		long nextval = ((Number) getManager().createNativeQuery(sql).getSingleResult()).longValue();
		logger.info("SEQUENCE, " + name + ".NEXTVAL : " + nextval);
		return Long.valueOf(nextval);
	}

	@Override
	public void execute(String sql) {
		getManager().createNativeQuery(sql).executeUpdate();
	}

}
