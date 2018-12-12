package com.springboot.basic.repository.crud;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.transform.Transformers;
import org.springframework.util.Assert;

import com.springboot.basic.entity.SupportModel;
import com.springboot.basic.repository.FindRepository;
import com.springboot.basic.repository.configure.AssertMessage;
import com.springboot.basic.repository.configure.SuppressWarningsParams;


/**
 * Default Find Repository
 * 
 * @param <T> SupportModel
 * @param <PK> Serializable
 * 
 * @since repository 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
@SuppressWarnings("deprecation")
public class DefaultFindRepository<T extends SupportModel, PK extends Serializable> extends DefaultCrudRepository<T, PK> implements FindRepository<T, PK> {

	@Override
	public List< T > findBy(final DetachedCriteria criteria) {
		Assert.notNull(criteria, AssertMessage.NotNull);

		Criteria execute = criteria.getExecutableCriteria((Session) getManager().getDelegate());

		@SuppressWarnings(SuppressWarningsParams.UNCHECKED)
		List< T > content = execute.list();

		return content;
	}

	@Override
	public List<T> findByHsql(final StringBuilder hsql) {
		return findByHsql(hsql, new HashMap<String, Object>());
	}

	@Override
	public List<T> findByHsql(final StringBuilder hsql, final Map<String, Object> params) {
		Assert.notNull(hsql, AssertMessage.NotNull);
		Assert.notNull(params, AssertMessage.NotNull);

		final String queryString = hsql.toString();
		Assert.hasText(queryString, AssertMessage.HasText);

		Query query = getManager().createQuery(queryString);
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		@SuppressWarnings(SuppressWarningsParams.UNCHECKED)
		List<T> content = query.getResultList();

		return content;
	}

	@Override
	public List<?> findArrayByNativeSql(final StringBuilder sql) {
		return findArrayByNativeSql(sql, new HashMap<String, Object>());
	}

	@Override
	public List<?> findArrayByNativeSql(final StringBuilder sql, final Map<String, Object> params) {
		Assert.notNull(sql, AssertMessage.NotNull);
		Assert.notNull(params, AssertMessage.NotNull);

		final String queryString = sql.toString();
		Assert.hasText(queryString, AssertMessage.HasText);

		Query query = getManager().createNativeQuery(queryString);
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		List<?> content = query.getResultList();

		return content;
	}

	@Override
	public List<Map<String, Object>> findMapByNativeSql(final StringBuilder sql) {
		return findMapByNativeSql(sql, new HashMap<String, Object>());
	}

	@Override
	public List<Map<String, Object>> findMapByNativeSql(final StringBuilder sql, final Map<String, Object> params) {
		Assert.notNull(sql, AssertMessage.NotNull);
		Assert.notNull(params, AssertMessage.NotNull);

		final String queryString = sql.toString();
		Assert.hasText(queryString, AssertMessage.HasText);

		Query query = getManager().createNativeQuery(queryString);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		@SuppressWarnings(SuppressWarningsParams.UNCHECKED)
		List<Map<String, Object>> content = query.getResultList();

		return content;
	}

}
