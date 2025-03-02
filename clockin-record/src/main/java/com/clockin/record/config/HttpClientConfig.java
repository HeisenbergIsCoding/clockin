package com.clockin.record.config;

import com.clockin.record.client.AuthServiceClient;
import com.clockin.record.service.WorkConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * HTTP 客戶端配置
 */
@Slf4j
@Configuration
public class HttpClientConfig {

    @Value("${clockin.auth.baseUrl}")
    private String authBaseUrl;
    
    @Value("${clockin.admin.baseUrl}")
    private String adminBaseUrl;

    /**
     * 認證服務客戶端
     *
     * @return AuthServiceClient
     */
    @Bean
    public AuthServiceClient authServiceClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(authBaseUrl)
                .requestInterceptor((request, body, execution) -> {
                    // 從當前請求中獲取 Authorization 頭
                    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    if (attributes != null) {
                        HttpServletRequest servletRequest = attributes.getRequest();
                        String authHeader = servletRequest.getHeader("Authorization");
                        
                        if (authHeader != null && !authHeader.isEmpty()) {
                            // 將 Authorization 頭傳遞給 HTTP 請求
                            request.getHeaders().add("Authorization", authHeader);
                            log.debug("HTTP 客戶端添加 Authorization 頭: {}", authHeader);
                        }
                    }
                    return execution.execute(request, body);
                })
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        
        return factory.createClient(AuthServiceClient.class);
    }
    
    /**
     * 工作配置服務客戶端
     *
     * @return WorkConfigClient
     */
    @Bean
    public WorkConfigClient workConfigClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(adminBaseUrl)
                .requestInterceptor((request, body, execution) -> {
                    // 從當前請求中獲取 Authorization 頭
                    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    if (attributes != null) {
                        HttpServletRequest servletRequest = attributes.getRequest();
                        String authHeader = servletRequest.getHeader("Authorization");
                        
                        if (authHeader != null && !authHeader.isEmpty()) {
                            // 將 Authorization 頭傳遞給 HTTP 請求
                            request.getHeaders().add("Authorization", authHeader);
                            log.debug("HTTP 客戶端添加 Authorization 頭: {}", authHeader);
                        }
                    }
                    return execution.execute(request, body);
                })
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        
        return factory.createClient(WorkConfigClient.class);
    }
}
