package com.springboot.basic.repository.crud;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl.OrderEntry;
import org.springframework.util.Assert;

import com.springboot.basic.entity.SupportModel;
import com.springboot.basic.page.DefaultPage;
import com.springboot.basic.page.Page;
import com.springboot.basic.page.PageRequest;
import com.springboot.basic.repository.PageRepository;
import com.springboot.basic.repository.configure.AssertMessage;
import com.springboot.basic.repository.configure.RepositoryConfigure;
import com.springboot.basic.repository.configure.SuppressWarningsParams;


/**
 * Default Page Repository
 * 
 * @param <T> SupportModel
 * @param <PK> Serializable
 * 
 * @since repository 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
public class DefaultPageRepository<T extends SupportModel, PK extends Serializable> extends DefaultFindRepository<T, PK> implements PageRepository<T, PK> {

	@Override
	public Page<T> pageBy(final DetachedCriteria criteria, final PageRequest pageable) {
		Assert.notNull(criteria, AssertMessage.NotNull);
		Assert.notNull(pageable, AssertMessage.NotNull);

		Criteria execute = criteria.getExecutableCriteria((Session) getManager().getDelegate());
		OrderEntry[] orderEntries = RepositoryConfigure.getOrders(execute);
		execute = RepositoryConfigure.removeOrders(execute);
		Projection projection = RepositoryConfigure.getProjection(execute);
		int total = ((Number) execute.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		execute.setProjection(projection);
		if (null == projection)
			execute.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		execute = RepositoryConfigure.addOrders(execute, orderEntries);
		execute.setFirstResult((pageable.getPageNumber()) * pageable.getPageSize());
		execute.setMaxResults(pageable.getPageSize());

		@SuppressWarnings(SuppressWarningsParams.UNCHECKED)
		List<T> content = execute.list();

		return new DefaultPage<T>(content, pageable, total);
	}

	@Override
	public Page<T> pageBy(final StringBuilder hsql, final Map<String, Object> params, final PageRequest pageable) {
		Assert.notNull(hsql, AssertMessage.NotNull);

		String queryString = hsql.toString();
		Assert.hasText(queryString, AssertMessage.HasText);

		StringBuilder hcount = new StringBuilder();
		hcount.append("SELECT COUNT(*) ");
		hcount.append(RepositoryConfigure.removeSelect(RepositoryConfigure.removeGroups(RepositoryConfigure.removeOrders(queryString))));

		return pageBy(hsql, hcount, params, pageable);
	}

	@Override
	public Page<T> pageBy(final StringBuilder hsql, final StringBuilder hcount, final PageRequest pageable) {
		return pageBy(hsql, hcount, new HashMap<String, Object>(), pageable);
	}

	@Override
	public Page<T> pageBy(final StringBuilder hsql, final StringBuilder hcount, final Map<String, Object> params, final PageRequest pageable) {
		Assert.notNull(hsql, AssertMessage.NotNull);
		Assert.notNull(hcount, AssertMessage.NotNull);
		Assert.notNull(params, AssertMessage.NotNull);
		Assert.notNull(pageable, AssertMessage.NotNull);

		final String countString = hcount.toString();
		final String queryString = hsql.toString();
		Assert.hasText(countString, AssertMessage.HasText);
		Assert.hasText(queryString, AssertMessage.HasText);

		Query count = getManager().createQuery(countString);
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			count.setParameter(entry.getKey(), entry.getValue());
		}
		int total = ((Number) count.getSingleResult()).intValue();

		Query query = getManager().createQuery(queryString);
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), params.get(entry.getValue()));
		}
		query.setFirstResult((pageable.getPageNumber()) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());

		@SuppressWarnings(SuppressWarningsParams.UNCHECKED)
		List<T> content = query.getResultList();

		return new DefaultPage<T>(content, pageable, total);
	}

}
