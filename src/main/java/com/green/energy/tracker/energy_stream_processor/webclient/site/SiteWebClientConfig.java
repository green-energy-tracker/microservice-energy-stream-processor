package com.green.energy.tracker.energy_stream_processor.webclient.site;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class SiteWebClientConfig {

    @Value("${spring.client.site-sensor-management.url}")
    private String clientSiteSensorManagementUrl;

    @Value("${spring.client.site-sensor-management.version}")
    private String clientSiteSensorManagementVersion;

    @Bean
    public SiteWebClient siteServiceClient() {
        var webClient = WebClient.create(clientSiteSensorManagementUrl+"/"+clientSiteSensorManagementVersion+"/site-management/site");
        var factory = HttpServiceProxyFactory.builder().exchangeAdapter(WebClientAdapter.create(webClient)).build();
        return factory.createClient(SiteWebClient.class);
    }
}
