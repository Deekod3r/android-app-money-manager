package com.project.hucemoney.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.project.hucemoney.R;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.common.enums.DialogType;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

public class FunctionUtils {

    public static void hideKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static String md5(String text) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showDialogNotify(Context context, String title, String message, DialogType dialogType) {
        SpannableString spannableTitle = new SpannableString("  " + title);
        spannableTitle.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.white)), 0, spannableTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableMessage = new SpannableString(message);
        spannableMessage.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.white)), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int textSizeMessageInSp = 16;
        int textSizeMessageInPx = (int) (textSizeMessageInSp * context.getResources().getDisplayMetrics().scaledDensity);
        spannableMessage.setSpan(new AbsoluteSizeSpan(textSizeMessageInPx), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        int dialogBackgroundResource = R.drawable.custom_dialog_bg;
        int positiveButtonColor = R.color.white;
        Drawable dialogIcon = null;

        switch (dialogType) {
            case INFO:
                dialogIcon = ContextCompat.getDrawable(context, R.drawable.baseline_notify_infor_sky_blue_32);
                break;
            case WARNING:
                dialogIcon = ContextCompat.getDrawable(context, R.drawable.baseline_notify_warning_yellow_32);
                break;
            case ERROR:
                dialogIcon = ContextCompat.getDrawable(context, R.drawable.baseline_notify_error_red_32);
                break;
        }

        if (dialogIcon != null) {
            dialogIcon.setBounds(0, 0, dialogIcon.getIntrinsicWidth(), dialogIcon.getIntrinsicHeight());
            ImageSpan imageSpan = new ImageSpan(dialogIcon, ImageSpan.ALIGN_BASELINE);
            spannableTitle.setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                .setTitle(spannableTitle)
                .setMessage(spannableMessage)
                .setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(dialogBackgroundResource);
        }

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setTextColor(ContextCompat.getColor(context, R.color.white));
        });

        dialog.show();
    }

    public static void showDialogConfirm(Context context, String title, String message, DialogType dialogType,
                                         DialogInterface.OnClickListener positiveCallback,
                                         DialogInterface.OnClickListener negativeCallback) {
        SpannableString spannableTitle = new SpannableString("  " + title);
        spannableTitle.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.white)), 0, spannableTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableMessage = new SpannableString(message);
        spannableMessage.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.white)), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int textSizeMessageInSp = 16;
        int textSizeMessageInPx = (int) (textSizeMessageInSp * context.getResources().getDisplayMetrics().scaledDensity);
        spannableMessage.setSpan(new AbsoluteSizeSpan(textSizeMessageInPx), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        int dialogBackgroundResource = R.drawable.custom_dialog_bg;
        int positiveButtonColor = R.color.white;
        Drawable dialogIcon = null;

        switch (dialogType) {
            case INFO:
                dialogIcon = ContextCompat.getDrawable(context, R.drawable.baseline_notify_infor_sky_blue_32);
                break;
            case WARNING:
                dialogIcon = ContextCompat.getDrawable(context, R.drawable.baseline_notify_warning_yellow_32);
                break;
            case ERROR:
                dialogIcon = ContextCompat.getDrawable(context, R.drawable.baseline_notify_error_red_32);
                break;
        }

        if (dialogIcon != null) {
            dialogIcon.setBounds(0, 0, dialogIcon.getIntrinsicWidth(), dialogIcon.getIntrinsicHeight());
            ImageSpan imageSpan = new ImageSpan(dialogIcon, ImageSpan.ALIGN_BASELINE);
            spannableTitle.setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                .setTitle(spannableTitle)
                .setMessage(spannableMessage)
                .setPositiveButton("Đồng ý", positiveCallback)
                .setNegativeButton("Hủy", negativeCallback)
                .setOnDismissListener(dialog -> negativeCallback.onClick(dialog, 0));

        AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(dialogBackgroundResource);
        }

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            positiveButton.setTextColor(ContextCompat.getColor(context, positiveButtonColor));
            negativeButton.setTextColor(ContextCompat.getColor(context, positiveButtonColor));
        });

        dialog.show();
    }

    public static void showDialogDate(Context context, EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                R.style.DatePickerDialog,
                (view, year1, month1, dayOfMonth) -> {
                    String formattedDay = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                    String formattedMonth = ((month1 + 1) < 10) ? "0" + (month1 + 1) : String.valueOf(month1 + 1);

                    editText.setTag(String.format("%s/%s/%d", formattedDay, formattedMonth, year1));
                    editText.setText(String.format("%s, %s/%s/%d", Constants.DAY_OF_WEEK[dayOfWeek - 1], formattedDay, formattedMonth, year1));
                },
                year, month, day
        );
        datePickerDialog.show();
        Button negativeButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(ContextCompat.getColor(context, R.color.blue));
        Button positiveButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(ContextCompat.getColor(context, R.color.blue));
        Window window = datePickerDialog.getWindow();
    }


    public static String randomCodeVerify() {
        Random random = new Random();
        int randomNumber = random.nextInt(10000);
        return String.format("%04d", randomNumber);
    }

    public static String getAppVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "N/A";
        }
    }

    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
