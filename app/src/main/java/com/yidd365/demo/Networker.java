package com.yidd365.demo;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.yidd365.utility.ILogger;
import com.yidd365.utility.network.HttpLogInterceptor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by orinchen on 2016/10/26.
 */

public final class Networker {
  private static final String BaseURL = "http://liling.gov.gsp365.cn/";
  //private static final String BaseURL = "http://192.168.1.237:8083/";// 花生壳
  private static final int DEFAULT_TIMEOUT = 5;

  private Retrofit retrofit = null;
  private WebService service = null;

  public static final Networker getInstance() {
    return SingletonHolder.INSTANCE;
  }

  private static class SingletonHolder {
    private static final Networker INSTANCE = new Networker();
  }

  private Networker(){
    HttpLogInterceptor interceptor = new HttpLogInterceptor(new ILogger() {
      @Override
      public void log(String message) {
       Logger.d(message);
      }
    });

    interceptor.setLevel(HttpLogInterceptor.Level.BODY);

//    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
////设定日志级别
//    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder okHttpClientBuilder =
        new OkHttpClient.Builder()
//                        .hostnameVerifier((hostname, session) -> true)
            .addInterceptor(interceptor)
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true);
//                        .sslSocketFactory(HttpsUtils.getSslSocketFactory(new InputStream[]{new Buffer().writeUtf8(CER_12306).inputStream()}, null, null));

    this.retrofit = new Retrofit.Builder()
        .baseUrl(BaseURL)
        .client(okHttpClientBuilder.build())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    this.service = this.retrofit.create(WebService.class);
  }

  public Observable<RESTResult<List<Enterprise>>> fetchEnterprises(@NonNull String enName,
                                                                   @NonNull String hasImage,
                                                                   int pageIndex,
                                                                   int pageSize){
    Observable<RESTResult<List<Enterprise>>> observable = service.fetchEnterprise(enName, hasImage, pageIndex, pageSize);
    return observable;
  }
}
