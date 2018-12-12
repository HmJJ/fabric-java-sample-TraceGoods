package com.springboot.basic.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DefaultPage<T> implements Page<T>,Serializable {

	private static final long serialVersionUID = 4646700011671998980L;
	
	private final List<T> content = new ArrayList<T>();
	private final PageRequest pageable;
	private final long total;
	
	public DefaultPage(List<T> content, PageRequest pageable, long total) {
		
		if(null == content) {
			throw new IllegalArgumentException("Content must not be null!");
		}
		
		this.content.addAll(content);
		this.total = total;
		this.pageable = pageable;
	}
	
	public DefaultPage(List<T> content) {
		this(content, null, null == content ? 0 : content.size());
	}

	@Override
	public int getNumber() {
		return pageable == null ? 0 : pageable.getPageNumber();
	}

	@Override
	public int getSize() {
		return pageable == null ? 0 : pageable.getPageSize();
	}

	@Override
	public int getTotalPages() {
		return getSize() == 0 ? 0 : (int)Math.ceil((double)total/(double)getSize());
	}

	@Override
	public int getNumberOfElements() {
		return content.size();
	}

	@Override
	public long getTotalElements() {
		return total;
	}

	@Override
	public boolean hasPreviousPage() {
		return getNumber() > 0;
	}

	@Override
	public boolean isFirstPage() {
		return !hasPreviousPage();
	}

	@Override
	public boolean hasNextPage() {
		return (getNumber()+1)*getSize() < total;
	}

	@Override
	public boolean isLastPage() {
		return !hasNextPage();
	}

	@Override
	public Iterator<T> iterator() {
		return content.iterator();
	}

	@Override
	public List<T> getContent() {
		return Collections.unmodifiableList(content);
	}

	@Override
	public boolean hasContent() {
		return !content.isEmpty();
	}

}
