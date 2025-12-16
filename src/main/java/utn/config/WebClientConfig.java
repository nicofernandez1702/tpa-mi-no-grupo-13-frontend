package utn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${metamapa.estatica.url}")
    private String estaticaServiceUrl;

    @Bean
    public WebClient estaticaWebClient() {
        return WebClient.builder()
                .baseUrl(estaticaServiceUrl)
                .build();
    }
}
