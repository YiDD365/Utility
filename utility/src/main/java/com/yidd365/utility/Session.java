package com.yidd365.utility;

import android.content.Context;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by orinchen on 16/6/12.
 */
public final class Session {
    private final static String TABLE_NAME = "Sessions";
    private Session(){
        KeyValueStore.getInstance().creatTable(TABLE_NAME);
    }

    private static class SingletonHolder {
        private static final Session instance = new Session();
    }

    public static void Initialize(Context context) {
        KeyValueStore.Initialize(context);
    }

    public static Session getInstance(){
        return SingletonHolder.instance;
    }

    public void putString(String key, String str){
        KeyValueStore.getInstance().putString(TABLE_NAME, key, str);
    }

    public String getString(String key){
        return KeyValueStore.getInstance().getString(TABLE_NAME, key);
    }

    public void putInteger(String tableName, String key, Integer value){
        KeyValueStore.getInstance().putInteger(TABLE_NAME, key, value);
    }

    public Integer getInteger(String tableName, String key){
        return KeyValueStore.getInstance().getInteger(TABLE_NAME, key);
    }

    public void putDouble(String tableName, String key, Double value){
        KeyValueStore.getInstance().putDouble(TABLE_NAME, key, value);
    }

    public Double getDouble(String tableName, String key){
        return KeyValueStore.getInstance().getDouble(TABLE_NAME, key);
    }

    public void putLong(String tableName, String key, Long value){
        KeyValueStore.getInstance().putLong(TABLE_NAME, key, value);
    }

    public Long getLong(String tableName, String key){
        return KeyValueStore.getInstance().getLong(TABLE_NAME, key);
    }

    public void putFloat(String tableName, String key, Float value){
        KeyValueStore.getInstance().putFloat(TABLE_NAME, key, value);
    }

    public Float getFloat(String tableName, String key){
        return KeyValueStore.getInstance().getFloat(TABLE_NAME, key);
    }

    public void putShort(String tableName, String key, Short value){
        KeyValueStore.getInstance().putShort(TABLE_NAME, key, value);
    }

    public Short getShort(String tableName, String key){
        return KeyValueStore.getInstance().getShort(TABLE_NAME, key);
    }

    public void putDate(String tableName, String key, Date date){
        KeyValueStore.getInstance().putDate(TABLE_NAME, key, date);
    }

    public Date getDate(String tableName, String key){
        return KeyValueStore.getInstance().getDate(TABLE_NAME, key);
    }

    public void putDateTime(String tableName, String key, LocalDateTime ldt){
        KeyValueStore.getInstance().putDateTime(TABLE_NAME, key, ldt);
    }

    public LocalDateTime getDateTime(String tableName, String key){
        return KeyValueStore.getInstance().getDateTime(TABLE_NAME, key);
    }

    public void putLocalDate(String tableName, String key, LocalDate ld){
        KeyValueStore.getInstance().putLocalDate(TABLE_NAME, key, ld);
    }

    public LocalDate getLocalDate(String tableName, String key){
        return KeyValueStore.getInstance().getLocalDate(TABLE_NAME, key);
    }

    public void putTime(String tableName, String key, LocalTime lt){
        KeyValueStore.getInstance().putTime(TABLE_NAME, key, lt);
    }

    public LocalTime getTime(String tableName, String key){
        return KeyValueStore.getInstance().getTime(TABLE_NAME, key);
    }

    public void put(String key, Object obj){
        KeyValueStore.getInstance().put(TABLE_NAME, key, obj);
    }

    public<T> T get(String key, Type typeOfT){
        return KeyValueStore.getInstance().getObject(TABLE_NAME,key,typeOfT);
    }
}
