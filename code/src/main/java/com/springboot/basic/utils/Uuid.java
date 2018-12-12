package com.springboot.basic.utils;

import java.util.UUID;

public class Uuid {
	
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString().replaceAll("-","");
		return uuid;
	}
	
}
