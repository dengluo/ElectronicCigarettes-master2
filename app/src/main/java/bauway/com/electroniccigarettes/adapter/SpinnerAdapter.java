package bauway.com.electroniccigarettes.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import bauway.com.electroniccigarettes.R;

/**
 * Created by zhaotaotao on 2017/8/12.
 */

public class SpinnerAdapter extends ArrayAdapter<String> {

    private String[] mStrings;


    public SpinnerAdapter(Context context, String[] strings) {
        super(context, android.R.layout.simple_spinner_item, strings);
        mStrings = strings;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        //设置列表中字体的颜色
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(mStrings[position]);
        tv.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.white_1));
        tv.setTextSize(14f);

        return convertView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        //设置被选中后，最终显示的字体颜色
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(mStrings[position]);
        tv.setTextSize(14f);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.white_1));

        return convertView;
    }
}
