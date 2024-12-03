package com.sparta.msa_exam.gateway.filter;
import com.sparta.msa_exam.gateway.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();

            // 경로별로 필터 적용 여부 결정
            if (path.startsWith("/auth/sign-up") || path.startsWith("/auth/sign-in")) {
                // 필터를 거치지 않고 다음 체인으로
                return chain.filter(exchange);
            }

            // JWT 토큰 검증 로직
            String token = extractToken(exchange);
            if (token == null || !jwtUtil.validateToken(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // "Bearer " 제거
        }
        return null;
    }

    public static class Config {
        // Custom configuration if needed
    }
}
