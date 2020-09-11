package com.asavin.hello.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {

    public CORSFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        Map<String, String> headers = Collections
                .list(httpRequest.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, httpRequest::getHeader));

        for(String s:headers.keySet()){
            System.out.println(s+"   "+headers.get(s));
        }
        System.out.println(httpRequest.getMethod());
        //doFilter
        chain.doFilter(httpRequest, response);
    }


//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) res;
//        HttpServletRequest request = (HttpServletRequest) req;
////        HttpServletResponse responseToCache = new ContentCachingResponseWrapper(response);
//
////        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
////
////
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, x-auth-token, origin, content-type, accept");
////
//        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            response.setStatus(HttpServletResponse.SC_OK);
//        } else {
//            filterChain.doFilter(request,response);
//        }
//
//
//            HttpServletResponseCopier responseCopier = new HttpServletResponseCopier((HttpServletResponse) response);

//            StringBuilderWriter branchedWriter = new org.apache.commons.io.output.StringBuilderWriter();
//            try {
//                chain.doFilter(request, wrapResponseForLogging(response, branchedWriter));
//            } finally {
//                System.out.println("Response: " + branchedWriter);
//            }
//
////            try {
////
////                System.out.println((
////                        new ContentCachingResponseWrapper(response).getContentAsByteArray().length));
//
////            responseWrapper.copyBodyToResponse();
////            responseWrapper.copyBodyToResponse();
//
//            filterChain.doFilter(request, response);
//
//            System.out.println(getResponseData(responseToCache));


//        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
//        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
//
//        try {
//            filterChain.doFilter(requestWrapper, responseWrapper);
//        } finally {
//            Iterator<String> iter = request.getHeaderNames().asIterator();
//            while(iter.hasNext())
//            {
//                String name = iter.next();
//                System.out.println(name+" "+request.getHeader(name));
//            }
//            String requestBody = new String(requestWrapper.getContentAsByteArray());
//            System.out.println("Request body: {}"+ requestBody);
//
//            String responseBody = new String(responseWrapper.getContentAsByteArray());
//            System.out.println("Response body: {}"+ responseBody);
//
//            responseWrapper.copyBodyToResponse();
//        }
//    }




    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}