package com.bauway.alarm.util.MyNumberPicker;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.bauway.alarm.R;
import com.blankj.utilcode.util.SizeUtils;

import java.lang.reflect.Field;

/**
 * Created by zhaotaotao on 12/01/2017.
 * 针对NumberPickerTools的工具类
 */
public class MyNumberPickerTools {


    /**
     * 设置NumBerPicker的统一样式
     *
     * @param context         context
     * @param viewNumberUnits viewNumBer
     */
    public static void setNumBerPickerStyle(Context context, MyNumberPicker viewNumberUnits) {
        MyNumberPickerTools.setNumberPickerDividerColor(viewNumberUnits, ContextCompat.getColor(context, R.color.white_1));
        MyNumberPickerTools.setNumberPickerDividerHeight(viewNumberUnits, SizeUtils.dp2px(0.5F));
    }

    /**
     * 设置NumberPicker字色（建议通过自定义View实现）
     *
     * @param numberPicker：NumberPicker
     * @param color：int
     * @return boolean
     */
    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                Field selectorWheelPaintField;
                try {
                    selectorWheelPaintField = numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    try {
                        ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 设置NumberPicker分割线颜色
     *
     * @param numberPicker：NumberPicker
     * @param color：int
     */
    public static void setNumberPickerDividerColor(NumberPicker numberPicker, int color) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field SelectionDividerField : pickerFields) {
            if (SelectionDividerField.getName().equals("mSelectionDivider")) {
                SelectionDividerField.setAccessible(true);
                try {
                    SelectionDividerField.set(numberPicker, new ColorDrawable(color));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * 设置NumberPicker分割线高度
     *
     * @param numberPicker：NumberPicker
     * @param height：int
     */
    public static void setNumberPickerDividerHeight(NumberPicker numberPicker, int height) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field SelectionDividerField : pickerFields) {
            if (SelectionDividerField.getName().equals("mSelectionDividerHeight")) {
                SelectionDividerField.setAccessible(true);
                try {
                    SelectionDividerField.set(numberPicker, height);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}
