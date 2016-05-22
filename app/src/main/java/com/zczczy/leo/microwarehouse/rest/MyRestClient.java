package com.zczczy.leo.microwarehouse.rest;

import com.zczczy.leo.microwarehouse.model.AdvertModel;
import com.zczczy.leo.microwarehouse.model.AreaModel;
import com.zczczy.leo.microwarehouse.model.BannerModel;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.CityModel;
import com.zczczy.leo.microwarehouse.model.MemberInfoModel;
import com.zczczy.leo.microwarehouse.model.ProvinceModel;
import com.zczczy.leo.microwarehouse.model.ShippingAddressModel;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Field;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Part;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.RequiresHeader;
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
import java.util.Map;

/**
 * Created by Leo on 2016/3/2.
 * http://218.61.203.50:8018/
 * http://192.168.0.198:8002/
 * http://appapia.86fuwuwang.com/
 */
@Rest(rootUrl = "http://192.168.0.198:8018/", requestFactory = MyOkHttpClientHttpRequestFactory.class, interceptors = {MyInterceptor.class},
        converters = {StringHttpMessageConverter.class, MappingJackson2HttpMessageConverter.class, FormHttpMessageConverter.class, ByteArrayHttpMessageConverter.class},
        responseErrorHandler = MyResponseErrorHandlerBean.class
)
public interface MyRestClient extends RestClientRootUrl, RestClientSupport, RestClientHeaders, RestClientErrorHandling {

    /**
     * App用户登录
     *
     * @param UserName  用户名
     * @param UserPw    登陆密码或验证码
     * @param LoginType 登录类型（1：手机验证码登录，2：账号密码登录）
     * @param Kbn       设备类型(1:android,2:ios)
     * @return
     */
    @Get("api/Content/Login?UserName={UserName}&UserPw={UserPw}&LoginType={LoginType}&Kbn={Kbn}")
    BaseModelJson<String> login(@Path String UserName, @Path String UserPw, @Path int LoginType, @Path String Kbn);

    /**
     * 查询App首页Banner
     *
     * @return
     */
    @Get("api/Content/GetHomeBanner")
    BaseModelJson<List<BannerModel>> getHomeBanner();

    /**
     * 根据广告区分查询广告信息（1：首页广告，2：其他页面广告） 广告区分（1：首页广告，2：其他页面广告）
     *
     * @param kbn 根据广告区分查询广告信息（1：首页广告，2：其他页面广告） 广告区分（1：首页广告，2：其他页面广告）
     * @return
     */
    @Get("api/Content/GetAdvertByKbn?kbn={kbn}")
    BaseModelJson<List<AdvertModel>> getAdvertByKbn(@Path String kbn);

    /***
     * 查询省下拉数据
     *
     * @return
     */
    @Get("api/Content/GetAllProvinceList")
    BaseModelJson<List<ProvinceModel>> getAllProvinceList();

    /***
     * 查询市下拉数据
     *
     * @param ProvinceId 省ID
     * @return
     */
    @Get("api/Content/GetCityListByProvinceId?ProvinceId={ProvinceId}")
    BaseModelJson<List<CityModel>> getCityListByProvinceId(@Path String ProvinceId);


    /***
     * 查询区下拉数据
     *
     * @param CityId 市ID
     * @return
     */
    @Get("api/Content/GetAreaListByCityId?CityId={CityId}")
    BaseModelJson<List<AreaModel>> getAreaListByCityId(@Path String CityId);


    //==============================================================================================
    // 需要传入token

    /**
     * 查询用户信息
     *
     * @return
     */
    @Get("api/Member/GetMemberInfo")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModelJson<MemberInfoModel> getMemberInfo();

    /**
     * 查询收货地址
     *
     * @return
     */
    @Get("api/Member/GetMReceiptAddressListByUserInfoId")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModelJson<List<ShippingAddressModel>> getMReceiptAddressListByUserInfoId();

    /**
     * 修改收货地址
     *
     * @param model
     * @return
     * @see ShippingAddressModel
     */
    @Post("api/Member/UpdMReceiptAddress")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModel updMReceiptAddress(@Body ShippingAddressModel model);

    /**
     * 新增收货地址
     *
     * @param model
     * @return BaseModel
     * @see ShippingAddressModel
     */
    @Post("api/Member/AddMReceiptAddress")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModel addMReceiptAddress(@Body ShippingAddressModel model);

    /**
     * 设置默认收货地址
     *
     * @param MReceiptAddressId 收货地址主键ID
     * @return
     */
    @Post("api/Member/UpdDefaultReceiptAddress/{MReceiptAddressId}")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModel updDefaultReceiptAddress(@Path int MReceiptAddressId);

    /**
     * 删除收货地址
     *
     * @param MReceiptAddressId 收货地址主键ID
     * @return
     */
    @Post("api/Member/DelReceiptAddress/{MReceiptAddressId}")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModel delReceiptAddress(@Path int MReceiptAddressId);

    /**
     * 修改会员头像
     *
     * @param model 头像地址
     * @return
     */
    @Post("api/Member/UpdateMemberInfoImg")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModelJson<String> updateMemberInfoImg(@Body String model);

    /**
     * 根据收货地址ID查询单个收货地址信息
     *
     * @param MReceiptAddressId 收货地址ID
     * @return
     * @see ShippingAddressModel
     */
    @Get("api/Member/GetMReceiptAddressById/{MReceiptAddressId}")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModelJson<ShippingAddressModel> getMReceiptAddressById(@Path int MReceiptAddressId);

    /**
     * 根据收货地址ID查询单个收货地址信息
     *
     * @return
     * @see ShippingAddressModel
     */
    @Get("api/Member/GetUserDefaultAddress")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModelJson<ShippingAddressModel> getUserDefaultAddress();
}
