package utn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${metamapa.estatica.url}")
    private String estaticaServiceUrl;

    private static final int MAX_SIZE = 20 * 1024 * 1024; // 20MB

    @Bean
    public WebClient estaticaWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(estaticaServiceUrl)
                .exchangeStrategies(
                        ExchangeStrategies.builder()
                                .codecs(c -> c.defaultCodecs()
                                        .maxInMemorySize(MAX_SIZE))
                                .build()
                )
                .build();
    }

    @Bean
    public WebClient publicApiWebClient(WebClient.Builder builder) {
        return builder
                .exchangeStrategies(
                        ExchangeStrategies.builder()
                                .codecs(c -> c.defaultCodecs()
                                        .maxInMemorySize(MAX_SIZE))
                                .build()
                )
                .build();
    }
}
