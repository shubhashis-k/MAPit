package com.example.MAPit.MAPit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by shubhashis on 2/20/2015.
 */
public class ImageConverter {

    public static String imageToStringConverter(Bitmap image){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String imageToString = Base64.encodeToString(byteArray, Base64.NO_WRAP);
        long lengthImg = imageToString.length();
        return imageToString;
    }

    public static Bitmap stringToimageConverter(String imageString){
        byte[] stringTobyte = Base64.decode(imageString, Base64.NO_WRAP);
        Bitmap bmp = BitmapFactory.decodeByteArray(stringTobyte, 0, stringTobyte.length);
        return bmp;
    }

}
