package com.example.gateway;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
//import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerResponse;

@SpringBootConfiguration
@EnableAutoConfiguration
//@Configuration
//@SpringBootApplication
public class Application {
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder, ThrottleGatewayFilterFactory throttle) {
		return builder.routes()
				.route(r -> r.path("/test01").uri("http://localhost:8000/supu"))

				.route(r -> r.path("/supu").uri("http://localhost:8000/supu"))

				.route(r -> r.path("/image/webp").addResponseHeader("X-TestHeader", "baz").uri("http://httpbin.org:80"))

				.route(r -> r.order(-1).host("**.throttle.org").and().path("/get")
						.filter(throttle.apply(1, 1, 10, TimeUnit.SECONDS)).uri("http://www.krakend.io/"))
				.build();
	}

	@Bean
	public ThrottleGatewayFilterFactory throttleWebFilterFactory() {
		return new ThrottleGatewayFilterFactory();
	}


	@Bean
	public RouterFunction<ServerResponse> testFunRouterFunction() {
		RouterFunction<ServerResponse> route = RouterFunctions.route(
				RequestPredicates.path("/testfun"),
				request -> ServerResponse.ok().body(BodyInserters.fromObject("hello")));
		return route;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}