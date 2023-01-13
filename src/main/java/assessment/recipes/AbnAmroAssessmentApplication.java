package assessment.recipes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({
		"classpath:application-local.properties"
})
public class AbnAmroAssessmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbnAmroAssessmentApplication.class, args);
	}

}
