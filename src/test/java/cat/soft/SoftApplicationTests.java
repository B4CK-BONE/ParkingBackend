package cat.soft;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SoftApplicationTests {

	@Test
	void contextLoads() {
		StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
		standardPBEStringEncryptor.setAlgorithm("PBEWithMD5AndDES");
		standardPBEStringEncryptor.setPassword("TEST_KEY");
		String encodedPass = standardPBEStringEncryptor.encrypt("12345678");
		System.out.println("Encrypted Password for admin is : "+encodedPass);
	}

}
