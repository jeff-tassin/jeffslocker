package com.jeta.locker.common;

import java.io.InputStream;

public class FileUtils {

	public static void close( InputStream is ) {
		try {
			if ( is != null ) {
				is.close();
			}
		} catch( Exception e ) {
			// ignore
		}
	}
}
