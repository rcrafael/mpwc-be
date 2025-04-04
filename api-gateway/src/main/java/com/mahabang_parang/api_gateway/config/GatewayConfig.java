// package com.mahabang_parang.api_gateway.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.reactive.CorsWebFilter;
// import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
// import org.springframework.web.server.WebFilter;

// @Configuration
// public class GatewayConfig {

//      @Bean
//      public CorsWebFilter corsFilter() {
//           CorsConfiguration config = new CorsConfiguration();
//           config.setAllowCredentials(true);
//           config.addAllowedOrigin("http://localhost:4200");
//           config.addAllowedHeader("*");
//           config.addAllowedMethod("*");
//           config.addExposedHeader("Authorization");
          
//           UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//           source.registerCorsConfiguration("/**", config);
          
//           return new CorsWebFilter(source);
//      }

//      @Bean
//      public WebFilter addResponseHeadersFilter() {
//           return (exchange, chain) -> {
//                exchange.getResponse()
//                     .getHeaders()
//                     .add("Access-Control-Allow-Origin", "http://localhost:4200");
//                return chain.filter(exchange);
//           };
//      }

// }
