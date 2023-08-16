package cat.soft.src.oauth.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.ApplicationArguments;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableEncryptableProperties
@Component
public class PropertyEncryptConfig {

	@Autowired
	ApplicationArguments arguments;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String JASYPT_STRING_ENCRYPTOR = "jasyptStringEncryptor";

	@Value("${key}")
	String encryptKey;

	@Value("${jasypt.encryptor.algorithm}")
	private String algorithm;

	@Bean(JASYPT_STRING_ENCRYPTOR)
	public StringEncryptor stringEncryptor() {
		logger.info("encryptKey: " + encryptKey);
		logger.info("algorithm: " + algorithm);

		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(encryptKey);
		config.setAlgorithm(algorithm);
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);

		logger.info("end");

		return encryptor;
	}
}