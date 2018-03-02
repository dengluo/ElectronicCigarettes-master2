package bauway.com.electroniccigarettes.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bestmafen.smablelib.component.SimpleSmaCallback;
import com.bestmafen.smablelib.component.SmaCallback;
import com.bestmafen.smablelib.component.SmaManager;

import java.util.Calendar;

import bauway.com.electroniccigarettes.R;
import bauway.com.electroniccigarettes.base.BaseActivity;
import bauway.com.electroniccigarettes.bean.ChargeEntity;
import bauway.com.electroniccigarettes.bean.CleanEntity;
import bauway.com.electroniccigarettes.util.PreferencesUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/8/11.
 * 提醒
 */
public class NotificationActivity extends BaseActivity {
    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.relativeLayout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.bt_return_1)
    ImageButton mBtReturn1;
    @BindView(R.id.relativeLayout1)
    RelativeLayout mRelativeLayout1;
    @BindView(R.id.show_rl_loe_power_warning)
    RelativeLayout mShowRlLoePowerWarning;
    @BindView(R.id.show_rl_is_need_charge)
    RelativeLayout mShowRlIsNeedCharge;
    @BindView(R.id.show_rl_is_need_clean)
    RelativeLayout mShowRlIsNeedClean;
    @BindView(R.id.show_rl_is_need_charge_maintenance)
    RelativeLayout mShowRlIsNeedChargeMaintenance;
    @BindView(R.id.tv_surplus_num)
    TextView mTvSurplusNum;
    @BindView(R.id.show_rl_is_surplus_warning)
    RelativeLayout mShowRlIsSurplusWarning;

    private SmaManager mSmaManager;
    private SmaCallback mSmaCallback;
    private Calendar calendar;
    private int year;

    @Override
    protected int getLayoutRes() {
        return R.layout.notification_activity;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        updateClean();
        updateCharge();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mSmaManager = SmaManager.getInstance().addSmaCallback(mSmaCallback = new SimpleSmaCallback() {

            @Override
            public void onReadVoltage(float voltage) {
                if (voltage >= 4f) {
                    mShowRlLoePowerWarning.setBackgroundResource(R.drawable.shape_bt_bg_green);
                    mShowRlIsNeedCharge.setBackgroundResource(R.drawable.shape_bt_bg_green);
                } else if (voltage >= 3.7f) {
                    mShowRlLoePowerWarning.setBackgroundResource(R.drawable.shape_bt_bg_blue);
                    mShowRlIsNeedCharge.setBackgroundResource(R.drawable.shape_bt_bg_blue);
                } else {
                    mShowRlLoePowerWarning.setBackgroundResource(R.drawable.shape_bt_bg_red);
                    mShowRlIsNeedCharge.setBackgroundResource(R.drawable.shape_bt_bg_red);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSmaManager.removeSmaCallback(mSmaCallback);
    }

    private void updateClean() {
        CleanEntity cleanEntity = PreferencesUtils.getEntity(this, CleanEntity.class);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        if (year > cleanEntity.getYear() || (year == cleanEntity.getYear() && week > cleanEntity.getWeek())) {
            mShowRlIsNeedClean.setBackgroundResource(R.drawable.left_red);
        } else {
            mShowRlIsNeedClean.setBackgroundResource(R.drawable.left_blue);
        }
    }

    private void updateCharge() {
        ChargeEntity cleanEntity = PreferencesUtils.getEntity(this, ChargeEntity.class);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        if (year > cleanEntity.getYear() || (year == cleanEntity.getYear() && month > cleanEntity.getMonth())) {
            mShowRlIsNeedChargeMaintenance.setBackgroundResource(R.drawable.right_red);
        } else {
            mShowRlIsNeedChargeMaintenance.setBackgroundResource(R.drawable.right_blue);
        }
    }

    @OnClick({R.id.bt_return_1, R.id.show_rl_is_need_clean, R.id.show_rl_is_need_charge_maintenance})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_return_1:
                finish();
                break;
            case R.id.show_rl_is_need_clean:
                final CleanEntity cleanEntity = PreferencesUtils.getEntity(this, CleanEntity.class);
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                final int week = calendar.get(Calendar.WEEK_OF_YEAR);
                if (year > cleanEntity.getYear() || (year == cleanEntity.getYear() && week > cleanEntity.getWeek())) {
                    new AlertDialog.Builder(this).setMessage(R.string.have_clean)
                            .setPositiveButton(R.string.cancel, null)
                            .setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cleanEntity.setYear(year);
                                    cleanEntity.setWeek(week);
                                    PreferencesUtils.putEntity(NotificationActivity.this, cleanEntity);
                                    mShowRlIsNeedClean.setBackgroundResource(R.drawable.left_blue);
                                }
                            }).show();
                }
                break;
            case R.id.show_rl_is_need_charge_maintenance:
                final ChargeEntity chargeEntity = PreferencesUtils.getEntity(this, ChargeEntity.class);
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                if (year > chargeEntity.getYear() || (year == chargeEntity.getYear() && month > chargeEntity.getMonth())) {
                    new AlertDialog.Builder(this).setMessage(R.string.have_charge)
                            .setPositiveButton(R.string.cancel, null)
                            .setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    chargeEntity.setYear(year);
                                    chargeEntity.setMonth(month);
                                    PreferencesUtils.putEntity(NotificationActivity.this, chargeEntity);
                                    mShowRlIsNeedChargeMaintenance.setBackgroundResource(R.drawable.right_blue);
                                }
                            }).show();
                }
                break;
        }
    }
}
