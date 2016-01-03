package test.jeta.locker.type.password;

import java.awt.event.ActionListener;

import org.junit.Test;

import com.jeta.locker.type.password.PasswordController;

public class TestPasswordView {

	@Test
	public void testGeneratePassword() {
		ActionListener listener = new PasswordController.GeneratePasswordAction();
		listener.actionPerformed(null);
	}
}
