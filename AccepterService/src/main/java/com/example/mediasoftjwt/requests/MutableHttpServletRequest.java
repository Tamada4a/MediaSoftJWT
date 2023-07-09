package com.example.mediasoftjwt.requests;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.*;


public class MutableHttpServletRequest extends HttpServletRequestWrapper {

    private final Map<String, String> customHeaders;


    public MutableHttpServletRequest(HttpServletRequest request){
        super(request);
        this.customHeaders = new HashMap<String, String>();
    }


    public void putHeader(String name, String value){
        this.customHeaders.put(name, value);
    }


    public String getHeader(String name) {

        String headerValue = customHeaders.get(name);

        if (headerValue != null){
            return headerValue;
        }

        return ((HttpServletRequest) getRequest()).getHeader(name);
    }


    @Override
    public Enumeration<String> getHeaders(String name) {
        Set<String> headerValues = new HashSet<>();
        headerValues.add(customHeaders.get(name));

        Enumeration<String> underlyingHeaderValues = ((HttpServletRequest) getRequest()).getHeaders(name);
        while (underlyingHeaderValues.hasMoreElements()) {
            headerValues.add(underlyingHeaderValues.nextElement());
        }

        return Collections.enumeration(headerValues);
    }
}
