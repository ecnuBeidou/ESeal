package com.agenthun.eseal.connectivity.manager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.agenthun.eseal.bean.BeidouMasterDeviceInfos;
import com.agenthun.eseal.bean.BleAndBeidouNfcDeviceInfos;
import com.agenthun.eseal.bean.DeviceLocationInfos;
import com.agenthun.eseal.bean.User;
import com.agenthun.eseal.bean.base.BeidouMasterDevice;
import com.agenthun.eseal.bean.base.BleAndBeidouNfcDevice;
import com.agenthun.eseal.bean.base.DeviceLocation;
import com.agenthun.eseal.bean.base.LocationDetail;
import com.agenthun.eseal.bean.base.Result;
import com.agenthun.eseal.bean.updateByRetrofit.UpdateResponse;
import com.agenthun.eseal.connectivity.manager.cookie.CacheInterceptor;
import com.agenthun.eseal.connectivity.manager.cookie.CookieJarManager;
import com.agenthun.eseal.connectivity.service.Api;
import com.agenthun.eseal.connectivity.service.FreightTrackWebService;
import com.agenthun.eseal.connectivity.service.PathType;
import com.agenthun.eseal.utils.DeviceSearchSuggestion;
import com.agenthun.eseal.utils.LanguageUtil;
import com.baidu.mapapi.model.LatLng;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
//import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/2 下午8:59.
 */
public class RetrofitManager {
    private static final String TAG = "RetrofitManager";

    private static FreightTrackWebService freightTrackWebService;
    private static OkHttpClient mOkHttpClient = null;
    private Cache cache = null;
    private File httpCacheDirectory;
    private Context mContext;

    //创建实例
    public static RetrofitManager builder(PathType pathType) {
        return new RetrofitManager(pathType);
    }

    public static RetrofitManager builder(Context context, PathType pathType) {
        return new RetrofitManager(context, pathType);
    }

    //配置Retrofit
    public RetrofitManager(PathType pathType) {
        if (freightTrackWebService == null) {
            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(getPath(pathType))
//                    .baseUrl(getPath(PathType.WEB_SERVICE_V2_RELEASE))
                    .baseUrl(getPath(PathType.WEB_SERVICE_V2_TEST))
                    .addConverterFactory(XMLGsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            freightTrackWebService = retrofit.create(FreightTrackWebService.class);
        }
    }

    public RetrofitManager(Context context, PathType pathType) {
        mContext = context;
        initOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(getPath(pathType))
//                    .baseUrl(getPath(PathType.WEB_SERVICE_V2_RELEASE))
                .baseUrl(getPath(PathType.WEB_SERVICE_V2_TEST))
                .client(mOkHttpClient)
                .addConverterFactory(XMLGsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        freightTrackWebService = retrofit.create(FreightTrackWebService.class);
    }

    //配置OKHttpClient
    private void initOkHttpClient() {
        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), "okhttp_cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
            }
        } catch (Exception e) {
            Log.e("OKHttp", "Could not create http cache", e);
        }
        mOkHttpClient = new OkHttpClient.Builder()
//                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .cookieJar(new CookieJarManager(mContext))
                .cache(cache)
                .addInterceptor(new CacheInterceptor(mContext))
                .addNetworkInterceptor(new CacheInterceptor(mContext))
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(5, 10, TimeUnit.SECONDS))
                .build();
    }

    //获取相应的web路径
    private String getPath(PathType pathType) {
        switch (pathType) {
            case BASE_WEB_SERVICE:
                return Api.K_API_BASE_URL_STRING;
            case AMAP_SERVICE:
                return Api.AMAP_SERVICE_URL_STRING;
            case WEB_SERVICE_V2_TEST:
                return Api.WEB_SERVICE_V2_TEST;
            case WEB_SERVICE_V2_RELEASE:
                return Api.WEB_SERVICE_V2_RELEASE;
            case MAP_SERVICE_V2_TEST:
                return Api.MAP_SERVICE_V2_URL_STRING;
            case ESeal_UPDATE_SERVICE_URL:
                return Api.ESeal_UPDATE_SERVICE_URL;
            case ESeal_LITE_UPDATE_SERVICE_URL:
                return Api.ESeal_LITE_UPDATE_SERVICE_URL;
        }
        return "";
    }


    //登陆,获取token
    public Observable<User> getTokenObservable(String userName, String password) {
        return freightTrackWebService.getToken(userName, password, LanguageUtil.getLanguage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /**
     * @description 蓝牙锁、北斗终端NFC 配置设备
     */
    //配置终端货物信息参数
    public Observable<Result> configureDeviceObservable(String token, @Nullable String deviceType, @Nullable String implementID,
                                                        @Nullable String containerNo, @Nullable String freightOwner, @Nullable String freightName, @Nullable String origin, @Nullable String destination, @Nullable String VesselName, @Nullable String voyage,
                                                        @Nullable String frequency,
                                                        String RFID,
                                                        @Nullable String images,
                                                        @Nullable String coordinate,
                                                        String operateTime) {
        return freightTrackWebService
                .configureDevice(token, deviceType, implementID,
                        containerNo, freightOwner, freightName, origin, destination, VesselName, voyage,
                        frequency,
                        RFID,
                        images,
                        coordinate,
                        operateTime,
                        LanguageUtil.getLanguage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /**
     * @description 蓝牙锁、北斗终端NFC设备的上封/解封操作
     */
    //开箱操作 - 获取MAC implementID="12345678"
    public Observable<Result> openDeviceObservable(String token, String implementID, String RFID, @Nullable String images, @Nullable String coordinate, String operateTime) {
        return freightTrackWebService.openDevice(token, implementID, RFID, images, coordinate, operateTime, LanguageUtil.getLanguage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    //关箱操作 - 获取MAC implementID="12345678"
    public Observable<Result> closeDeviceObservable(String token, String containerNo, String implementID, String RFID, @Nullable String images, @Nullable String coordinate, String operateTime) {
        return freightTrackWebService.closeDevice(token, containerNo, implementID, RFID, images, coordinate, operateTime, LanguageUtil.getLanguage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /**
     * @description 蓝牙锁、北斗终端NFC设备访问链路
     */
    //根据Token获取蓝牙锁和BeidouNfc设备的所有在途中的货物信息
    public Observable<BleAndBeidouNfcDeviceInfos> getBleAndBeidouNfcDeviceFreightListObservable(String token) {
        return freightTrackWebService.getBleAndBeidouNfcDeviceFreightList(token, LanguageUtil.getLanguage());
    }


    /**
     * @description 北斗终端帽访问链路
     */
    //根据Token获取北斗终端帽的所有在途中的货物信息
    public Observable<BeidouMasterDeviceInfos> getBeidouMasterDeviceFreightListObservable(String token) {
        return freightTrackWebService.getBeidouMasterDeviceFreightList(token, LanguageUtil.getLanguage());
    }

    //根据containerId获取蓝牙锁和BeidouNfc设备的该货物状态列表
    public Observable<DeviceLocationInfos> getBleAndBeidouNfcDeviceLocationObservable(String token, String containerId) {
        return freightTrackWebService.getBleAndBeidouNfcDeviceLocation(token, containerId, LanguageUtil.getLanguage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    //根据implementID获取该货物状态列表
    public Observable<DeviceLocationInfos> getBeidouMasterDeviceLocationObservable(String token, String implementID) {
        return freightTrackWebService.getBeidouMasterDeviceLocation(token, implementID, LanguageUtil.getLanguage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /**
     * @description 获取所有终端(蓝牙锁、北斗终端NFC、北斗终端帽)的货物信息
     * 返回DeviceSearchSuggestion列表
     */
    //根据Token获取所有终端的货物信息, 返回DeviceSearchSuggestion列表
    public Observable<List<DeviceSearchSuggestion>> getAllDeviceFreightListObservable(String token) {
        Observable<BleAndBeidouNfcDeviceInfos> bleAndBeidouNfcDeviceInfos = getBleAndBeidouNfcDeviceFreightListObservable(token);
        Observable<BeidouMasterDeviceInfos> beidouMasterDeviceInfos = getBeidouMasterDeviceFreightListObservable(token);

        return Observable.zip(bleAndBeidouNfcDeviceInfos, beidouMasterDeviceInfos, new Func2<BleAndBeidouNfcDeviceInfos, BeidouMasterDeviceInfos, List<DeviceSearchSuggestion>>() {
            @Override
            public List<DeviceSearchSuggestion> call(BleAndBeidouNfcDeviceInfos bleAndBeidouNfcDeviceInfos, BeidouMasterDeviceInfos beidouMasterDeviceInfos) {
                List<DeviceSearchSuggestion> result = new ArrayList<DeviceSearchSuggestion>();

                // Token timeout
                if (bleAndBeidouNfcDeviceInfos.getResult().get(0).getRESULT() == 0 && bleAndBeidouNfcDeviceInfos.getResult().get(0).getEFFECTIVETOKEN() == 0) {
                    // Need to find a way to notify users.
                }

                if (bleAndBeidouNfcDeviceInfos != null &&
                        bleAndBeidouNfcDeviceInfos.getResult().get(0).getRESULT() == 1) {
                    List<BleAndBeidouNfcDevice> details = bleAndBeidouNfcDeviceInfos.getDetails();

                    if (details != null && !details.isEmpty()) {
                        for (BleAndBeidouNfcDevice detail :
                                details) {
                            Log.d(TAG, "getBleAndBeidouNfcDevice(): " + detail.toString());
                            if (detail.getDeviceType().equals(Api.DEVICE_TYPE_BLE)) {
                                DeviceSearchSuggestion suggestion = new DeviceSearchSuggestion(detail,
                                        DeviceSearchSuggestion.DEVICE_BLE);
                                result.add(suggestion);
                            } else if (detail.getDeviceType().equals(Api.DEVICE_TYPE_BEIDOU_NFC)) {
                                DeviceSearchSuggestion suggestion = new DeviceSearchSuggestion(detail,
                                        DeviceSearchSuggestion.DEVICE_BEIDOU_NFC);
                                result.add(suggestion);
                            }
                        }
                    }
                }

                if (beidouMasterDeviceInfos != null
                        && beidouMasterDeviceInfos.getResult().get(0).getRESULT() == 1) {
                    List<BeidouMasterDevice> details = beidouMasterDeviceInfos.getDetails();
                    if (details != null && !details.isEmpty()) {
                        for (BeidouMasterDevice detail :
                                details) {
                            Log.d(TAG, "getBeidouMasterDevice(): " + detail.toString());
                            DeviceSearchSuggestion suggestion = new DeviceSearchSuggestion(detail);
                            result.add(suggestion);
                        }
                    }
                }

                return result;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /**
     * @description 版本检测更新
     */
    //APP 版本检测更新
    public Observable<UpdateResponse.Entity> checkAppUpdateObservable() {
        Observable<UpdateResponse> response = freightTrackWebService.checkAppUpdate();
        return response.map(new Func1<UpdateResponse, UpdateResponse.Entity>() {
            @Override
            public UpdateResponse.Entity call(UpdateResponse updateResponse) {
                if (updateResponse == null) {
                    return null;
                }
                if (updateResponse.getError() == null || updateResponse.getError().getResult() != 1) {
                    return null;
                }
                if (updateResponse.getEntity() != null) {
                    return updateResponse.getEntity();
                }
                return null;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());

        //构造测试数据
/*        String testDownloadUrl = "http://www.freight-track.com/files/ESeal_Lite_v_1_0_3_2017-02-25.apk";
        return Observable
                .just(new UpdateResponse.Entity(12537361, false,
                        "1.谷歌地图更新\r\n2.UI更新",
                        testDownloadUrl,
                        100,
                        "x.0.x"))
                .delay(100, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());*/
    }

    /**
     * @description 下载文件
     */
    //下载文件
    public Observable<ResponseBody> downloadFileObservable(@Url String fileUrl) {
        return freightTrackWebService.downloadFile(fileUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

}
