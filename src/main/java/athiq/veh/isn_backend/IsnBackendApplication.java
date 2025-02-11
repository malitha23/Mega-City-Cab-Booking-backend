package athiq.veh.isn_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IsnBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(IsnBackendApplication.class, args);
	}

}
