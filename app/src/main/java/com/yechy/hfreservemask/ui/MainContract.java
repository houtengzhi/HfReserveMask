package com.yechy.hfreservemask.ui;

import android.graphics.Bitmap;

import com.yechy.hfreservemask.BaseContract;

import java.io.File;

/**
 * Created by cloud on 2020-02-23.
 */
public interface MainContract {

    public interface IView extends BaseContract.IView {
        void showCaptchaImage(Bitmap bitmap);
        void onGetCookie();
        void onGetCaptchaFile(File file);
        void onQueryCaptchaTextSuccess(String captcha);
        void onQueryCaptchaTextFailed();
        void showLog(String message);
        void clearLog();
    }

    public interface IPresenter extends BaseContract.IPreseneter {
    }
}
