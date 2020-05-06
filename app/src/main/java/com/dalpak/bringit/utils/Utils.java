package com.dalpak.bringit.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static void openAlertDialog(Context context, String msg, String title) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert
            );
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static String getOrderTimerStr(String orderTime) {
        long tms = getOrderTimerLong(orderTime);
        if (tms <= 1) {
            return "דקה";
        } else if (tms > 59) {
            return "שעה";
        }
        return tms + " דק' ";
    }

    public static long getOrderTimerLong(String orderTime) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendarForOrder = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        try {
            calendarForOrder.setTime(sdf.parse(orderTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return TimeUnit.MILLISECONDS.toSeconds(calendar.getTimeInMillis() - calendarForOrder.getTimeInMillis());
    }

}
