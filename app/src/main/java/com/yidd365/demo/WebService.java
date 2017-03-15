package com.yidd365.demo;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by orinchen on 2016/10/26.
 */

public interface WebService {
  @FormUrlEncoded
  @POST("Api/EnterpriseImage/index")
  Observable<RESTResult<List<Enterprise>>> fetchEnterprise (
      @Field("enterpriseName") @NonNull String enName,
      @Field("isUploadedImg") @NonNull String hasImg,
      @Field("pageIndex") int pageIndex,
      @Field("pageNum") int pageSize);
}
