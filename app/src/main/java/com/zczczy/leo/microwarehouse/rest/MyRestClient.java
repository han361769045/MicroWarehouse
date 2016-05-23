package com.zczczy.leo.microwarehouse.rest;

import com.zczczy.leo.microwarehouse.model.Banner;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.LoginInfo;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientErrorHandling;
import org.androidannotations.rest.spring.api.RestClientHeaders;
import org.androidannotations.rest.spring.api.RestClientRootUrl;
import org.androidannotations.rest.spring.api.RestClientSupport;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

/**
 * Created by Leo on 2016/3/2.
 * http://124.254.56.58:8007/
 * http://192.168.0.198:8002/
 * http://appapia.86fuwuwang.com/
 */
@Rest(rootUrl = "http://192.168.0.198:8018/", requestFactory = MyOkHttpClientHttpRequestFactory.class, interceptors = {MyInterceptor.class},
        converters = {StringHttpMessageConverter.class, MappingJackson2HttpMessageConverter.class, FormHttpMessageConverter.class, ByteArrayHttpMessageConverter.class},
        responseErrorHandler = MyResponseErrorHandlerBean.class
)
public interface MyRestClient extends RestClientRootUrl, RestClientSupport, RestClientHeaders, RestClientErrorHandling {

    /**
     * @param UserName  登录账号
     * @param UserPw    登录密码
     * @param LoginType 登录类型（1：普通会员，2：VIP用户）
     * @param Kbn       设备类型（1：Android,2:Ios）
     * @return
     */
    @Get("api/Content/Login?UserName={UserName}&UserPw={UserPw}&LoginType={LoginType}&Kbn={Kbn}")
    BaseModelJson<LoginInfo>login(@Path String UserName, @Path String UserPw, @Path String LoginType, @Path String Kbn);

    /**
     * 查询App首页Banner
     */
    @Get("api/Content/GetHomeBanner")
    BaseModelJson<List<Banner>> getHomeBanner();


}
