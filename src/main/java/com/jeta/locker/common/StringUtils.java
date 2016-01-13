package com.jeta.locker.common;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {



	/**
	 * Returns the last value in a path.
	 * For example:
	 *   url=/meetingdata/sval
	 *   Returns sval.
	 *   
	 *   If the path does not contain a separator character (\ or /), then the 
	 *   whole path is returned.
	 **/
	public static String getTrailingString( String path ) {
		if ( path == null || path.length() == 0 ) {
			return null;
		}

		for( int index=path.length()-1; index >= 0; index-- ) {
			char c = path.charAt(index);
			if ( c == '\\' || c == '/' ) {
				return path.substring(index+1);
			}
		}
		return path;
	}

	/**
	 * Returns the last number in a path.
	 * For example:
	 *   url=/meetingdata/11
	 * This method will return:  11
	 * If a number is not found, -1 is returned.
	 */
	public static long getTrailingNumber( String path ) {
		if ( path == null || path.length() == 0 ) {
			return -1;
		}
		Pattern pattern = Pattern.compile("[\\\\/](\\d+)$");
		Matcher matcher = pattern.matcher( path );
		long id=-1;
		if(matcher.find()) {
			id = Long.parseLong(matcher.group(1));
		}
		if ( id == -1 ) {
			try {
				id = Long.parseLong( path );
			} catch( Exception e ) {
				// ignore
			}
		}
		return id;
	}

	/**
	 * Removes any leading or trailing slashes from the specified path.
	 */
	public static String trimPath( String path ) {
		return trimPathRight( trimPathLeft( path ) );
	}

	/**
	 * Removes any trailing slashes from the specified path 
	 */
	public static String trimPathRight(String path) {
		if (path == null || path.length() == 0) {
			return path;
		}

		StringBuilder result = null;
		boolean checkslash = true;
		for (int index = path.length() - 1; index >= 0; index--) {
			char pathchar = path.charAt(index);
			if (index == path.length() - 1) {
				if (pathchar == '/' || pathchar == '\\') {
					result = new StringBuilder();
				} else {
					return path;
				}
			}
			if (checkslash) {
				if (pathchar == '\\' || pathchar == '/') {
					continue;
				}
				checkslash = false;
			}
			result.insert(0, pathchar);
		}
		return result.toString();
	}


	/**
	 * Removes any leading slashes from the specified path 
	 */
	public static String trimPathLeft(String path) {
		if (path == null || path.length() == 0) {
			return path;
		}

		StringBuilder result = null;
		boolean checkslash = true;
		for (int index = 0; index < path.length(); index++) {
			char pathChar = path.charAt(index);
			if (index == 0) {
				if (pathChar == '/' || pathChar == '\\') {
					result = new StringBuilder();
				} else {
					return path;
				}
			}
			if (checkslash) {
				if (pathChar == '\\' || pathChar == '/') {
					continue;
				}
				checkslash = false;
			}
			result.append(pathChar);
		}
		return result.toString();
	}

	/**
	 * Builds a file system path by concatenating list of args using the specified separator character.  Automatically trims
	 * any separators from the args list, if they are present.
	 * @param separator
	 * @param args
	 * @return the path
	 */
	public static String buildFilePath( Object... args ) {
		return buildPath( File.separatorChar, args );
	}

	/**
	 * Builds a url path by concatenating list of args using the specified separator character.  Automatically trims
	 * any separators from the args list, if they are present.
	 * @param separator
	 * @param args
	 * @return the path
	 */
	public static String buildUrlPath( Object... args ) {
		return buildPath( '/', args );
	}

	/**
	 * Builds a path by concatenating list of args using the specified separator character.  Automatically trims
	 * any separators from the args list, if they are present.
	 * @param separator
	 * @param args
	 * @return the path
	 */
	public static String buildPath( char separator, Object... args ) {
		StringBuilder builder = new StringBuilder();
		for( Object arg : args ) {
			if ( arg != null && arg.toString().length() > 0 ) {
				if ( builder.length() > 0 ) {
					builder.append( separator );
					builder.append( StringUtils.trimPath( String.valueOf(arg) ) );
				} else {
					builder.append( StringUtils.trimPathRight( String.valueOf(arg) ) );
				}
			}
		}
		return builder.toString();
	}

	/**
	 * This method safely converts a string to lower case.  If string is null, an empty string is returned.
	 */
	public static String safeToLowerCase(String parameter) {
		return parameter == null ? "" : parameter.toLowerCase();
	}

	/**
	 * Safely parses a string parameter to a long.  If the string is not a
	 * number or is null, the defaultVal is returned. Otherwise the parsed long value is
	 * returned.
	 * @param sVal the string to convert to long.
	 * @param defaultVal the value to return if a parsing error occurs.
	 */
	public static long safeParseLong(String sVal, long defaultVal ) {
		if ( sVal == null ) {
			return defaultVal;
		}
		try {
			return Long.parseLong( sVal );
		} catch( Exception e ) {
			// ignore
		}
		return defaultVal;
	}

	/**
	 * Safely parses a string parameter to an int.  If the string is not a
	 * number or is null, the defaultVal is returned. Otherwise the parsed integer is
	 * returned.
	 * @param sVal the string to convert to long.
	 * @param defaultVal the value to return if a parsing error occurs.
	 */
	public static int safeParseInt(String sVal, int defaultVal) {
		if ( sVal == null ) {
			return defaultVal;
		}
		try {
			return Integer.parseInt( sVal );
		} catch( Exception e ) {
			// ignore
		}
		return defaultVal;
	}


	/**
	 * Safely removes whitespace from specified string parameter. If parameter is null, empty string is returned.
	 * @param parameter
	 * @return
	 */
	public static String safeTrim(String parameter) {
		return parameter == null ? "" : parameter.trim();
	}
	

}
