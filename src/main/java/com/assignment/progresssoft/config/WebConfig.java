package com.assignment.progresssoft.config;

import com.assignment.progresssoft.security.component.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring configuration class for customizing Web MVC behavior.
 * <p>
 * Registers global interceptors such as {@link PermissionInterceptor} for
 * pre-processing incoming HTTP requests.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final PermissionInterceptor permissionInterceptor;

    /**
     * Constructs a new {@code WebConfig} with the provided {@link PermissionInterceptor}.
     *
     * @param permissionInterceptor the custom interceptor for permission checking
     */
    @Autowired
    public WebConfig(PermissionInterceptor permissionInterceptor) {
        this.permissionInterceptor = permissionInterceptor;
    }

    /**
     * Adds the {@link PermissionInterceptor} to the interceptor registry.
     * <p>
     * The interceptor is applied to all request paths starting with <code>/documents/</code>.
     *
     * @param registry the {@link InterceptorRegistry} to register interceptors with
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/documents/**");
    }
}
