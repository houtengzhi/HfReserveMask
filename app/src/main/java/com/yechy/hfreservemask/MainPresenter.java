package com.yechy.hfreservemask;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.WordSimple;
import com.yechy.hfreservemask.entry.MaskInfo;
import com.yechy.hfreservemask.entry.Pharmacy;
import com.yechy.hfreservemask.entry.User;
import com.yechy.hfreservemask.entry.UserSendData;
import com.yechy.hfreservemask.ui.MainContract;
import com.yechy.hfreservemask.util.FileUtil;
import com.yechy.hfreservemask.util.ThreadUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cloud on 2020-02-22.
 */
public class MainPresenter {

    private static final String TAG = "MainPresenter";

    private MainContract.IView mView;

    public MainPresenter(MainContract.IView view) {
        this.mView = view;
    }

    public List<User> queryUserList() {
        List<Pharmacy> pharmacies = new ArrayList<>();
        pharmacies.add(new Pharmacy("合肥大药房高新区百草店", 10807));
        pharmacies.add(new Pharmacy("立方连锁高新安医店", 10519));
        pharmacies.add(new Pharmacy("国胜大药房文曲路店", 10147));
        pharmacies.add(new Pharmacy("邻加医康复海亮九玺店", 11124));
        List<User> userList = new ArrayList<>();
        User user1 = new User("徐文昌", "340721198901010238", "13805623343", pharmacies);
        User user2 = new User("王子健", "342622199007020246", "13805623343", pharmacies);
        userList.add(user1);
        userList.add(user2);
        return userList;
    }

    public void getHfMaskReservationCookie() {
        ThreadUtil.executeByCached(new ThreadUtil.Task<Boolean>() {
            @Override
            public Boolean doInBackground() throws Throwable {
                HttpHelper.getInstance().getHfMaskReservationCookie();
                return true;
            }

            @Override
            public void onSuccess(Boolean result) {
                if (mView != null) {
                    mView.onGetCookie();
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFail(Throwable t) {

            }
        });
    }

    public void startReserve(final User user, final String captcha) {
        mView.showLog("开始预约: " + user.toString() + ", capthca=" + captcha);
            ThreadUtil.executeByCached(new ThreadUtil.Task<Object>() {
                @Override
                public Object doInBackground() throws Throwable {
                    for (Pharmacy pharmacy : user.getPharmacies()) {
                        int pharmacyCode = pharmacy.getCode();
                        List<MaskInfo> maskInfoList = HttpHelper.getInstance().getMaskCounts(pharmacyCode);
                        for (int i = 0; i < maskInfoList.size(); i++) {
                            MaskInfo maskInfo = maskInfoList.get(i);
                            if (i == 0) {
                                UserSendData sendData = new UserSendData(user, pharmacy.getName(), String.valueOf(pharmacyCode),
                                        maskInfo.getValue(), maskInfo.getText(),  captcha);
                                sendReserveData(sendData);
                                break;
                            }
                        }
                        break;

                    }
                    return null;
                }

                @Override
                public void onSuccess(Object result) {

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onFail(Throwable t) {

                }
            });
    }

    public void getMaskCounts(final int pharmacyCode) {
        ThreadUtil.executeByCached(new ThreadUtil.Task<List<MaskInfo>>() {
            @Override
            public List<MaskInfo> doInBackground() throws Throwable {
                return HttpHelper.getInstance().getMaskCounts(pharmacyCode);
            }

            @Override
            public void onSuccess(List<MaskInfo> result) {
                Log.d(TAG, "result: " + Arrays.toString(result.toArray()));
                for (MaskInfo maskInfo : result) {
                    if (maskInfo.getRemain() > 0) {

                    }
                }

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFail(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getPharmacy() {
        ThreadUtil.executeByCached(new ThreadUtil.Task<List<Pharmacy>>() {
            @Override
            public List<Pharmacy> doInBackground() throws Throwable {
                return HttpHelper.getInstance().getPharmacies("高新");
            }

            @Override
            public void onSuccess(List<Pharmacy> result) {
                Log.d(TAG, "result: " + Arrays.toString(result.toArray()));
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFail(Throwable t) {

            }
        });
    }

    public void downloadCaptchaImage() {
        ThreadUtil.executeByCached(new ThreadUtil.Task<Bitmap>() {
            @Override
            public Bitmap doInBackground() throws Throwable {
                return HttpHelper.getInstance().getCaptchaImage();
            }

            @Override
            public void onSuccess(Bitmap result) {
                if (mView != null) {
                    mView.showCaptchaImage(result);
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFail(Throwable t) {

            }
        });
    }

    private void sendReserveData(final UserSendData data) {
        if (!checkSendData(data)) {
            mView.showLog("参数有空值");
            return;
        }
        mView.showLog("" + data.toString());
        HttpHelper.getInstance().sendReserveData(data);
    }

    private boolean checkSendData(UserSendData data) {
        return data != null && !isEmpty(data.getName()) && !isEmpty(data.getCardNum()) &&
        !isEmpty(data.getPhoneNum()) && !isEmpty(data.getCaptcha());
    }

    private boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str);
    }

    public void recognizeCaptchaText(File file) {
        GeneralBasicParams params = new GeneralBasicParams();
        params.setDetectDirection(false);
        params.setImageFile(file);
        OCR.getInstance(HfApp.getContext()).recognizeAccurateBasic(params, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult generalResult) {
                Log.d(TAG, "" + generalResult.getWordList());
                List<? extends WordSimple> wordSimpleList = generalResult.getWordList();
                if (wordSimpleList.size() > 0) {
                    mView.onQueryCaptchaTextSuccess(wordSimpleList.get(0).getWords());
                }
            }

            @Override
            public void onError(OCRError ocrError) {
                Log.e(TAG, "" + ocrError.getMessage());
                mView.onQueryCaptchaTextFailed();
            }
        });
    }

    public void saveFile(final String path, final Bitmap bitmap) {
        ThreadUtil.executeByIo(new ThreadUtil.Task<File>() {
            @Override
            public File doInBackground() throws Throwable {
                return FileUtil.saveImage(path, bitmap);
            }

            @Override
            public void onSuccess(File result) {
                if (mView != null) {
                    mView.onGetCaptchaFile(result);
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFail(Throwable t) {

            }
        });
    }
}
