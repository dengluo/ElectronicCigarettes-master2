package com.bauway.alarm.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bauway.alarm.R;
import com.bauway.alarm.interfaces.DialogCallback;


/**
 * Created by zhaotaotao on 10/01/2017.
 * dialog弹窗工具类
 */
public class DialogUtil {

    private static AlertDialog progressDialog;
    private static AlertDialog.Builder builder;
    private static int userSelector;

    public static void hide() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (builder != null) {
            builder = null;
        }
    }

    public static AlertDialog progressDialog(Context context, String showContent, boolean isCancelable) {
        hide();
        progressDialog = new AlertDialog.Builder(context).create();
        progressDialog.show();
        View view = View.inflate(context, R.layout.dialog_progress_view, null);
        TextView desc = (TextView) view.findViewById(R.id.progress_description);
        desc.setText(showContent);
        progressDialog.setContentView(view);
        progressDialog.setCanceledOnTouchOutside(isCancelable);
        progressDialog.setCancelable(isCancelable);
        return progressDialog;
    }

    public static void defaultDialog(Context context, String showContent,
                                     String confirm, String cancel,
                                     final DialogCallback... dc) {
        hide();
        builder = new AlertDialog.Builder(context);
        builder.setMessage(showContent);
        if (TextUtils.isEmpty(confirm)) {
            confirm = context.getString(R.string.confirm);
        }
        if (TextUtils.isEmpty(cancel)) {
            cancel = context.getString(R.string.cancel);
        }
        builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消
                if (dc.length > 1) {
                    dc[1].execute(dialog, which);
                }
            }
        });
        builder.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //确定
                if (dc.length > 0) {
                    dc[0].execute(dialog, which);
                }
            }
        });
        builder.show();
    }

    public static void defaultDialog(Context context, String[] showContent, int nowSelector,
                                     String confirm, String cancel,
                                     final DialogCallback... dc) {
        hide();
        builder = new AlertDialog.Builder(context);
        builder.setSingleChoiceItems(showContent, nowSelector, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userSelector = which;
            }
        });
        if (TextUtils.isEmpty(confirm)) {
            confirm = context.getString(R.string.confirm);
        }
        if (TextUtils.isEmpty(cancel)) {
            cancel = context.getString(R.string.cancel);
        }
        builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消
                if (dc.length > 1) {
                    dc[1].execute(dialog, which);
                }
            }
        });
        builder.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //确定
                if (dc.length > 0) {
                    dc[0].execute(dialog, userSelector);
                }
            }
        });
        builder.show();
    }

}
