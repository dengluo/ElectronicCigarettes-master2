package bauway.com.electroniccigarettes.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import bauway.com.electroniccigarettes.R;
import bauway.com.electroniccigarettes.base.BaseActivity;
import bauway.com.electroniccigarettes.common.MyConstants2;
import bauway.com.electroniccigarettes.util.DateUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/8/11.
 * 口数(暂时取消)
 */

public class UserUseNumberActivity extends BaseActivity {

    @BindView(R.id.bt_return)
    ImageButton mBtReturn;
    @BindView(R.id.relativeLayout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.tv_every_day_num)
    TextView mTvEveryDayNum;
    @BindView(R.id.tv_day_save_num)
    TextView mTvDaySaveNum;
    @BindView(R.id.linearLayout1)
    LinearLayout mLinearLayout1;
    @BindView(R.id.tv_every_week_num)
    TextView mTvEveryWeekNum;
    @BindView(R.id.tv_every_week_save_num)
    TextView mTvEveryWeekSaveNum;
    @BindView(R.id.linearLayout2)
    LinearLayout mLinearLayout2;
    @BindView(R.id.tv_every_month_num)
    TextView mTvEveryMonthNum;
    @BindView(R.id.tv_every_month_save_num)
    TextView mTvEveryMonthSaveNum;
    @BindView(R.id.linearLayout3)
    LinearLayout mLinearLayout3;
    @BindView(R.id.tv_count_all_save_sum)
    TextView mTvCountAllSaveSum;
    @BindView(R.id.linearLayout4)
    LinearLayout mLinearLayout4;
    @BindView(R.id.linearLayout5)
    LinearLayout mLinearLayout5;

    @Override
    protected int getLayoutRes() {
        return R.layout.use_number_activity;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        Integer price = userRxPreferences.getInteger(MyConstants2.SP_PRICE_PER_PACKET, MyConstants2.DEFAULT_PRICE_PER_PACKET).get();
        Integer dayTotalCount = userRxPreferences.getInteger(MyConstants2.SP_PUFF_PER_DAY, MyConstants2.DEFAULT_PUFF_PER_DAY).get();
        Integer everyLoop = userRxPreferences.getInteger(MyConstants2.SP_PUFF_PER_LOOP, MyConstants2.DEFAULT_PUFF_PER_LOOP).get();

        /*
         开机次数 = 每日口数计划／每循环口数计划
         每日省钱 = 开机次数 * 每包烟的价格 ／ 20 * 0.66
         */
        Double dayBoot = null;
        if (dayTotalCount != null && everyLoop != null && price != null) {
            dayBoot = Math.ceil(dayTotalCount.doubleValue() / everyLoop.doubleValue());
            Double daySaveMoney = (dayBoot) * price / 20 * 0.66;
            daySaveMoney = (double) Math.round(daySaveMoney * 100) / 100;
            mTvEveryDayNum.setText(String.valueOf(dayBoot.intValue()));
            mTvDaySaveNum.setText(String.valueOf(daySaveMoney));

            Calendar calendar = Calendar.getInstance();
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            int temp;
            if (week == 1) {
                temp = -6;
            } else {
                temp = -(week - 2);
            }
            calendar.add(Calendar.DAY_OF_YEAR, temp);
            String weekStartMon = DateUtils.conversionTime5(calendar.getTimeInMillis());
            String weekEndSun = DateUtils.conversionTime5(System.currentTimeMillis());


        }


    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @OnClick(R.id.bt_return)
    public void onViewClicked() {
        this.finish();
    }
}
