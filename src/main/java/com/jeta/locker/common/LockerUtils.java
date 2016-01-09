package com.jeta.locker.common;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.util.UUID;

public class LockerUtils {
	
	public static String generateId() {
		return UUID.randomUUID().toString();
	}
	
		
	public static char randomCharacter() {
		int ival = Math.round( (float)Math.random()*2.0f);
		switch( ival) {
		case 0: // digits
			int dval = Math.round( (float)Math.random()*9.0f);
			return (char)(48+dval);
		case 1: // upper case
			int cval = Math.round( (float)Math.random()*25.0f);
			return (char)(65+cval);
		default: // lower case
			cval = Math.round( (float)Math.random()*25.0f);
			return (char)(97+cval);		
		}	
	}
	
	/**
	 * Generates a random alpha-numeric character string of length N
	 * @param N the length of the string
	 * @return
	 */
	public static String generateRandomCharacters( int N ) {
		StringBuilder sbuilder = new StringBuilder();
		for( int index=0; index < N; index++ ) {
			sbuilder.append( randomCharacter() );
		}
		return sbuilder.toString();
	}
	
}
