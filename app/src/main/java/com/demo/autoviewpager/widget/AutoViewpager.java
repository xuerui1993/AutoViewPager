package com.demo.autoviewpager.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.demo.autoviewpager.R;

import java.util.ArrayList;
import java.util.List;

/*
 *  @项目名：  huoban 
 *  @包名：    huoban.com.gongshe.ui.view
 *  @文件名:   AutoViewpager
 *  @创建者:   xuerui
 *  @创建时间:  2017/4/1 16:42
 *  @描述：    TODO
 */
public class AutoViewpager extends RelativeLayout implements ViewPager.OnPageChangeListener, View.OnTouchListener {
	private static final String TAG = "AutoViewpager";
	private static final int NORMAL_DURATION = 5000;
	private static final int START_DURATION = 2000;
	private AutoViewPagerAdpater mAdvertAdapter;
	private ImageView mCurrentDot;
	private SwitchPagerTask mSwitchPagerTask;
	private int mCurrentPage;
	private int mCount;
	private ViewPager mViewPager;
	private LinearLayout mLlDot;
	private int mDotSize;
	private int mDuration;
	private int mDotDrawableRes = R.drawable.dot_selector;
	private int DOT_NORAML_SIZE = getResources().getDimensionPixelSize(R.dimen.top_dot_size);
	private boolean mIsAuto;
	private TextView mTvLabel;
	private List<String> mLabelList;
	String mLabelColor = "#ffffff";

	public AutoViewpager(Context context) {
		this(context, null);
	}

	public AutoViewpager(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = View.inflate(context, R.layout.view_auto_viewpager, this);
		mViewPager = (ViewPager) view.findViewById(R.id.top_view_pager);
		mLlDot = (LinearLayout) view.findViewById(R.id.ll_dot);
		mTvLabel = (TextView) view.findViewById(R.id.tv_label);
		initListener();

		//读取属性
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AutoViewpager);
		mDuration = ta.getInteger(R.styleable.AutoViewpager_duration, NORMAL_DURATION);
		mDotSize = ta.getDimensionPixelSize(R.styleable.AutoViewpager_dotSize, DOT_NORAML_SIZE);
		mIsAuto = ta.getBoolean(R.styleable.AutoViewpager_isAuto, true);
	}

	public void setDotImageResourse(int res) {
		mDotDrawableRes = res;
	}

	public void setIsAuto(boolean isAuto) {
		mIsAuto = isAuto;
	}

	public void setDotSize(int sise) {
		mDotSize = sise;
	}

	public void setDuration(int duration) {
		mDuration = duration;
	}
	public void setLabelColor(String labelColor){
		mLabelColor = labelColor;
	}
	public void setLaBelData(List<String> list) {
		mLabelList = list;
	}

	private void initListener() {
		mViewPager.setOnTouchListener(this);
		mViewPager.addOnPageChangeListener(this);
	}

	public void setData(List<String> list) {
		if (list == null || list.size() == 0) return;
		List<ImageView> imageList = new ArrayList<>();
		mCount = list.size();
		for (String listBean : list) {
			ImageView imageView = new ImageView(getContext());
			imageList.add(imageView);
		}
		mAdvertAdapter = new AutoViewPagerAdpater(getContext(), list, imageList);
		mViewPager.setAdapter(mAdvertAdapter);
		//初始化小圆点
		initDotContaner(list);
		//		int middle = Integer.MAX_VALUE / 2;
		//		int extra = middle % mCount;
		//		mTopViewPager.setCurrentItem(mCount*10000);
		//马上开始自动轮播功能
		if (mIsAuto) {
			autoChangePage();
		}
		mTvLabel.setTextColor(Color.parseColor(mLabelColor));
	}

	private void autoChangePage() {
		if (mSwitchPagerTask == null) {
			mSwitchPagerTask = new SwitchPagerTask();
		}
		mSwitchPagerTask.start();
	}

	private void initDotContaner(List<String> list) {
		mLlDot.removeAllViews();
		ArrayList<ImageView> imageList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			ImageView imageView = new ImageView(getContext());
			imageView.setImageResource(mDotDrawableRes);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDotSize, mDotSize);
			imageList.add(imageView);
			if (i != 0) {
				params.leftMargin = mDotSize;
			} else {
				//默认第一个小圆点被选中
				mCurrentDot = imageView;
				imageView.setSelected(true);
			}
			imageView.setLayoutParams(params);
			mLlDot.addView(imageView);
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		if (mLabelList != null && mLabelList.size() > position) {
			mTvLabel.setText(mLabelList.get(position)+"");
		}
		mCurrentDot.setSelected(false);
		ImageView imageView = (ImageView) mLlDot.getChildAt(position);
		imageView.setSelected(true);
		mCurrentDot = imageView;
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.d(TAG, "onTouch: down--");
				if (mSwitchPagerTask != null) {
					mSwitchPagerTask.stop();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				Log.d(TAG, "onTouch: move--");
				if (mSwitchPagerTask != null) {
					mSwitchPagerTask.stop();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (mSwitchPagerTask != null) {
					mSwitchPagerTask.start();
				}
				Log.d(TAG, "onTouch: up--");
				break;
			case MotionEvent.ACTION_CANCEL:
				Log.d(TAG, "onTouch: cancel--");
				//mSwitchPagerTask.start();
				break;
		}
		return false;
	}


	/**
	 * 图片自动切换任务
	 */
	class SwitchPagerTask extends Handler implements Runnable {

		@Override
		public void run() {
			int currentItem = mViewPager.getCurrentItem();
			if (currentItem == mAdvertAdapter.getCount() - 1) {
				mViewPager.setCurrentItem(0);
			} else {
				mViewPager.setCurrentItem(currentItem + 1);
			}
			//在执行一次post ， 循环执行
			postDelayed(this, mDuration);
		}

		/**
		 * 开始切换
		 */
		public void start() {
			//停掉以前的任务
			removeCallbacks(this);
			//在执行一次post ， 循环执行
			postDelayed(this, START_DURATION);
		}

		/**
		 * 停止切换
		 */
		public void stop() {
			//停掉以前的任务
			removeCallbacks(this);
		}
	}

	public void onDestory() {
		mSwitchPagerTask.stop();
		mSwitchPagerTask = null;
		mViewPager.removeOnPageChangeListener(this);
		mItemClickListener = null;
	}

	public interface ItemClickListener {
		void OnItemClickListener(int postion);
	}

	ItemClickListener mItemClickListener;

	public void setItemClickListener(ItemClickListener itemClickListener) {
		mItemClickListener = itemClickListener;
	}

	public class AutoViewPagerAdpater extends PagerAdapter {
		private static final String TAG = "AutoViewPagerAdpater";
		Context mContext;
		List<String> mList;
		List<ImageView> mImageList;

		public AutoViewPagerAdpater(Context context, List<String> list, List<ImageView> imageList) {
			mContext = context;
			mList = list;
			mImageList = imageList;
		}

		@Override
		public int getCount() {
			if (mList != null) {
				return mList.size();
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			ImageView imageView = mImageList.get(position);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			container.addView(imageView);
			String url = mList.get(position);
			Glide.with(mContext).load(url).into(imageView);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mItemClickListener != null) {
						mItemClickListener.OnItemClickListener(position);
					}
				}
			});
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

}
