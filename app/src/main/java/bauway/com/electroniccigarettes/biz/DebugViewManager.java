package bauway.com.electroniccigarettes.biz;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import bauway.com.electroniccigarettes.R;

/**
 * Created by Administrator on 2017/8/21.
 */

public class DebugViewManager {
    private Context mContext;
    private WindowManager mWindowManager;

    private View mDebugView;
    private TextView mTv;
    private DateFormat mDateFormat = new SimpleDateFormat("HH:mm:ss");

    public DebugViewManager(@NonNull Context context) {
        this.mContext = context;
    }

    public void showDebugWindow() {
        if (mDebugView != null) return;

        setDebugView();
        addDebugView();
    }

    public void dismissDebugWindow() {
        if (mDebugView == null) return;

        mWindowManager.removeViewImmediate(mDebugView);
        mDebugView = null;
    }

    private void setDebugView() {
        mDebugView = View.inflate(mContext, R.layout.view_debug, null);
        mTv = (TextView) mDebugView.findViewById(R.id.tv);
        mTv.setTextColor(Color.GREEN);
        mTv.append("Debug Window\n");
    }

    private void addDebugView() {
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        lp.gravity = Gravity.START | Gravity.TOP;
        lp.alpha = 0.6f;
        lp.width = 400;
        lp.height = 800;
//        lp.x = 0;
        lp.y = 200;
        mWindowManager.addView(mDebugView, lp);
    }

    /**
     * @param type 0写 1读
     * @param data content
     */
    public void append(String type, String data) {
        if (mDebugView == null) return;

        mTv.append(getTimeStr() + "  " + type + "\n");
        mTv.append(data);
        mTv.append("\n\n");
    }

    public String getTimeStr() {
        return mDateFormat.format(new Date());
    }
}
