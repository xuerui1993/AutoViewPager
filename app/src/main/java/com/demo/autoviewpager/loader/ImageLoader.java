package com.demo.autoviewpager.loader;

/*
 *  @项目名：  AutoViewPager 
 *  @包名：    com.demo.autoviewpager.loader
 *  @文件名:   ImageLoader
 *  @创建者:   xuerui
 *  @创建时间:  2017/10/5 13:30
 *  @描述：   ImageLoader抽象类，外部需要实现这个类去加载图片， 尽力减少对第三方库的依赖
 */

import android.content.Context;
import android.widget.ImageView;

import java.io.Serializable;

public interface ImageLoader extends Serializable {
	void displayImage(Context context, String url, ImageView imageView);
	void clearMemoryCache();
}
