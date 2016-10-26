package com.yidd365.demo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by orinchen on 2016/10/26.
 */

public final class Enterprise implements Serializable {
  private String id;
  @SerializedName("enterpriseName")
  private String name;
  private String address;
  private String legalPerson;
  private String leadingPerson;
  private String tel;
  private String image;
  @SerializedName("enterpriseLat")
  private Float lat;
  @SerializedName("enterpriseLong")
  private Float lng;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getLegalPerson() {
    return legalPerson;
  }

  public void setLegalPerson(String legalPerson) {
    this.legalPerson = legalPerson;
  }

  public String getLeadingPerson() {
    return leadingPerson;
  }

  public void setLeadingPerson(String leadingPerson) {
    this.leadingPerson = leadingPerson;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public String getImage() {
    return image.replace('\\', '/');
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Float getLat() {

    return lat;
  }

  public void setLat(Float lat) {
    if (lat == null)
      lat = 0.0f;
    this.lat = lat;
  }

  public Float getLng() {
    return lng;
  }

  public void setLng(Float lng) {
    if (lng == null)
      lng = 0.0f;
    this.lng = lng;
  }
}
