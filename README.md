# AutoViewPager
1.在布局中添加
<br><com.demo.autoviewpager.widget.AutoViewpager
    <br>android:id="@+id/auto_viewpager"
    <br>android:layout_width="match_parent"
    <br>android:layout_height="180dp"/>
<br>布局支持自定义属性
<br><com.demo.autoviewpager.widget.AutoViewpager
		<br>app:isAuto="true"  //是否轮播
		<br>app:duration="5000"  //图片切换时间
		<br>app:dotSize="8dp"    //原点大小
		<br>android:id="@+id/auto_viewpager"
		<br>android:layout_width="match_parent"
		<br>android:layout_height="180dp"/>
<br>2.在代码中使用
<br>List<String> urlList = new ArrayList<>();  //图片地址集合
<br>List<String> labelList = new ArrayList<>();  //标题集合
<br>autoViewpager.setLaBelData(urlList); //设置标题
		<br>autoViewpager.setIsAuto(true);  //设置是否轮播
		<br>autoViewpager.setDotImageResourse(R.drawable.dot_selector);  //设置圆点图片
		<br>autoViewpager.setDotSize(getResources().getDimensionPixelOffset(R.dimen.top_dot_size));  //设置圆点大小
		<br>autoViewpager.setData(urlList);  //设置图片数据集合,需在所有设置最后使用
