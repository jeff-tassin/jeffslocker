package com.jeta.locker.common;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.util.UUID;

public class LockerUtils {
	
	public static String generateId() {
		return UUID.randomUUID().toString();
	}
	
}
