package bauway.com.electroniccigarettes.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import java.util.List;

import bauway.com.electroniccigarettes.R;

/**
 * FragmentTabAdapter Author: danny
 * 
 */
public class FragmentTabAdapter implements RadioGroup.OnCheckedChangeListener {
	private List<Fragment> fragments; // 一个tab页面对应一个Fragment
	private RadioGroup rgs; // 用于切换tab
	private FragmentActivity fragmentActivity; // Fragment所属的Activity
	private int fragmentContentId; // Activity中所要被替换的区域的id
	private int currentTab; // 当前Tab页面索引

	private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener; // 用于让调用者在切换tab时候增加新的功能
	private FragmentTransaction ft;

	public FragmentTabAdapter(FragmentActivity fragmentActivity,
                              List<Fragment> fragments, int fragmentContentId, RadioGroup rgs) {
		this.fragments = fragments;
		this.rgs = rgs;
		this.fragmentActivity = fragmentActivity;
		this.fragmentContentId = fragmentContentId;
		
		// 默认显示第一页
		rgs.check(rgs.getChildAt(0).getId());
		ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
		ft.add(fragmentContentId, fragments.get(0));
		ft.commit();

		rgs.setOnCheckedChangeListener(this);
	}

	/**
	 * radioButton 选中监听
	 */
	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		for (int i = 0; i < rgs.getChildCount(); i++) {
			if (rgs.getChildAt(i).getId() == checkedId) {
				showTab(i);
//				 Fragment fragment = fragments.get(i);
//				 FragmentTransaction ft = obtainFragmentTransaction(i);
//				
//				 ft.replace(fragmentContentId, fragment);
//				 ft.commit();
				// 如果设置了切换tab额外功能功能接口
				if (null != onRgsExtraCheckedChangedListener) {
					onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(
							radioGroup, checkedId, i);
				}
			}
		}

	}

	/**
	 * 切换tab（只隐藏Fragment，不重构）
	 * 
	 * @param idx
	 */
	private void showTab(int idx) {
		FragmentTransaction ft = obtainFragmentTransaction(idx);
		if (!fragments.get(idx).isAdded()) { // 先判断是否被add过
			ft.hide(fragments.get(currentTab))
					.add(fragmentContentId, fragments.get(idx)).commit(); // 隐藏当前的fragment，add下一个到Activity中
		} else {
			ft.hide(fragments.get(currentTab)).show(fragments.get(idx))
					.commit(); // 隐藏当前的fragment，显示下一个
		}
		currentTab = idx; // 更新目标tab为当前tab
	}
	
	/**
	 * 获取一个带动画的FragmentTransaction
	 * 
	 * @param index
	 * @return
	 */
	private FragmentTransaction obtainFragmentTransaction(int index) {
		FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
				.beginTransaction();
		// 设置切换动画
		if (index > currentTab) {
			ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
		} else {
			ft.setCustomAnimations(R.anim.slide_right_in,
					R.anim.slide_right_out);
		}
		// currentTab=index;
		return ft;
	}

	public int getCurrentTab() {
		return currentTab;
	}

	public Fragment getCurrentFragment() {
		return fragments.get(currentTab);
	}

	public OnRgsExtraCheckedChangedListener getOnRgsExtraCheckedChangedListener() {
		return onRgsExtraCheckedChangedListener;
	}

	public void setOnRgsExtraCheckedChangedListener(
			OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
		this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
	}

	/**
	 * 切换tab额外功能功能接口
	 */
	public interface OnRgsExtraCheckedChangedListener {
		public void OnRgsExtraCheckedChanged(RadioGroup radioGroup,
                                             int checkedId, int index);
	}

}
