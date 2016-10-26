package com.yidd365.demo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by orinchen on 2016/10/26.
 */

public class RESTResult<DataType> implements Serializable {
  @SerializedName("Data")
  private DataType data;
  private String state;
  @SerializedName("DataCount")
  private Integer dataCount;
  @SerializedName("msg")
  private String message;

  public DataType getData() {
    return data;
  }

  public void setData(DataType data) {
    this.data = data;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Integer getDataCount() {
    return dataCount;
  }

  public void setDataCount(int dataCount) {
    this.dataCount = dataCount;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}