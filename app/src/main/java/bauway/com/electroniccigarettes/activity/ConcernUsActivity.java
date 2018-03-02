package bauway.com.electroniccigarettes.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    @BindView(R.id.bt_return)
    ImageButton mBtReturn;
    @BindView(R.id.tv_company_name)
    TextView mTvCompanyName;
    @BindView(R.id.linearLayout)
    LinearLayout mLinearLayout;
    @BindView(R.id.bt_youtube)
    Button mBtYoutube;
    @BindView(R.id.bt_instagram)
    Button mBtInstagram;
    @BindView(R.id.bt_facebook)
    Button mBtFacebook;
    @BindView(R.id.bt_twitter)
    Button mBtTwitter;

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


    @OnClick({R.id.bt_return, R.id.bt_youtube, R.id.bt_instagram, R.id.bt_facebook, R.id.bt_twitter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_return:
                this.finish();
                break;
            case R.id.bt_youtube:
                jumpWeb("https://www.youtube.com/c/zhounicociggo");
                break;
            case R.id.bt_instagram:
                jumpWeb("https://www.instagram.com/herbstick/");
                break;
            case R.id.bt_facebook:
                jumpWeb("https://www.facebook.com/bauway.ecigarette");
                break;
            case R.id.bt_twitter:
                jumpWeb("https://twitter.com/Herbstick");
                break;
        }
    }

}
