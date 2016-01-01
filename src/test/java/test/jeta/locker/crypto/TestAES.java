package test.jeta.locker.crypto;

import org.junit.Test;

import com.jeta.locker.crypto.AES;

public class TestAES {

	@Test
	public void test() {
		try {
			byte[] data = AES._encrypt("foo##", "12345foobar" );
			String sval = new String( data, "UTF-8");
			System.out.println( "encrypted: " + sval );
			System.out.println( "decrpyted: " + AES._decrypt("foo##", data ) );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
