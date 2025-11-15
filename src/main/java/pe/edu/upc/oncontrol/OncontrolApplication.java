package pe.edu.upc.oncontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

/**
 * OncontrolApplication
 *
 * @summary
 * The main class of the Learning Center Platform application.
 * It is responsible for starting the Spring Boot application.
 * It also enables JPA auditing.
 *
 * @since 1.0
 */

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class OncontrolApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(OncontrolApplication.class, args);
    }

}
