package com.jeta.locker.common;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

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

	public static void close(OutputStream os) {
		// TODO Auto-generated method stub
		try {
			if ( os != null ) {
				os.close();
			}
		} catch( Exception e ) {
			// ignore
		}
	}

	public static boolean isDirectory(String path) {
		return new File(path).isDirectory();
	}
	public static boolean isFile(String path) {
		return new File(path).isFile();
	}
}
