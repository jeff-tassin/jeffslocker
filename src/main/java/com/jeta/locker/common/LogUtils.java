package com.jeta.locker.common;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogUtils {

	public static void debug( final String msg ) {
		System.out.println( msg );
	}

	public static void debug(String msg, Throwable err) {
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		err.printStackTrace(writer);
		debug( msg );
		debug( sw.toString() );
	}
	
	
	public static void error(String msg, Exception e) {
		debug( msg, e );
	}
}
