package bauway.com.electroniccigarettes.activity;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bauway.com.electroniccigarettes.R;
import bauway.com.electroniccigarettes.base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/8/11.
 * 备注
 */

public class NotesActivity extends BaseActivity {
    @BindView(R.id.bt_return)
    ImageButton mBtReturn;
    @BindView(R.id.relativeLayout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.tv_notes)
    TextView mTvNotes;
    @BindView(R.id.nestedScrollView2)
    NestedScrollView mNestedScrollView2;
    @BindView(R.id.LinearLayout1)
    LinearLayout mLinearLayout1;

    @Override
    protected int getLayoutRes() {
        return R.layout.notes_activity;
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


    @OnClick(R.id.bt_return)
    public void onViewClicked() {
        this.finish();
    }
}
