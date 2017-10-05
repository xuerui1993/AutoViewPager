package com.demo.autoviewpager.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

import com.demo.autoviewpager.R;
import com.demo.autoviewpager.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/*
 *  @项目名：  AutoViewPager
 *  @包名：    com.demo.autoviewpager
 *  @文件名:   AutoViewpager
 *  @创建者:   xuerui
 *  @创建时间:  2017/4/7 16:42
 *  @描述：    自动轮播图实现
 */
public class AutoViewpager extends RelativeLayout implements ViewPager.OnPageChangeListener, View.OnTouchListener {
	private static final String TAG = "AutoViewpager";
	private static final int NORMAL_DURATION = 5000;
	private static final int START_DURATION = 2000;
	private static final int DOT_POSITION_LEFT = 0;
	private static final int DOT_POSITION_CENTER = 1;
	private static final int DOT_POSITION_RIGHT = 2;
	private AutoViewPagerAdpater mAdvertAdapter;
	private ImageView mCurrentDot;
	private SwitchPagerTask mSwitchPagerTask;
	private int mCurrentPage;
	private int mCount;
	private ViewPager mViewPager;
	private LinearLayout mLlDot;
	private int mDotSize;
	private int mDuration;
	private int mDotDrawableRes;
	private int DOT_NORAML_SIZE = getResources().getDimensionPixelSize(R.dimen.top_dot_size);
	private boolean mIsAuto;
	private TextView mTvLabel;
	private List<String> mLabelList;
	String mLabelColor = "#ffffff";
	private Drawable mDrawable;
	private int mDotPosition;
	private ImageLoader mImageloader;

	public AutoViewpager(Context context) {
		this(context, null);
	}

	public void setImageloader(ImageLoader imageloader) {
		this.mImageloader = imageloader;
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
		mDrawable = ta.getDrawable(R.styleable.AutoViewpager_dotSrc);
		mDotPosition = ta.getInteger(R.styleable.AutoViewpager_dotPosition, 2);
		//释放资源
		ta.recycle();
	}

	public void setDotPosition(int dotPosition) {
		mDotPosition = dotPosition;
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

	public void setLabelColor(String labelColor) {
		mLabelColor = labelColor;
	}

	public void setLaBelData(List<String> list) {
		mLabelList = list;
	}

	private void initListener() {
		mViewPager.setOnTouchListener(this);
		mViewPager.addOnPageChangeListener(this);
	}

	public void setPicList(List<String> list) {
		if (list == null || list.size() == 0) return;
		mCount = list.size();
		mAdvertAdapter = new AutoViewPagerAdpater(getContext(), list);
		mViewPager.setAdapter(mAdvertAdapter);
		//初始化小圆点
		initDotContaner(list);
		//选中中间的第一个
		int middle = Integer.MAX_VALUE / 2;
		int extra = middle % mCount;
		mViewPager.setCurrentItem(middle-extra);
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
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLlDot.getLayoutParams();
		switch (mDotPosition) {
			case 0:
				layoutParams.addRule(RelativeLayout.ALIGN_LEFT);
				break;
			case 1:
				layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
				break;
			case 2:
				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				break;
		}
		mLlDot.removeAllViews();
		ArrayList<ImageView> imageList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			ImageView imageView = new ImageView(getContext());
			if (mDotDrawableRes != 0) {
				//代码中设置了mDotDrawableRes则优先设置
				imageView.setImageResource(R.drawable.dot_selector);
			} else {
				if (mDrawable != null) {
					//在布局中设置了,则显示布局中的
					Drawable newDrawable = mDrawable.getConstantState().newDrawable();
					imageView.setImageDrawable(newDrawable);

				} else {
					//若都没设置则,设置默认
					imageView.setImageResource(R.drawable.dot_selector);
				}
			}
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
		position = position % mLabelList.size();
		if (mLabelList != null && mLabelList.size() > position) {
			mTvLabel.setText(mLabelList.get(position) + "");
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
		void OnItemClickListener(int position);
	}

	ItemClickListener mItemClickListener;

	public void setItemClickListener(ItemClickListener itemClickListener) {
		mItemClickListener = itemClickListener;
	}

	public class AutoViewPagerAdpater extends PagerAdapter {
		private static final String TAG = "AutoViewPagerAdpater";
		Context mContext;
		List<String> mList;

		public AutoViewPagerAdpater(Context context, List<String> list) {
			mContext = context;
			mList = list;
		}

		@Override
		public int getCount() {
			if (mList != null) {
				return Integer.MAX_VALUE;
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			final int picPosition = position % mList.size();
			ImageView imageView = new ImageView(mContext);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			String url = mList.get(picPosition);
			mImageloader.displayImage(mContext,url,imageView);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mItemClickListener != null) {
						mItemClickListener.OnItemClickListener(picPosition);
					}
				}
			});
			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

}
