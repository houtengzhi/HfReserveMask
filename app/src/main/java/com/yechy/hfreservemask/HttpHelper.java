package com.yechy.hfreservemask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.yechy.hfreservemask.entry.MaskInfo;
import com.yechy.hfreservemask.entry.Pharmacy;
import com.yechy.hfreservemask.entry.ReserveResult;
import com.yechy.hfreservemask.entry.User;
import com.yechy.hfreservemask.entry.UserSendData;
import com.yechy.hfreservemask.util.Constants;
import com.yechy.hfreservemask.util.FileUtil;
import com.yechy.hfreservemask.util.ParseUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by cloud on 2020-02-21.
 */
public class HttpHelper {

    private static final String TAG = "HttpHelper";
    private static final String USER_AGENT = "Linux; U; Android 2.3.6; zh-cn; GT-S5660 Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1 MicroMessenger/4.5.255";

    private static final String START_STR = "/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAlAFYDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD";

    private static volatile HttpHelper instance;

    private OkHttpClient okHttpClient;
    private HfCookieJar mCookieJar;

    private HttpHelper() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mCookieJar = new HfCookieJar();
        builder.connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addNetworkInterceptor(loggingInterceptor)
        .cookieJar(mCookieJar);
        okHttpClient = builder.build();
    }

    public static HttpHelper getInstance() {
        if (instance == null) {
            synchronized (HttpHelper.class) {
                if (instance == null) {
                    instance = new HttpHelper();
                }
            }
        }
        return instance;
    }

    public String getHfMaskReservationCookie() {
        String url = Constants.BASE_URL + "/mask/captcha";
        mCookieJar.clearCookie();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", USER_AGENT)
                .get()
                .build();

        String result = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                result = response.body().string();
                response.body().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<MaskInfo> getMaskCounts(int pharmacyCode) {
        String url = Constants.BASE_URL + "/mask/pharmacy-stock?code=" + pharmacyCode;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", USER_AGENT)
                .get()
                .build();
        String result = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                result = response.body().string();
                response.body().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getMaskCounts(), result=" + result);
        return ParseUtil.parseMaskInfo(result);
    }

    public List<Pharmacy> getPharmacies(String keyWord) {
        String url = Constants.BASE_URL + "/mask/pharmacy-search";
        FormBody formBody = new FormBody.Builder()
                .add("pharmacyName", keyWord)
                .add("areaId", "")
                .add("pageNum", "1")
                .add("pageSize", "100")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", USER_AGENT)
                .post(formBody)
                .build();
        String result = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                result = response.body().string();
                response.body().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getPharmacies(), result=" + result);
        return ParseUtil.parsePharmacyInfo(result);
    }

    public Bitmap getCaptchaImage() {
        mCookieJar.clearCookie();
        Log.d(TAG, "getCaptchaImage(), requestTime=" + FileUtil.getCurrentTime());
        String url = Constants.BASE_URL + "/mask/captcha";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", USER_AGENT)
                .addHeader("Accept-Encoding", "identity")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .get()
                .build();
        Bitmap bitmap = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            Log.d(TAG, "getCaptchaImage(), responseTime=" + FileUtil.getCurrentTime());
            if (response != null && response.isSuccessful()) {
                String base64String = response.body().string();
                String base64String1 = base64String.replace("/\n", "");
                String[] tmps = base64String1.split("EAPwD");
                String newBase64String = START_STR + tmps[1];

                byte[] bytes = Base64.decode(newBase64String, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                response.body().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public ReserveResult sendReserveData(UserSendData sendData) {
        String url = Constants.BASE_URL + "/mask/book";
        long timestamp = System.currentTimeMillis() - 20000;
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");

        sendData.setTimestamp(String.valueOf(timestamp));
        sendData.setHash(getHash(timestamp));

        RequestBody requestBody = RequestBody.create(mediaType, sendData.toJson());
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", USER_AGENT)
                .post(requestBody)
                .build();
        String result = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                result = response.body().string();
                response.body().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "sendReserveData(), result=" + result);
        return ParseUtil.parseReserveResult(result);
    }

    private String getHash(long timestamp) {
        String str = timestamp + "c7c7405208624ed90976f0672c09b884";
        return md5Decode32(str);
    }

    public String md5Decode32(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException",e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
        //对生成的16字节数组进行补零操作
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10){
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static class HfCookieJar implements CookieJar {

        private final Map<String, List<Cookie>> cookieStore = new HashMap<>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.put(url.host(), cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url.host());
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }

        public void clearCookie() {
            cookieStore.clear();
        }

        public Map<String, List<Cookie>> getCookie() {
            return cookieStore;
        }
    }

}
