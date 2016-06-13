package com.yidd365.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by orinchen on 16/6/9.
 */
public class KeyValueStore {
    static String DEFAULT_DB_NAME = "database.sqlite";

    static String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %s (id TEXT NOT NULL, json TEXT NOT NULL, createdTime INTEGER NOT NULL,PRIMARY KEY(id));";

    static String COUNT_ALL_SQL = "SELECT count(*) as num from %s";

    static String DELETE_ITEMS_SQL = "DELETE from %s where id in ( %s )";

    static String CHECK_TABLE_EXIST_SQL = "select count(*) from sqlite_master where type ='table' and name = ? ";

    private SQLiteDatabase db = null;

    private static Context context;

    private KeyValueStore() {
        this.db = KeyValueStore.context.openOrCreateDatabase(DEFAULT_DB_NAME, Context.MODE_PRIVATE, null);
    }

    private static class SingletonHolder {
        private static final KeyValueStore instance = new KeyValueStore();
    }

    public static final KeyValueStore getInstance() {
        assert (KeyValueStore.context != null);
        return SingletonHolder.instance;
    }

    public static void Initialize(Context context) {
        if(KeyValueStore.context == null)
            KeyValueStore.context = context;
    }

    protected void finalize() {
        if (this.db != null) {
            this.db.close();
        }
        this.db = null;
    }

    public static boolean checkTableName(String tableName){
        if(tableName == null || tableName.trim().length() < 1|| tableName.indexOf(" ") != -1 ){
            return false;
        }
        return true;
    }

    public void creatTable(String tableName) {
        if(!checkTableName(tableName)){
            return;
        }
        if (this.tableIsExists(tableName)) {
            return;
        }
        String sql = String.format(CREATE_TABLE_SQL, tableName);
        this.db.execSQL(sql);
    }

    public boolean tableIsExists(String tabName) {
        boolean result = false;
        Cursor cursor = this.db.rawQuery(CHECK_TABLE_EXIST_SQL, new String[]{ tabName.trim()});
        if (cursor.moveToNext()) {
            int count = cursor.getInt(0);
            result = count > 0;
        }
        cursor.close();
        return result;
    }

    public void clearTable(String tableName){
        if(!checkTableName(tableName)){
            return;
        }

        this.db.delete(tableName, null, null);
    }

    public void put(String tableName, String key, Object obj) {
        assert (!StringUtils.isNullOrWhitespace(key));
        if (!checkTableName(tableName)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put("id", key);

        if(obj instanceof String ||
                obj instanceof Integer ||
                obj instanceof Long ||
                obj instanceof Double ||
                obj instanceof Float ||
                obj instanceof Short ||
                obj instanceof Boolean) {
            values.put("json", obj.toString());
        }else if(obj instanceof Date){
            values.put("json", DateUtils.formatDateTime((Date) obj));
        }else if(obj instanceof LocalDateTime){
            values.put("json", DateUtils.formatDateTime((LocalDateTime) obj));
        }else if(obj instanceof LocalDate){
            values.put("json", DateUtils.formatDate((LocalDate) obj));
        }else if(obj instanceof LocalTime){
            values.put("json", DateUtils.formatTime((LocalTime) obj));
        }
        else {
            values.put("json", new Gson().toJson(obj));
        }

        values.put("createdTime", System.currentTimeMillis());
        this.db.replace(tableName, null, values);
    }

    public void putString(String tableName, String key, String str){
        put(tableName, key, str);
    }

    public String getString(String tableName, String key){
        return getObject(tableName, key, String.class);
    }

    public void putInteger(String tableName, String key, Integer value){
        put(tableName, key, value);
    }

    public Integer getInteger(String tableName, String key){
        return getObject(tableName, key, Integer.class);
    }

    public void putDouble(String tableName, String key, Double value){
        put(tableName, key, value);
    }

    public Double getDouble(String tableName, String key){
        return getObject(tableName, key, Double.class);
    }

    public void putLong(String tableName, String key, Long value){
        put(tableName, key, value);
    }

    public Long getLong(String tableName, String key){
        return getObject(tableName, key, Long.class);
    }

    public void putFloat(String tableName, String key, Float value){
        put(tableName, key, value);
    }

    public Float getFloat(String tableName, String key){
        return getObject(tableName, key, Float.class);
    }

    public void putShort(String tableName, String key, Short value){
        put(tableName, key, value);
    }

    public Short getShort(String tableName, String key){
        return getObject(tableName, key, Short.class);
    }

    public void putDate(String tableName, String key, Date date){
        put(tableName, key, date);
    }

    public Date getDate(String tableName, String key){
        return getObject(tableName, key, Date.class);
    }

    public void putDateTime(String tableName, String key, LocalDateTime ldt){
        put(tableName, key, ldt);
    }

    public LocalDateTime getDateTime(String tableName, String key){
        return getObject(tableName, key, LocalDateTime.class);
    }

    public void putLocalDate(String tableName, String key, LocalDate ld){
        put(tableName, key, ld);
    }

    public LocalDate getLocalDate(String tableName, String key){
        return getObject(tableName, key, LocalDate.class);
    }

    public void putTime(String tableName, String key, LocalTime lt){
        put(tableName, key, lt);
    }

    public LocalTime getTime(String tableName, String key){
        return getObject(tableName, key, LocalTime.class);
    }

    public <T> KeyValueItem<T> getItem(String tableName, String key, Type typeOfT) {
        if (!checkTableName(tableName)) {
            return null;
        }
        Cursor cursor = null;
        KeyValueItem<T> item = null;
        try {
            cursor = this.db.query(tableName,new String[]{"id", "json", "createdTime"}, "id=?", new String[]{key}, null, null, null, "1" );
            if (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String json = cursor.getString(1);
                long createTime = cursor.getLong(2);

                item = new KeyValueItem<>();

                item.setId(id);
                item.setCreatTime(createTime);
                if (StringUtils.isNullOrWhitespace(json)) {
                    item.setData(null);
                }
                T o = processJsonToObject(typeOfT, json);
                item.setData(o);
            }

        } catch (Exception ex) {}
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return item;
    }

    @Nullable
    private <T> T processJsonToObject(Type typeOfT, String json) {
        String typeOfTName = typeOfT.toString();
        Object o = null;
        if(typeOfTName.equalsIgnoreCase(Integer.class.toString())){
            o = Integer.parseInt(json);
        }else if (typeOfTName.equalsIgnoreCase(Long.class.toString())){
            o = Long.parseLong(json);
        }else if (typeOfTName.equalsIgnoreCase(Short.class.toString())){
            o = Short.parseShort(json);
        }else if (typeOfTName.equalsIgnoreCase(Double.class.toString())){
            o = Double.parseDouble(json);
        }else if (typeOfTName.equalsIgnoreCase(Float.class.toString())){
            o = Float.parseFloat(json);
        }else if (typeOfTName.equalsIgnoreCase(Boolean.class.toString())) {
            o = Boolean.parseBoolean(json);
        }else if(typeOfTName.equalsIgnoreCase(String.class.toString())) {
            o = json;
        }else if(typeOfTName.equalsIgnoreCase(Date.class.toString())){
            LocalDateTime ldt = DateUtils.fromDateTimeStr(json);
            o = DateUtils.convertToDate(ldt);
        }else if(typeOfTName.equalsIgnoreCase(LocalDateTime.class.toString())){
            o = DateUtils.fromDateTimeStr(json);
        }else if(typeOfTName.equalsIgnoreCase(LocalDate.class.toString())){
            o = DateUtils.fromDateStr(json);
        }else if(typeOfTName.equalsIgnoreCase(LocalTime.class.toString())){
            o = DateUtils.fromTimeStr(json);
        } else {
            o = new Gson().fromJson(json, typeOfT);
        }
        return (T)o;
    }

    public <T> T getObject(String tableName, String key, Type typeOfT){
        if (!checkTableName(tableName)) {
            return null;
        }

        KeyValueItem<T> item = this.getItem(tableName, key, typeOfT);

        if(item == null)
            return null;

        return item.getData();
    }

    public <T> Collection<KeyValueItem<T>> getAll(String tableName, Type typeOfT){
        if (!checkTableName(tableName)) {
            return null;
        }
        ArrayList<KeyValueItem<T>> items = null;
        Cursor cursor = null;
        try {
            cursor = this.db.query(tableName,new String[]{"id", "json", "createdTime"}, null, null, null, null, "createdTime");
            items = new ArrayList<>();
            if (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String json = cursor.getString(1);
                long createdTime = cursor.getLong(2);

                KeyValueItem<T> item = new KeyValueItem<>();

                item.setId(id);
                item.setCreatTime(createdTime);
                if (StringUtils.isNullOrWhitespace(json)) {
                    item.setData(null);
                }

                T o = processJsonToObject(typeOfT, json);

                item.setData(o);
                items.add(item);
            }
        } catch (Exception ex) {}
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return items;
    }

    public void delete(String tableName, String id){
        if(!checkTableName(tableName)){
            return;
        }

        this.db.delete(tableName, "id=?", new String[]{id});
    }

    public void delete(String tableName, String... ids){
        if(!checkTableName(tableName)){
            return;
        }

        StringBuilder idsBuilder = new StringBuilder();
        for (String id : ids) {
            if(idsBuilder.length() > 0){
                idsBuilder.append(',');
            }
            idsBuilder.append('\'');
            idsBuilder.append(id);
            idsBuilder.append('\'');
        }

        String sql = String.format(DELETE_ITEMS_SQL, tableName, idsBuilder.toString());
        this.db.execSQL(sql);
    }

    public void deleteByIdPrefix(String tableName, String idPrefix){
        if(!checkTableName(tableName)){
            return;
        }

        this.db.delete(tableName, "id like ?", new String[]{ idPrefix + "%"});
    }

    public long getCountFromTable(String tableName){
        if(!checkTableName(tableName)){
            return 0;
        }
        String sql = String.format(COUNT_ALL_SQL, tableName);
        Cursor cursor = null;

        long count = 0;
        try {
            cursor = this.db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                count = cursor.getLong(0);
            }
        }catch (Exception ex){}
        finally {
            if(cursor != null)
                cursor.close();
        }
        return count;
    }

    public class KeyValueItem <T> {
        private String id;
        private T data;
        private long creatTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public long getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(long date) {
            this.creatTime = date;
        }
    }
}
