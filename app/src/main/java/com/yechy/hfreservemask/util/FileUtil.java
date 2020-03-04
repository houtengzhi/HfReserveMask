package com.yechy.hfreservemask.util;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cloud on 2020-02-23.
 */
public class FileUtil {

    public static File saveImage(String path, Bitmap bitmap) {
        String fileName = "captcha_" + System.currentTimeMillis() + ".png";
        File imageFile = new File(path, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    public static String getFormatTime(long millis) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        return format.format(new Date(millis));
    }

    public static String getCurrentTime() {
        return getFormatTime(System.currentTimeMillis());
    }
}
