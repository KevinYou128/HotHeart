# HotHeart
模仿抖音APP双击屏幕蹦出心图，特点：   1. 可以自定义图片   2. 可以自定义旋转角度   3. 超级简洁，占用内存小
#使用
第一步：
在项目根目录的build.gradle文件中加入

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
第二步：
添加依赖项

	dependencies {
		implementation 'com.github.User:Repo:Tag'
	}
  
#XML属性说明
swipe_image：点击时需要显示的图片
refresh_rate：设置动画刷新频率，默认为16，数值越大动画表现越慢，建议使用默认就好了
degrees_interval_min：图片最小旋转角度，默认-30，取值范围为-360~360（注意取值小于或等于max）
degrees_interval_max:图片最大旋转角度，默认30，取值范围为-360~360（注意取值大于或等于min）
#java代码属性说明
setOnDoubleClickListener：双击事件监听
示例：
heartViewGroup.setOnDoubleClickListener(new HeartViewGroup.DoubleClickListener() {
            @Override
            public void onDoubleClick(View view) {
                //双击事件处理
            }
        });
        
setSwipeImage(int id)：设置点击时需要显示的图片
setRefreshRate(int refreshRate)：设置动画刷新频率，默认为16，数值越大动画表现越慢，建议使用默认就好了
setDegreesInterval(int min,int max)：设置图片旋转角度区间，默认-30~30
min取值范围为-360~360（注意取值小于或等于max）
max取值范围为-360~360（注意取值大于或等于min）
