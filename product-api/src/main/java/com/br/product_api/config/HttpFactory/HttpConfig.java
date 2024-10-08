package com.br.product_api.config.HttpFactory;

import com.br.product_api.modules.sales.client.SalesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpConfig {

    @Value("${app-config.services.sales}")
    private String baseUrl;

    @Bean
    public SalesClient salesClient() {
        return HttpServiceProxyFactory.builderFor(
                WebClientAdapter.create(
                                WebClient
                                        .builder()
                                        .baseUrl(baseUrl)
                                        .build())
        ).build()
                .createClient(SalesClient.class);
    }
}
