package test.jeta.locker.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;

import org.junit.Test;

import com.jeta.locker.common.LogUtils;
import com.jeta.locker.config.LockerProperties;

public class TestFile {

	@Test
	public void test() {
		try {
			/*
			File f = new File(LockerProperties.getDataFile());
			FileInputStream fis = new FileInputStream(f);
			byte[] data = new byte[(int)f.length()];
			fis.read(data);
			byte[] data64 = Base64.getDecoder().decode( data );
			LogUtils.debug("read bytes: " + f.length() );
			LogUtils.debug( "first 100 bytes: " + new String(data,0,100, "UTF-8"));
			LogUtils.debug( "base64 decoded first 100 bytes: " + new String(data64,0,100, "UTF-8"));
			*/

			
		} catch( Exception e ) {
			LogUtils.error( "Failed test file", e);
		}
	}
}
