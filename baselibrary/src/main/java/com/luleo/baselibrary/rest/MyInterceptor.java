package com.luleo.baselibrary.rest;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * Created by leo on 2015/6/2.
 */
@EBean
public class MyInterceptor implements ClientHttpRequestInterceptor {

    @RootContext
    Context context;

//    @StringRes
//    String no_net;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] data,
                                        ClientHttpRequestExecution execution) throws IOException {
        return execution.execute(request, data);
    }
}
