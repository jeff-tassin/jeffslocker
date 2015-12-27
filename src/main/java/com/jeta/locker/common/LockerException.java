package com.jeta.locker.common;

public class LockerException extends Exception {

	public LockerException(String msg ) {
		super(msg);
	}

	public static LockerException create(Exception e) {
		if ( e instanceof LockerException ) {
			return (LockerException)e;
		}
		return new LockerException( e.getMessage() );
	}
}
