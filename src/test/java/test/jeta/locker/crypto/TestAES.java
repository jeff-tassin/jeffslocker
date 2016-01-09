package test.jeta.locker.crypto;

import org.junit.Test;

import com.jeta.locker.crypto.AES;

public class TestAES {

	@Test
	public void test() {
		try {
			String salt = "salt";
			String pepper = "pepper";
			String iv = "15";
			int keysize = 128;
			String password = "foo##";
			byte[] data = AES._encrypt( keysize, salt, iv, pepper, password, "12345foobar" );
			String sval = new String( data, "UTF-8");
			System.out.println( "encrypted: " + sval );
			System.out.println( "decrpyted: " + AES._decrypt(keysize, salt, iv, pepper, password, data ) );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
