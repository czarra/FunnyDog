package com.example.rad.funnydog.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rad on 2017-12-04.
 */

public class Dogs {

    public static Dogs fromJsonObject(JSONObject jsonObject) {
        try {
            return new Dogs(jsonObject.getString("id"), jsonObject.getString("url"),
                    jsonObject.getString("time"),
                    jsonObject.getString("format"));
        } catch (JSONException exp) {
            Log.e("dogs class", exp.getMessage());
        }
        return null;
    }

    public final String id;
    public final String url;
    public final String time;
    public final String format;
    private boolean star = false;

    public Dogs() {
        this(null,null,null,null);
    }


    private Dogs(String id, String url, String time, String format ) {
        this.id = id;
        this.url = url;
        this.time = time;
        this.format = format;

    }

    @Override
    public String toString() {
        return "id=" + id + " url= " + url + ", format=" + format;
    }

    public void setStar(boolean star){
        this.star = star;
    }
    public Boolean getStar(){
        return this.star;
    }


}
