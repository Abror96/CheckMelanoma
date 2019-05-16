package com.era.checkmelanoma.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import static com.era.checkmelanoma.utils.Constants.QUALITY_PERCENT_AVG;
import static com.era.checkmelanoma.utils.Constants.QUALITY_PERCENT_MAX;
import static com.era.checkmelanoma.utils.Constants.QUALITY_PERCENT_MIN;


public class CompressAndConvertBitmap {

    public static MultipartBody.Part compressBitmap(Bitmap selectedImageBmp,
                                                    String nameMultipartParam, Context context) {
        MultipartBody.Part body = null;
        Bitmap resizedBitmap;


        if (selectedImageBmp != null) {
            int width = selectedImageBmp.getWidth(), height = selectedImageBmp.getHeight();

            Log.d("LOGGERR", "width: " + width + " height: " + height);

            if (selectedImageBmp.getWidth() >= 800 && selectedImageBmp.getWidth() < 1500) {
                width = (int)(selectedImageBmp.getWidth()*0.5);
                height = (int)(selectedImageBmp.getHeight()*0.5);
            } else if (selectedImageBmp.getWidth() >= 1500) {
                width = (int)(selectedImageBmp.getWidth()*0.25);
                height = (int)(selectedImageBmp.getHeight()*0.25);
            }

            resizedBitmap = Bitmap.createScaledBitmap(selectedImageBmp, width, height, true);

            File myDir = new File(context.getFilesDir() + "/checkMelanoma");
            myDir.mkdirs();

            File f = new File(myDir, "image"+UUID.randomUUID() +".jpg");
            try {
                f.createNewFile();

                //Convert bitmap to byte array
                FileOutputStream fos = new FileOutputStream(f);
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY_PERCENT_MAX /*ignored for PNG*/, fos);
                if (selectedImageBmp.getWidth() >= 800 && selectedImageBmp.getWidth() < 1500) {
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY_PERCENT_AVG /*ignored for PNG*/, fos);
                } else if (selectedImageBmp.getWidth() >= 1500) {
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY_PERCENT_MIN /*ignored for PNG*/, fos);
                }
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("LOGGER UPLOAD IMAGE", "compressBitmap: ошибка при создании файла");
            }

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"), f);
            body = MultipartBody.Part.createFormData(nameMultipartParam, "image"+UUID.randomUUID(), reqFile);


        }

        return body;
    }
}
