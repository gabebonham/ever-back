package hedge.ever.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
@MapperScan({"hedge.ever.repository.auth"})
@ComponentScan(basePackages = {
		"hedge.ever.controllers.auth",  // Controller module
		"hedge.ever.security.config",   // Security module (where SecurityConfig lives)
		"hedge.ever.security.filters",   // Security module (where SecurityConfig lives)
		"hedge.ever.security.services",   // Security module (where SecurityConfig lives)
		"hedge.ever.impl" // AuthService's module (if different)
})
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
