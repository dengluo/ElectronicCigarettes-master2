package bauway.com.electroniccigarettes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import bauway.com.electroniccigarettes.R;
import bauway.com.electroniccigarettes.adapter.ProductListAdapter;
import bauway.com.electroniccigarettes.base.BaseActivity;
import bauway.com.electroniccigarettes.bean.Product;
import bauway.com.electroniccigarettes.common.MyConstants;
import bauway.com.electroniccigarettes.common.MyConstants2;
import bauway.com.electroniccigarettes.interfaces.OnRecyclerViewItemClickListener;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/8/11.
 * 产品选择界面
 */

public class ProductSelectActivity extends BaseActivity {


    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.relativeLayout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.bt_return_1)
    ImageButton mBtReturn1;

    private ProductListAdapter productListAdapter;


    @Override
    protected int getLayoutRes() {
        return R.layout.product_select_activity;
    }


    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        productListAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Product product = productListAdapter.getItem(position);
                userRxPreferences.getString(MyConstants2.SELECT_PRODUCT_NAME).set(product.name);
                startActivity(new Intent(ProductSelectActivity.this, BindDeviceActivity.class).putExtra(MyConstants2.PRODUCT, product));
            }

            @Override
            public void onItemChildClick(View view, int position) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(MyConstants.CS_BOX, "BLE to UART_2"));
        products.add(new Product(MyConstants.BOX_PLUS, "BLE to UART_2"));
        products.add(new Product(MyConstants.PAD, "BLE to UART_2"));
        products.add(new Product(MyConstants.SLIDER, "BLE to UART_2"));
        productListAdapter.setData(products);

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mRecycleView.setLayoutManager(new GridLayoutManager(this, 2));
        productListAdapter = new ProductListAdapter();
        mRecycleView.setAdapter(productListAdapter);

    }


    @OnClick(R.id.bt_return_1)
    public void onViewClicked() {
        this.finish();
    }
}
