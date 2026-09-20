package com.webarch.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class GatewayRoutesConfig {

    @Value("${services.user.url}")
    private String userServiceUrl;

    @Value("${services.product.url}")
    private String productServiceUrl;

    @Value("${services.order.url}")
    private String orderServiceUrl;

    @Value("${services.cart.url}")
    private String cartServiceUrl;

    @Value("${services.payment.url}")
    private String paymentServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return route("user_service")
                .route(req -> req.path().startsWith("/api/auth") || req.path().startsWith("/api/users") || req.path().startsWith("/api/reports") || req.path().startsWith("/.well-known"), http())
                .before(uri(userServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productServiceRoute() {
        return route("product_service")
                .route(req -> req.path().startsWith("/api/products"), http())
                .before(uri(productServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {
        return route("order_service")
                .route(req -> req.path().startsWith("/api/orders"), http())
                .before(uri(orderServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> cartServiceRoute() {
        return route("cart_service")
                .route(req -> req.path().startsWith("/api/carts"), http())
                .before(uri(cartServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> paymentServiceRoute() {
        return route("payment_service")
                .route(req -> req.path().startsWith("/api/payments"), http())
                .before(uri(paymentServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userDocsRoute() {
        return route("user_docs")
                .route(req -> req.path().startsWith("/v3/api-docs/user"), http())
                .before(rewritePath("/v3/api-docs/user", "/v3/api-docs"))
                .before(uri(userServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productDocsRoute() {
        return route("product_docs")
                .route(req -> req.path().startsWith("/v3/api-docs/product"), http())
                .before(rewritePath("/v3/api-docs/product", "/v3/api-docs"))
                .before(uri(productServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderDocsRoute() {
        return route("order_docs")
                .route(req -> req.path().startsWith("/v3/api-docs/order"), http())
                .before(rewritePath("/v3/api-docs/order", "/v3/api-docs"))
                .before(uri(orderServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> cartDocsRoute() {
        return route("cart_docs")
                .route(req -> req.path().startsWith("/v3/api-docs/cart"), http())
                .before(rewritePath("/v3/api-docs/cart", "/v3/api-docs"))
                .before(uri(cartServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> paymentDocsRoute() {
        return route("payment_docs")
                .route(req -> req.path().startsWith("/v3/api-docs/payment"), http())
                .before(rewritePath("/v3/api-docs/payment", "/v3/api-docs"))
                .before(uri(paymentServiceUrl))
                .build();
    }
}
