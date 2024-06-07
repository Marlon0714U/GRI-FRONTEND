package co.edu.uniquindio.gri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Clase StartWebApplication
 */
@EnableJpaAuditing
@SpringBootApplication
public class StartWebApplication {
	
	/**
	 * Método principal para ejecutar SpringBoot.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(StartWebApplication.class, args);
	}

}
