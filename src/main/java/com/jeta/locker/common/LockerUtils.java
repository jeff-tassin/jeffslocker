package com.jeta.locker.common;

import java.util.UUID;

public class LockerUtils {
	
	private static String CHARS = "123456789ABCDEFGHIJKLMNPQRSTUVWXYZabcdefghijklmnpqrstuvwxyz";
	private static String SYMBOLS_AND_CHARS = "{|}~!#$%&()*+,-.:;<=>?@[]^_" + CHARS;
			
	public static String generateId() {
		return UUID.randomUUID().toString();
	}
		
	public static char randomCharacter(boolean includeSymbols) {
		if ( includeSymbols ) {
			int ival = (int)(Math.random()*SYMBOLS_AND_CHARS.length());
			return SYMBOLS_AND_CHARS.charAt(ival);
		} else {
			int ival = (int)(Math.random()*CHARS.length());
			return CHARS.charAt(ival);
		}	
	}
	
	/**
	 * Generates a random alpha-numeric character string of length N
	 * @param N the length of the string
	 * @return
	 */
	public static String generateRandomCharacters( int N ) {
		return generateRandomCharacters( N, false );
	}
	
	public static String generateRandomCharacters( int N, boolean includeSymbols ) {
		StringBuilder sbuilder = new StringBuilder();
		for( int index=0; index < N; index++ ) {
			sbuilder.append( randomCharacter(includeSymbols) );
		}
		return sbuilder.toString();
	}
	
	public static boolean isDebug() {
		return "true".equals(System.getProperty("locker.debug"));
	}
	
	
	
}
