package com.yechy.hfreservemask.ui;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.yechy.hfreservemask.BuildConfig;
import com.yechy.hfreservemask.HttpHelper;
import com.yechy.hfreservemask.MainPresenter;
import com.yechy.hfreservemask.OutputTextView;
import com.yechy.hfreservemask.R;
import com.yechy.hfreservemask.entry.MaskInfo;
import com.yechy.hfreservemask.entry.Pharmacy;
import com.yechy.hfreservemask.entry.User;
import com.yechy.hfreservemask.entry.UserSendData;
import com.yechy.hfreservemask.util.ThreadUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.IView {

    private static final String TAG = "MainActivity";

    private MainPresenter mainPresenter;

    private ImageView mCaptchaIv;
    private EditText mCaptchaEdit;
    private OutputTextView mMessageTv;
    private Button mRefreshBtn, mReserveBtn;

    private List<User> userList;
    private UserSendData userSendData;
    private String mCaptcha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPresenter = new MainPresenter(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCaptchaIv = findViewById(R.id.iv_captcha);
        mCaptchaEdit = findViewById(R.id.edit_captcha);
        mMessageTv = findViewById(R.id.tv_message);
        mMessageTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        mRefreshBtn = findViewById(R.id.btn_refresh_captcha);
        mReserveBtn = findViewById(R.id.btn_reserve);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mReserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = userList.get(0);
                mainPresenter.startReserve(user, mCaptcha);
            }
        });

        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mainPresenter.getHfMaskReservationCookie();
                mainPresenter.downloadCaptchaImage();
            }
        });

        OCR.getInstance(getApplicationContext()).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
            }

            @Override
            public void onError(OCRError ocrError) {

            }
        }, getApplicationContext(), BuildConfig.OCR_API_KEY, BuildConfig.OCR_SECRET_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userList = mainPresenter.queryUserList();
//        mainPresenter.getHfMaskReservationCookie();
        mainPresenter.downloadCaptchaImage();
    }

    @Override
    public void onGetCookie() {
        mainPresenter.downloadCaptchaImage();
    }

    @Override
    public void showCaptchaImage(Bitmap bitmap) {
        if (bitmap != null) {
            mCaptchaIv.setImageBitmap(bitmap);
            mainPresenter.saveFile(getExternalCacheDir().getAbsolutePath(), bitmap);
        } else {
            mCaptchaIv.setImageResource(R.drawable.placeholder);
        }
    }

    @Override
    public void onGetCaptchaFile(File file) {
        mainPresenter.recognizeCaptchaText(file);
    }

    @Override
    public void onQueryCaptchaTextSuccess(String captcha) {
        mCaptchaEdit.setText(captcha);
        mCaptchaEdit.setEnabled(false);
        mCaptcha = captcha;
        Snackbar.make(mCaptchaEdit, "识别验证码成功", Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void onQueryCaptchaTextFailed() {
        mCaptchaEdit.setText(null);
        mCaptchaEdit.setEnabled(true);
        mCaptcha = null;
        Snackbar.make(mCaptchaEdit, "识别验证码失败", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLog(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mMessageTv.outputMessage(message);
            }
        });
    }

    @Override
    public void clearLog() {
        mMessageTv.setText(null);
    }

    private Handler handler = new Handler();
}
