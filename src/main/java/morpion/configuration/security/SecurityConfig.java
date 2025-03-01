package morpion.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Component
    public class AuthConverter implements ServerAuthenticationConverter {

        @Override
        public Mono<Authentication> convert(ServerWebExchange exchange) {
            return Mono.justOrEmpty(
                    exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                    .filter(s -> s.startsWith("Bearer "))
                    .map(s -> s.substring(7))
                    .map(s -> new BearerToken(s));
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthConverter jwtAuthConverter,
            AuthManager jwtAuthManager) {

        AuthenticationWebFilter jwtFilter = new AuthenticationWebFilter(jwtAuthManager);
        jwtFilter.setServerAuthenticationConverter(jwtAuthConverter);

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange((auth) -> auth
                        .pathMatchers("/webjars/**").permitAll()
                        .pathMatchers("/swagger-ui.html").permitAll()
                        .pathMatchers("/swagger-ui/**").permitAll()
                        .pathMatchers("/v3/**").permitAll()
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers("/user/**").authenticated())
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}