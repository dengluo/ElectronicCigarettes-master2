package com.bauway.alarm.interfaces;

import android.view.View;

/**
 * Created by zhaotaotao on 16/11/18.
 */
public interface OnRecyclerViewItemClickListener {
    void onItemClick(View view, int position);

    void onItemChildClick(View view, int position);

}
