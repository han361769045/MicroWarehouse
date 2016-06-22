package com.zczczy.leo.microwarehouse.rest;

import com.zczczy.leo.microwarehouse.model.AdvertModel;
import com.zczczy.leo.microwarehouse.model.AreaModel;
import com.zczczy.leo.microwarehouse.model.BannerModel;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.CartModel;
import com.zczczy.leo.microwarehouse.model.CityModel;
import com.zczczy.leo.microwarehouse.model.DealerApplyModel;
import com.zczczy.leo.microwarehouse.model.DepotModel;
import com.zczczy.leo.microwarehouse.model.GoodsCommentsModel;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.model.GoodsTypeModel;
import com.zczczy.leo.microwarehouse.model.LogisticsInfoModel;
import com.zczczy.leo.microwarehouse.model.MemberInfoModel;
import com.zczczy.leo.microwarehouse.model.NoticeInfoModel;
import com.zczczy.leo.microwarehouse.model.OrderCountModel;
import com.zczczy.leo.microwarehouse.model.OrderDetailModel;
import com.zczczy.leo.microwarehouse.model.OrderModel;
import com.zczczy.leo.microwarehouse.model.PagerResult;
import com.zczczy.leo.microwarehouse.model.ProvinceModel;
import com.zczczy.leo.microwarehouse.model.ShippingAddressModel;
import com.zczczy.leo.microwarehouse.model.TaskOrderModel;
import com.zczczy.leo.microwarehouse.model.UpdateAppModel;

import org.androidannotations.rest.spring.annotations.Body;
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
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 2016/3/2.
 * http://wcapib.zczczy.com/
 * http://218.61.203.50:8018/
 */
@Rest(rootUrl = "http://wcapib.zczczy.com/", requestFactory = MyOkHttpClientHttpRequestFactory.class, interceptors = {MyInterceptor.class},
        converters = {StringHttpMessageConverter.class, GsonHttpMessageConverter.class, FormHttpMessageConverter.class, ByteArrayHttpMessageConverter.class},
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
    BaseModelJson<MemberInfoModel> login(@Path String UserName, @Path String UserPw, @Path int LoginType, @Path String Kbn);

    /**
     * 验证app是否需要更新
     *
     * @param kbn 类型
     * @return
     */
    @Get("api/Content/AppUpdCheck?kbn={kbn}")
    BaseModelJson<UpdateAppModel> appUpdCheck(@Path String kbn);

    /**
     * 查询App首页Banner
     *
     * @return
     */
    @Get("api/Content/GetHomeBanner")
    BaseModelJson<List<BannerModel>> getHomeBanner();

    /**
     * 查询公告信息表
     *
     * @return
     * @see NoticeInfoModel
     */
    @Get("api/Content/GetNoticeInfoList")
    BaseModelJson<List<NoticeInfoModel>> getNoticeInfoList();

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

    /**
     * 搜索商品
     *
     * @param PageIndex  当前页面
     * @param PageSize   页面大小
     * @param SearchWord 搜索关键字
     * @param OB         1:综合排序，2：销量降序，3：价格降序，4：价格升序
     * @return
     */
    @Get("api/Content/GetGoodsInfoLikeWord?PageIndex={PageIndex}&PageSize={PageSize}&SearchWord={SearchWord}&OB={OB}")
    BaseModelJson<PagerResult<GoodsModel>> getGoodsInfoLikeWord(@Path int PageIndex, @Path int PageSize, @Path String SearchWord, @Path String OB);

    /**
     * 查询商品明细
     *
     * @param GoodsInfoId 查询商品明细Id
     * @return
     */
    @Get("api/Content/GetGoodsInfoDetailById?GoodsInfoId={GoodsInfoId}")
    BaseModelJson<GoodsModel> getGoodsInfoDetailById(@Path String GoodsInfoId);

    /**
     * 查询推荐商品
     *
     * @param PageIndex 当前页面
     * @param PageSize  页面大小
     * @return
     */
    @Get("api/Content/GetRecommendedGoods?PageIndex={PageIndex}&PageSize={PageSize}")
    BaseModelJson<PagerResult<GoodsModel>> getRecommendedGoods(@Path int PageIndex, @Path int PageSize);

    /**
     * 会员用户注册
     *
     * @param map Mobile 手机号码
     *            Code 验证码
     * @return
     */
    @Post("api/Content/Register")
    BaseModelJson<String> register(@Body Map map);

    /**
     * 发送验证码
     *
     * @param map Mobile 手机号码
     *            SendType 发送类型（1：用户注册，2：用户登录）
     * @return
     */
    @Post("api/Content/SendMsg")
    BaseModel sendMsg(@Body Map map);

    /**
     * 根据商品ID查询评论信息
     *
     * @param PageIndex   当前页面
     * @param PageSize    页面大小
     * @param GoodsInfoId 商品Id
     * @return
     */
    @Get("api/Content/GetGoodsCommentsByGoodsInfoId?PageIndex={PageIndex}&PageSize={PageSize}&GoodsInfoId={GoodsInfoId}")
    BaseModelJson<PagerResult<GoodsCommentsModel>> getGoodsCommentsByGoodsInfoId(@Path int PageIndex, @Path int PageSize, @Path String GoodsInfoId);

    /**
     * 根据父ID查询二级分类
     *
     * @param GoodsTypePid 分类id
     * @return
     */
    @Get("api/Content/GetGoodsTypeListByPid?GoodsTypePid={GoodsTypePid}")
    BaseModelJson<List<GoodsTypeModel>> getGoodsTypeListByPid(@Path String GoodsTypePid);

    /**
     * 根据商品类别ID查询商品信息
     *
     * @param PageIndex   当前页面
     * @param PageSize    页面大小
     * @param GoodsTypeId 商品Id
     * @return
     */
    @Get("api/Content/GetGoodsInfoByTypeId?PageIndex={PageIndex}&PageSize={PageSize}&GoodsTypeId={GoodsTypeId}")
    BaseModelJson<PagerResult<GoodsModel>> getGoodsInfoByTypeId(@Path int PageIndex, @Path int PageSize, @Path String GoodsTypeId);

    /**
     * 查询微仓黄页列表信息
     *
     * @param PageIndex 当前页面
     * @param PageSize  页面大小
     * @return
     */
    @Get("api/Content/GetDepotList?PageIndex={PageIndex}&PageSize={PageSize}")
    BaseModelJson<PagerResult<DepotModel>> getDepotList(@Path int PageIndex, @Path int PageSize);

    /**
     * 查询待跑腿的任务信息
     *
     * @param PageIndex 当前页面
     * @param PageSize  页面大小
     * @return
     */
    @Get("api/Content/GetTaskOrderList?PageIndex={PageIndex}&PageSize={PageSize}")
    BaseModelJson<PagerResult<TaskOrderModel>> getTaskOrderList(@Path int PageIndex, @Path int PageSize);


    //==============================================================================================
    // 需要传入token 和 token

    /**
     * 查询用户信息
     *
     * @return
     */
    @Get("api/Member/GetMemberInfo")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModelJson<MemberInfoModel> getMemberInfo();

    /**
     * @param model MemberInfoModel
     * @return
     * @see MemberInfoModel
     */
    @Post("api/Member/PerfectMemberInfo")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModel perfectMemberInfo(@Body MemberInfoModel model);

    /**
     * 修改会员密码
     *
     * @param map OldPw 原始密码
     *            NewPw 新密码
     *            ConfirmPw 确认密码
     * @return
     */
    @Post("api/Member/UpdPassWord")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModel updPassWord(@Body Map map);

    /**
     * 修改会员头像
     *
     * @param model 头像地址
     * @return
     */
    @Post("api/Member/UpdateMemberInfoImg")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModelJson<String> updateMemberInfoImg(@Body Map model);

    /**
     * 更新头像
     * HttpHeaders.CONTENT_TYPE
     *
     * @param image
     * @return
     */
    @Post("http://updimage.86fuwuwang.com/FileHandler.ashx?type=17&folder=user")
    String uploadAvatar(@Part FileSystemResource image);

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

    /**
     * 商品加入购物车
     * 购物车加1
     *
     * @param id GoodsInfoId 商品ID
     * @return
     */
    @Post("api/Member/AddShoppingCart/{id}")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModel addShoppingCart(@Path String id);

    /**
     * 购物车减1
     *
     * @param id GoodsInfoId 商品ID
     * @return
     */
    @Post("api/Member/SubShoppingCart/{id}")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModel subShoppingCart(@Path String id);

    /**
     * 删除购物车商品
     *
     * @param ids 以逗号隔开
     * @return
     */
    @Post("api/Member/DelShoppingCartCountByIds/{ids}")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModel deleteShoppingCartById(@Path String ids);

    /**
     * 查询用户购物车信息
     *
     * @return
     */
    @Get("api/Member/GetBuyCartInfo")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModelJson<List<CartModel>> getBuyCartInfo();

    /**
     * 生成临时订单信息
     *
     * @param map BuyCardIds 购物车结算时选择的多个购物车ID,用逗号分隔的形式
     * @return
     */
    @Post("api/Member/CreateTempOrder")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModelJson<OrderModel> createTempOrder(@Body Map map);

    /**
     * (立即购买按钮)生成临时订单信息
     *
     * @param GoodsInfoId 商品id
     * @param Num         数量
     * @return
     */
    @RequiresHeader(value = {"Token", "Kbn"})
    @Get("api/Member/CreateSingleTempOrder?GoodsInfoId={GoodsInfoId}&Num={Num}")
    BaseModelJson<OrderModel> createSingleTempOrder(@Path String GoodsInfoId, @Path int Num);

    /**
     * 生成订单信息
     *
     * @param model d订单对象
     * @return
     * @see OrderModel
     */
    @Post("api/Member/CreateOrder")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModelJson<OrderModel> createOrder(@Body OrderModel model);


    @Get("api/Member/GetOrderInfoById?MorderId={MorderId}")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModelJson<OrderModel> getOrderInfoById(@Path String MorderId);

    /**
     * 根据订单状态查询订单信息
     *
     * @param PageIndex 当前页码
     * @param PageSize  每页显示记录数
     * @param Status    订单状态（0：待支付，1：待收货，2：全部订单）
     * @return
     */
    @Get("api/Member/GetOrderInfoListByStatus?PageIndex={PageIndex}&PageSize={PageSize}&Status={Status}")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModelJson<PagerResult<OrderModel>> getOrderInfoListByStatus(@Path int PageIndex, @Path int PageSize, @Path int Status);

    /**
     * 根据订单ID取消订单
     *
     * @param id 订单ID
     * @return
     */
    @Post("api/Member/CancelOrderByOrderId/{id}")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModel cancelOrderByOrderId(@Path String id);

    /**
     * 确认收货处理
     *
     * @param id 订单ID
     * @return
     */
    @Post("api/Member/ConfirmSh/{id}")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModel confirmSh(@Path String id);

    /**
     * 查询待评价订单
     *
     * @return
     * @see OrderDetailModel
     */
    @Get("api/Member/GetBeevaluatedOrder")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModelJson<List<OrderDetailModel>> getBeevaluatedOrder();

    /**
     * 添加商品评论
     *
     * @param map GoodsInfoId 商品主键ID
     *            MOrderDetailId 子订单ID
     *            GoodsCommentsDj 评论等级(1:好评，2：中评，3：差评)
     *            GoodsCommentsNr 评论内容
     *            XNum 星的数量
     * @return
     */
    @Post("api/Member/InsertGoodsComments")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModel insertGoodsComments(@Body Map map);

    /**
     * 查询物流信息
     *
     * @param MOrderId 订单id
     * @return
     */
    @Get("api/Member/GetLogistics?MOrderId={MOrderId}")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModelJson<List<LogisticsInfoModel>> getLogistics(@Path String MOrderId);

    /**
     * 申请经销商
     *
     * @param model DealerApplyModel
     * @return DealerApplyModel
     * @see
     */
    @Post("api/Member/DealerApply")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModel dealerApply(@Body DealerApplyModel model);

    /**
     * 意见反馈
     *
     * @param map 反馈内容
     * @return
     */
    @Post("api/Member/InsertFeedBack")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModel insertFeedback(@Body Map map);

    /**
     * 查询用户各订单状态的数量
     *
     * @return OrderCountModel
     */
    @Get("api/Member/GetUserOrderCount")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModelJson<OrderCountModel> getUserOrderCount();

    /**
     * 会员发布跑腿任务
     *
     * @param model OrderCountModel
     * @return
     * @see OrderCountModel
     */
    @Post("api/Member/PublisherTaskOrder")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModel publisherTaskOrder(@Body TaskOrderModel model);


    /**
     * 查询我的任务列表信息
     *
     * @param PageIndex 当前页面
     * @param PageSize  页面大小
     * @param kbn       区分（0：我发布的任务，1：我接受的任务）
     * @return
     */
    @Get("api/Member/GetMyTaskOrderList?PageIndex={PageIndex}&PageSize={PageSize}&kbn={kbn}")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModelJson<PagerResult<TaskOrderModel>> getMyTaskOrderList(@Path int PageIndex, @Path int PageSize, @Path String kbn);

    /**
     * 任务完成
     *
     * @param map TaskOrderId 任务id
     * @return
     */
    @Post("api/Member/FinishTaskOrder")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModel finishTaskOrder(@Body Map map);

    /**
     * 会员抢跑腿任务
     *
     * @param map TaskOrderId 任务id
     * @return
     */
    @Post("api/Member/LootTaskOrder")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModel lootTaskOrder(@Body Map map);

    /**
     * 根据任务ID查询任务明细
     *
     * @param TaskOrderId 任务id
     * @return
     */
    @Get("api/Member/GetTaskOrderById?TaskOrderId={TaskOrderId}")
    @RequiresHeader(value = {"Token", "Kbn"})
    BaseModelJson<TaskOrderModel> getTaskOrderById(@Path String TaskOrderId);

}

