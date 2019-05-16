package com.era.checkmelanoma.utils;


import android.app.ProgressDialog;
import android.content.Context;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Constants {


    public static String getDate(long timeStamp){

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

    public static ProgressDialog initProgressDialog(Context context, String text) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(text);
        return progressDialog;
    }

}
