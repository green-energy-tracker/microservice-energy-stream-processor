package com.green.energy.tracker.energy_stream_processor.webclient.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class UserManagementWebClientConfig {

    @Value("${spring.client.user-management.url}")
    private String clientUserManagementUrl;

    @Value("${spring.client.user-management.version}")
    private String clientUserManagementVersion;

    @Bean
    public UserManagementWebClient userManagementServiceClient() {
        WebClient webClient = WebClient.create(
                clientUserManagementUrl+"/"+clientUserManagementVersion+"/user-management"
        );
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder()
                .exchangeAdapter(WebClientAdapter.create(webClient))
                .build();
        return factory.createClient(UserManagementWebClient.class);
    }
}
