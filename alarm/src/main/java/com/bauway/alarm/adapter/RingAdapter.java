package com.bauway.alarm.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bauway.alarm.R;
import com.bauway.alarm.bean.RingBean;
import com.bauway.alarm.interfaces.OnRecyclerViewItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhaotaotao on 2017/12/19.
 */

public class RingAdapter extends RecyclerView.Adapter<RingAdapter.MyViewHolder> {

    private Context mContext;
    private List<RingBean> mRingBeanList;

    public RingAdapter(Context context) {
        mContext = context;
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void addList(List<RingBean> list) {
        mRingBeanList = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ring_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        RingBean ringBean = mRingBeanList.get(position);
        holder.mTvSingNum.setText(ringBean.getRingName());
        if (ringBean.isSelected()) {
            holder.mImageView.setVisibility(View.VISIBLE);
        } else {
            holder.mImageView.setVisibility(View.INVISIBLE);
        }
        holder.mRingItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    holder.mImageView.setVisibility(View.VISIBLE);
                    //注意这里使用getTag方法获取数据
                    mOnItemClickListener.onItemClick(view, (Integer) view.getTag());

                }
            }
        });
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if (mRingBeanList != null) {
            return mRingBeanList.size();
        }
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_sing_num)
        TextView mTvSingNum;
        @BindView(R.id.imageView)
        ImageView mImageView;
        @BindView(R.id.ring_item)
        ConstraintLayout mRingItem;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
