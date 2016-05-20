package com.zczczy.leo.microwarehouse.rest;

import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientErrorHandling;
import org.androidannotations.rest.spring.api.RestClientHeaders;
import org.androidannotations.rest.spring.api.RestClientRootUrl;
import org.androidannotations.rest.spring.api.RestClientSupport;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Created by Leo on 2016/3/2.
 * http://124.254.56.58:8007/
 * http://192.168.0.198:8002/
 * http://appapia.86fuwuwang.com/
 */
@Rest(rootUrl = "http://218.61.203.50:8002/", requestFactory = MyOkHttpClientHttpRequestFactory.class, interceptors = {MyInterceptor.class},
        converters = {StringHttpMessageConverter.class, MappingJackson2HttpMessageConverter.class, FormHttpMessageConverter.class, ByteArrayHttpMessageConverter.class},
        responseErrorHandler = MyResponseErrorHandlerBean.class
)
public interface MyRestClient extends RestClientRootUrl, RestClientSupport, RestClientHeaders, RestClientErrorHandling {



}
