package com.jeta.locker.common;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	public static final String BACKUP_DATE_FORMAT = "MM_dd_yyyy_HH_mm_ss";
	public static String backup( String srcFilePath, String targetDir ) throws LockerException {
		File srcFile = new File(srcFilePath);
		if ( !srcFile.isFile() ) {
			throw new LockerException( "Invalid file path.");
		}
		File destDir = new File(targetDir);
		if ( !destDir.isDirectory() ) {
			throw new LockerException( "Invalid destination directory.");
		}
		
		String fname = srcFile.getName();
		if ( fname.endsWith(".jlocker")) {
			fname = fname.substring(0, fname.length() - ".jlocker".length() );
		}

		SimpleDateFormat format = new SimpleDateFormat( BACKUP_DATE_FORMAT );
		String backupFile = fname + "." + format.format( new Date() ) + ".jlocker";
		
		destDir = new File( StringUtils.buildFilePath(targetDir,backupFile) );
		try {
			Files.copy( srcFile.toPath(),destDir.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
			return destDir.getName();
		} catch( Exception e ) {
			throw LockerException.create(e);
		}
	}

}
