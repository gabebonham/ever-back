package hedge.ever.impl.email.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;


@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender javaMailSender(Environment env) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        String host = getRequiredProperty(env, "spring.mail.host");
        int port = Integer.parseInt(getRequiredProperty(env, "spring.mail.port"));

        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(env.getProperty("spring.mail.username", ""));
        mailSender.setPassword(env.getProperty("spring.mail.password", ""));

        Properties props = new Properties();
        props.put("mail.smtp.auth",
                env.getProperty("spring.mail.properties.mail.smtp.auth", "true"));
        props.put("mail.smtp.starttls.enable",
                env.getProperty("spring.mail.properties.mail.smtp.starttls.enable", "true"));

        mailSender.setJavaMailProperties(props);
        return mailSender;
    }

    private String getRequiredProperty(Environment env, String key) {
        String value = env.getProperty(key);
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException(
                    "Required mail property '" + key + "' is missing");
        }
        return value;
    }
}