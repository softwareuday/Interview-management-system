//package com.ims.fullstack.config;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import java.io.IOException;
//
//@Slf4j
//@Component
//public class RequestLoggerFilter implements Filter {
//
//    @Override
//    public void doFilter(
//            ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest req = (HttpServletRequest) request;
//
//        log.info("➡️ REQUEST: {} {}", req.getMethod(), req.getRequestURI());
//        log.info("Headers: {}", req.getHeaderNames());
//        log.info("Query Params: {}", req.getQueryString());
//
//        chain.doFilter(request, response);
//    }
//}


package com.ims.fullstack.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Slf4j
@Component
public class RequestLoggerFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        log.info("➡️ REQUEST: {} {}", req.getMethod(), req.getRequestURI());
        chain.doFilter(request, response);
    }
}