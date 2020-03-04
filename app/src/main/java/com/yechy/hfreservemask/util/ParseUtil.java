package com.yechy.hfreservemask.util;

import android.text.TextUtils;

import com.yechy.hfreservemask.entry.MaskInfo;
import com.yechy.hfreservemask.entry.Pharmacy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cloud on 2020-02-22.
 */
public class ParseUtil {

    public static List<MaskInfo> parseMaskInfo(String response) {
        List<MaskInfo> maskInfoList = new ArrayList<>();
        String result = response;
        if (!TextUtils.isEmpty(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean succeed = jsonObject.optBoolean("succeed");
                int status = jsonObject.optInt("status");
                if (succeed) {
                    String msg = jsonObject.optString("msg");
                    JSONArray jsonArray = new JSONArray(msg);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                        int remain = jsonObject1.optInt("remain");
                        String text = jsonObject1.optString("text");
                        String value = jsonObject1.optString("value");
                        MaskInfo maskInfo = new MaskInfo(remain, text, value);
                        maskInfoList.add(maskInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return maskInfoList;
    }

    public static List<Pharmacy> parsePharmacyInfo(String response) {
        List<Pharmacy> pharmacyList = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean succeed = jsonObject.optBoolean("succeed");
                int status = jsonObject.optInt("status");
                if (succeed) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                        int code = jsonObject1.optInt("code");
                        String name = jsonObject1.optString("name");
                        Pharmacy pharmacy = new Pharmacy(name, code);
                        pharmacyList.add(pharmacy);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return pharmacyList;
    }
}
