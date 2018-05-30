package bauway.com.electroniccigarettes.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bauway.com.electroniccigarettes.R;
import bauway.com.electroniccigarettes.base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/8/11.
 * 关注我们
 */

public class ConcernUsActivity extends BaseActivity {
    @BindView(R.id.action_back)
    ImageView action_back;
    @BindView(R.id.rl_concern_us1)
    RelativeLayout rl_concern_us1;
    @BindView(R.id.rl_concern_us2)
    RelativeLayout rl_concern_us2;
    @BindView(R.id.rl_concern_us3)
    RelativeLayout rl_concern_us3;
    @BindView(R.id.rl_concern_us4)
    RelativeLayout rl_concern_us4;

    @Override
    protected int getLayoutRes() {
        return R.layout.concern_us_activity;
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

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    @OnClick({R.id.action_back, R.id.rl_concern_us1, R.id.rl_concern_us2, R.id.rl_concern_us3, R.id.rl_concern_us4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.action_back:
                this.finish();
                break;
            case R.id.rl_concern_us1:
                jumpWeb("https://www.youtube.com/c/zhounicociggo");
                break;
            case R.id.rl_concern_us4:
                jumpWeb("https://www.instagram.com/herbstick/");
                break;
            case R.id.rl_concern_us2:
                jumpWeb("https://www.facebook.com/bauway.ecigarette");
                break;
            case R.id.rl_concern_us3:
                jumpWeb("https://twitter.com/Herbstick");
                break;
        }
    }

}
