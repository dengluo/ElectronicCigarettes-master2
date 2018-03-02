package bauway.com.electroniccigarettes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import bauway.com.electroniccigarettes.R;
import bauway.com.electroniccigarettes.bean.Product;
import bauway.com.electroniccigarettes.interfaces.OnRecyclerViewItemClickListener;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhaotaotao on 2017/8/10.
 * 产品列表
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {


    private List<Product> mProducts = new ArrayList<>();

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public ProductListAdapter() {

    }

    public void setData(List<Product> products) {
        mProducts.clear();
        mProducts.addAll(products);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.mBtProductItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, (Integer) view.getTag());
                }
            }
        });
        LogUtils.d("onCreateViewHolder 执行");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String productName = mProducts.get(position).name;
        holder.mTvProductNameOne.setText(productName);
        holder.mTvProductNameTwo.setText(productName);
        LogUtils.d("onBindViewHolder productName:" + productName);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if (mProducts != null) {
            return mProducts.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public Product getItem(int position) {
        return mProducts.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_product_name_one)
        TextView mTvProductNameOne;
        @BindView(R.id.tv_product_name_two)
        TextView mTvProductNameTwo;
        @BindView(R.id.bt_product_item)
        LinearLayout mBtProductItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
