package com.yidd365.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by orinchen on 2016/8/26.
 */

public final class Geohash {
  private static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7',
      '8', '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n',
      'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
  public static final double MIN_LAT = -90;
  public static final double MAX_LAT = 90;
  public static final double MIN_LNG = -180;
  public static final double MAX_LNG = 180;

  private Geohash(){}

  /**
   * @return
   * @Author:lulei
   * @Description: 求所在坐标点及周围点组成的九个
   */
  public static List<String> getGeoHashBase32For9(double lat, double lng, int hashLength) {
    assert (lat >= MIN_LAT && lat <= MAX_LAT);
    assert (lng >= MIN_LNG && lng <= MIN_LNG);

    int latLength = (int)Math.floor((hashLength * 5.0)/2.0);
    int lngLength = (int)Math.ceil((hashLength * 5.0)/2.0);

    double lat_step = MAX_LAT - MIN_LAT;
    for (int i = 0; i < latLength; i++) {
      lat_step /= 2.0;
    }

    double lng_step = MAX_LNG - MIN_LNG;
    for (int i = 0; i < lngLength; i++) {
      lng_step /= 2.0;
    }

    double leftLat = lat - lat_step;
    double rightLat = lat + lat_step;
    double upLng = lng - lng_step;
    double downLng = lng + lng_step;

    List<String> base32For9 = new ArrayList<>();
    //左侧从上到下 3个
    String leftUp = getGeoHashBase32(leftLat, upLng, hashLength);
    if (!(leftUp == null || "".equals(leftUp))) {
      base32For9.add(leftUp);
    }
    String leftMid = getGeoHashBase32(leftLat, lng, hashLength);
    if (!(leftMid == null || "".equals(leftMid))) {
      base32For9.add(leftMid);
    }
    String leftDown = getGeoHashBase32(leftLat, downLng, hashLength);
    if (!(leftDown == null || "".equals(leftDown))) {
      base32For9.add(leftDown);
    }
    //中间从上到下 3个
    String midUp = getGeoHashBase32(lat, upLng, hashLength);
    if (!(midUp == null || "".equals(midUp))) {
      base32For9.add(midUp);
    }
    String midMid = getGeoHashBase32(lat, lng, hashLength);
    if (!(midMid == null || "".equals(midMid))) {
      base32For9.add(midMid);
    }
    String midDown = getGeoHashBase32(lat, downLng, hashLength);
    if (!(midDown == null || "".equals(midDown))) {
      base32For9.add(midDown);
    }
    //右侧从上到下 3个
    String rightUp = getGeoHashBase32(rightLat, upLng, hashLength);
    if (!(rightUp == null || "".equals(rightUp))) {
      base32For9.add(rightUp);
    }
    String rightMid = getGeoHashBase32(rightLat, lng, hashLength);
    if (!(rightMid == null || "".equals(rightMid))) {
      base32For9.add(rightMid);
    }
    String rightDown = getGeoHashBase32(rightLat, downLng, hashLength);
    if (!(rightDown == null || "".equals(rightDown))) {
      base32For9.add(rightDown);
    }
    return base32For9;
  }

  /**
   * @param lat
   * @param lng
   * @return
   * @Author:lulei
   * @Description: 获取经纬度的base32字符串
   */
  public static String getGeoHashBase32(double lat, double lng, int hashLength) {
    int[] binarys = getGeoBinary(lat, lng, hashLength);
    if (binarys == null) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < binarys.length; i = i + 5) {

      int sy = binarys[i] << 4
          | binarys[i + 1] << 3
          | binarys[i + 2] << 2
          | binarys[i + 3] << 1
          | binarys[i + 4];

      char cha = CHARS[sy];
      sb.append(cha);
    }
    return sb.toString();
  }

  /**
   * @param lat
   * @param lng
   * @return
   * @Author:lulei
   * @Description: 获取坐标的geo二进制字符串
   */
  private static int[] getGeoBinary(double lat, double lng, int hashLength) {
    assert (hashLength > 0);
    if (lat < MIN_LAT || lat > MAX_LAT){
      return null;
    }

    int latLength = (int)Math.floor((hashLength * 5.0)/2.0);
    int lngLength = (int)Math.ceil((hashLength * 5.0)/2.0);
    int[] latArray = getHashArray(lat, MIN_LAT, MAX_LAT, latLength);
    int[] lngArray = getHashArray(lng, MIN_LNG, MAX_LNG, lngLength);
    return merge(latArray, lngArray);
  }

  /**
   * @param latArray
   * @param lngArray
   * @return
   * @Author:lulei
   * @Description: 合并经纬度二进制
   */
  private static int[] merge(int[] latArray, int[] lngArray) {
    assert (latArray != null && lngArray != null);

    int[] result = new int[lngArray.length + latArray.length];
    Arrays.fill(result, 0x00);
    for (int i = 0; i < lngArray.length; i++) {
      result[2 * i] = lngArray[i];
    }

    for (int i = 0; i < latArray.length; i++) {
      result[2 * i + 1] = latArray[i];
    }

    return result;
  }

  /**
   * @param value
   * @param min
   * @param max
   * @return
   * @Author:lulei
   * @Description: 将数字转化为geohash二进制字符串
   */
  private static int[] getHashArray(double value, double min, double max, int length) {
    assert (value>=min && value <= max);
    assert (length >= 1);

    int[] result = new int[length];
    for (int i = 0; i < length; i++) {
      double mid = (min + max) / 2.0;
      if (value > mid) {
        result[i] = 0x01;
        min = mid;
      } else {
        result[i] = 0x00;
        max = mid;
      }
    }
    return result;
  }
}
