package com.htfate.getwaycenter.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Log4j2
public class MyGatewayFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        URI uri = request.getURI();

        StringBuilder ri = new StringBuilder();
        String method = request.getMethodValue();
        ri.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~过滤器~~~~~~~~~~~~~~~~~~~~~~~~~~~~\r\n");
        ri.append("\tgetScheme: ").append(uri.getScheme()).append("\r\n");
        ri.append("\turl: ").append(uri.toString()).append("\r\n");
        ri.append("\tmethod: ").append(method).append("\r\n");
        ri.append("\thost: ").append(uri.getHost()).append("\r\n");
        ri.append("\tpath: ").append(uri.getPath()).append("\r\n");
        ri.append("\trawPath: ").append(uri.getRawQuery()).append("\r\n");
        ri.append("\tUser-Agent: ").append(request.getHeaders().get(HttpHeaders.USER_AGENT).get(0)).append("\r\n");
        ri.append("\tparams: ").append(request.getQueryParams().toString()).append("\r\n");
        /*if (true) {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().set(HttpHeaders.LOCATION,"http://baidu.com");

            return response.setComplete();
        }*/
        ri.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\r\n");
        log.info(ri);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -18;
    }
}
