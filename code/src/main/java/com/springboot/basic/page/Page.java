package com.springboot.basic.page;

import java.util.Iterator;
import java.util.List;

public interface Page<T> extends Iterable<T> {
	
	int getNumber();
	
	int getSize();
	
	int getTotalPages();
	
	int getNumberOfElements();
	
	long getTotalElements();
	
	boolean hasPreviousPage();
	
	boolean isFirstPage();
	
	boolean hasNextPage();
	
	boolean isLastPage();
	
	Iterator<T> iterator();
	
	List<T> getContent();
	
	boolean hasContent();
	
}
