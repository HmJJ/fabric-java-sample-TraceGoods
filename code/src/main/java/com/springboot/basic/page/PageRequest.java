package com.springboot.basic.page;

import java.io.Serializable;

public class PageRequest implements Serializable {

	private static final long serialVersionUID = 7818462349160828248L;
	
	private final int page;
	private final int size;
	
	public PageRequest(int page, int size) {
		if(0>page) {
			throw new IllegalArgumentException("Page index must not be less than zero!");
		}
		
		if(0>=size) {
			throw new IllegalArgumentException("Page index must not be less than zero!");
		}
		
		this.page = page;
		this.size = size;
	}
	
	public int getPageSize() {
		return size;
	}
	
	public int getPageNumber() {
		return page;
	}
	
	public int getOffset() {
		return page*size;
	}

}
