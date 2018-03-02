package com.bauway.alarm.activity;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.bauway.alarm.R;
import com.bauway.alarm.adapter.RingAdapter;
import com.bauway.alarm.base.BaseActivity;
import com.bauway.alarm.bean.RingBean;
import com.bauway.alarm.common.MyConstants;
import com.bauway.alarm.interfaces.OnRecyclerViewItemClickListener;
import com.bauway.alarm.util.PreferencesUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/12/15.
 * <p>
 * 铃声选择
 */

public class RingSet extends BaseActivity {

    @BindView(R.id.bt_return)
    Button mBtReturn;
    @BindView(R.id.rl1)
    RelativeLayout mRl1;
    @BindView(R.id.line1)
    View mLine1;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private RingAdapter mRingAdapter;
    private List<RingBean> mRingBeanList = new ArrayList<>();
    private MyAudioRecord mAudioRecord = new MyAudioRecord();

    @Override
    protected int getLayoutRes() {
        return R.layout.ring_set;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        mRingAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mAudioRecord.playStop();
                for (int i = 0; i < mRingBeanList.size(); i++) {
                    RingBean bean = mRingBeanList.get(i);
                    if (bean.isSelected()) {
                        bean.setSelected(false);
                        mRingAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                RingBean ringBean = mRingBeanList.get(position);
                ringBean.setSelected(true);
                if (position == 0) {
                    //默认，不播放任何声音
                } else {
                    //播放选中的音频
                    playSelectRing(ringBean);
                }
                PreferencesUtils.putInt(mContext, MyConstants.GOAL_VOICE_MODE, position);
                setResult(RESULT_OK);
            }

            @Override
            public void onItemChildClick(View view, int position) {

            }
        });
    }

    protected void playSelectRing(RingBean ringBean) {
        try {
            mAudioRecord.playRecord(mContext.getAssets().openFd(ringBean.getRingPath()),
                    new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            LogUtils.d("播放结束");
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {
        int selectNum = PreferencesUtils.getInt(mContext, MyConstants.GOAL_VOICE_MODE, 0);
        mRingAdapter = new RingAdapter(mContext);
        mRecyclerView.setAdapter(mRingAdapter);
        mRingBeanList.add(new RingBean(getString(R.string.default_ring), "", false));
        for (int i = 1; i <= 29; i++) {
            mRingBeanList.add(new RingBean(getString(R.string.ring) + i, "ring/" + i + ".wav", false));
        }
        mRingBeanList.get(selectNum).setSelected(true);
        mRingAdapter.addList(mRingBeanList);
        mRecyclerView.smoothScrollToPosition(selectNum);
    }

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    @OnClick({R.id.bt_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_return:
                this.finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (mAudioRecord != null) {
            mAudioRecord.playStop();
        }
        super.onDestroy();

    }

    class MyAudioRecord {

        MyAudioRecord() {
        }

        private MediaPlayer mediaPlayer;

        /**
         * 播放音频
         *
         * @param listener 播放回调
         */
        void playRecord(AssetFileDescriptor afd, MediaPlayer.OnCompletionListener listener) {
            LogUtils.d("开始播放");
            if (!EmptyUtils.isEmpty(afd)) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnCompletionListener(listener);
                try {
                    mediaPlayer.setDataSource(afd.getFileDescriptor(),
                            afd.getStartOffset(), afd.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        void playStop() {
            LogUtils.d("结束播放");
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }


}
