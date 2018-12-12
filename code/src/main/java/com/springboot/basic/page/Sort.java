package com.springboot.basic.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Sort implements Iterable<Sort.Order>,Serializable {

	private static final long serialVersionUID = 5737186511678863905L;
	public static final Direction DEFAULT_DIRECTION = Direction.ASC;
	
	private final List<Order> orders;
	
	public Sort(Order... orders) {
		this(Arrays.asList(orders));
	}
	
	public Sort(List<Order> orders) {
		if(null == orders || orders.isEmpty()) {
			try {
				throw new IllegalAccessException("You have to provide at least one sort property to sort by!");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		this.orders = orders;
	}
	
	public Sort(String... properties) {
		this(DEFAULT_DIRECTION, properties);
	}

	public Sort(Direction direction, String... properties) {
		this(direction, properties == null ? new ArrayList<String>() : Arrays.asList(properties));
	}
	
	public Sort(Direction direction, List<String> properties) {
		if(properties == null || properties.isEmpty()) {
			try {
				throw new IllegalAccessException("You have to provide at least one sort property to sort by!");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		this.orders = new ArrayList<Order>(properties.size());
		for(String property : properties) {
			this.orders.add(new Order(direction, property));
		}
	}
	
	public Sort and(Sort sort) {
		if(sort == null) {
		    return this;
		}
		ArrayList<Order> these = new ArrayList<Order>(this.orders);
		for(Order order : sort) {
			these.add(order);
		}
		return new Sort(these);
	}
	
	public Order getOrderFor(String property) {
		for(Order order : this) {
			if(order.getProperty().equals(property)) {
				return order;
			}
		}
		return null;
	}
	
	public Iterator<Order> iterator(){
		return this.orders.iterator();
	}
	
	
	public static enum Direction {
		ASC,
		DESC
	}
	
	public static class Order implements Serializable {
		private static final long serialVersionUID = -5737186511678863905L;
		
		private final Direction direction;
		private final String property;
		
		public Order(String property) {
			this(DEFAULT_DIRECTION, property);
		}
		
		public Order(Direction direction, String property) {
			if(StringUtils.isBlank(property)) {
				try {
					throw new IllegalAccessException("Property must not null or empty!");
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
			this.direction = direction == null ? DEFAULT_DIRECTION : direction;
			this.property = StringUtils.trim(property);
		}
		
		public Direction getDirection() {
			return direction;
		}
		
		public String getProperty() {
			return property;
		}
		
	}
	
}
