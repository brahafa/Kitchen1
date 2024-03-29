package com.dalpak.bringit.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.dalpak.bringit.R;
import com.dalpak.bringit.models.OpenOrderModel;
import com.dalpak.bringit.models.OrderCategoryModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.dalpak.bringit.utils.Constants.PIZZA_TYPE_BL;
import static com.dalpak.bringit.utils.Constants.PIZZA_TYPE_BR;
import static com.dalpak.bringit.utils.Constants.PIZZA_TYPE_FULL;
import static com.dalpak.bringit.utils.Constants.PIZZA_TYPE_LH;
import static com.dalpak.bringit.utils.Constants.PIZZA_TYPE_RH;
import static com.dalpak.bringit.utils.Constants.PIZZA_TYPE_TL;
import static com.dalpak.bringit.utils.Constants.PIZZA_TYPE_TR;

public class Utils {


    public static void openPermissionAlertDialog(Context context) {
        openAlertDialog(context, "You don't have permission for this action", "Permission denied");
    }

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


    public static void openAlertDialogRetry(Context context, DialogListener listener) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setMessage("בדוק חיבור לאינטרנט")
                .setPositiveButton("Retry", (dialog, which) -> listener.onRetry(true))
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> listener.onRetry(false))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public interface DialogListener {
        void onRetry(boolean isRetry);
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

    public static int getImageRes(String viewType) {
        int imageRes = R.drawable.ic_pizza_full_active;
        switch (viewType) {
            case PIZZA_TYPE_FULL:
                imageRes = R.drawable.ic_pizza_full_active;
                break;
            case PIZZA_TYPE_RH:
                imageRes = R.drawable.ic_pizza_rh_active;
                break;
            case PIZZA_TYPE_LH:
                imageRes = R.drawable.ic_pizza_lh_active;
                break;
            case PIZZA_TYPE_TR:
                imageRes = R.drawable.ic_pizza_tr_cart;
                break;
            case PIZZA_TYPE_TL:
                imageRes = R.drawable.ic_pizza_tl_cart;
                break;
            case PIZZA_TYPE_BR:
                imageRes = R.drawable.ic_pizza_br_cart;
                break;
            case PIZZA_TYPE_BL:
                imageRes = R.drawable.ic_pizza_bl_cart;
                break;
        }
        return imageRes;
    }

    public static int getImageResRect(String viewType) {
        int imageRes = R.drawable.ic_pizza_full_rect_active;
        switch (viewType) {
            case PIZZA_TYPE_FULL:
                imageRes = R.drawable.ic_pizza_full_rect_active;
                break;
            case PIZZA_TYPE_RH:
                imageRes = R.drawable.ic_pizza_rh_rect_cart;
                break;
            case PIZZA_TYPE_LH:
                imageRes = R.drawable.ic_pizza_lh_rect_cart;
                break;
            case PIZZA_TYPE_TR:
                imageRes = R.drawable.ic_pizza_tr_rect_cart;
                break;
            case PIZZA_TYPE_TL:
                imageRes = R.drawable.ic_pizza_tl_rect_cart;
                break;
            case PIZZA_TYPE_BR:
                imageRes = R.drawable.ic_pizza_br_rect_cart;
                break;
            case PIZZA_TYPE_BL:
                imageRes = R.drawable.ic_pizza_bl_rect_cart;
                break;
        }
        return imageRes;
    }

    public static double getTotalOrder(OpenOrderModel openOrderModel) {
        double sum = 0;
        for (int i = 0; i < openOrderModel.getProducts().size(); i++) {
            if(openOrderModel.getProducts().get(i).isDeleted() || openOrderModel.getProducts().get(i).isCanceled()){
                continue;
            }
            sum += Double.parseDouble(openOrderModel.getProducts().get(i).getPrice());
            sum += getTotalToppings(openOrderModel.getProducts().get(i).getCategories());
            for (int j = 0; j < openOrderModel.getProducts().get(i).getProducts().size(); j++) {//DEAL
                sum += Double.parseDouble(openOrderModel.getProducts().get(i).getProducts().get(j).getPrice());
                sum += getTotalToppings(openOrderModel.getProducts().get(i).getProducts().get(j).getCategories());
            }
        }
        return sum;
    }

    public static double getTotalToppings(List<OrderCategoryModel> categoryModels) {
        double sum = 0;
        double fixPriceCounter = 0;
        for (int i = 0; i < categoryModels.size(); i++) {
            fixPriceCounter = categoryModels.get(i).getProductsFixedPrice();
            for (int j = 0; j < categoryModels.get(i).getProducts().size(); j++) {
                if(categoryModels.get(i).getCategoryHasFixedPrice()){
                    fixPriceCounter --;
                    if(fixPriceCounter >= 0) {
                        sum += categoryModels.get(i).getFixedPrice();
                    }else{
                        sum += Double.parseDouble(categoryModels.get(i).getProducts().get(j).getPrice());
                    }
                }else{
                    sum += Double.parseDouble(categoryModels.get(i).getProducts().get(j).getPrice());
                }
            }

        }
        return sum;
    }

    public static String getVersionApp(Context context){
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

}
