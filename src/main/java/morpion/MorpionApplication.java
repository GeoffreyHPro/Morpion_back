package morpion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@SpringBootApplication
@EnableWebFluxSecurity
@OpenAPIDefinition
public class MorpionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MorpionApplication.class, args);
	}

}
