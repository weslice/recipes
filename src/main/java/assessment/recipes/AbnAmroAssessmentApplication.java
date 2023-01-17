package assessment.recipes;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({
		"classpath:application-local.properties"
})
@OpenAPIDefinition(info = @Info(title = "Recipes API", version = "1.0", description = "REST API to insert, update, delete and filter recipes"))
public class AbnAmroAssessmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbnAmroAssessmentApplication.class, args);
	}

}
