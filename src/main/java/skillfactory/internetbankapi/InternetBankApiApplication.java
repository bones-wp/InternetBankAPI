package skillfactory.internetbankapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class InternetBankApiApplication {
	static final Logger log = LoggerFactory.getLogger(InternetBankApiApplication.class);


	public static void main(String[] args) {
		log.info("Начало сборки контекста " + new Date());
		SpringApplication.run(InternetBankApiApplication.class, args);
		log.info("Начало работы приложения " + new Date());

	}

}
