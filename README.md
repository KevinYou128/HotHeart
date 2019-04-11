# HotHeart
模仿抖音APP双击屏幕蹦出心图，特点：   1. 可以自定义图片   2. 可以自定义旋转角度   3. 超级简洁，占用内存小
# 预览
[image](https://github.com/KevinYou128/HotHeart/blob/master/app/src/main/res/drawable/Screenshot_20190312-183946.png)
# 使用
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
		implementation 'com.github.KevinYou128:HotHeart:v1.0'
	}
<h3>XML属性说明</h3>

<p> swipe_image：点击时需要显示的图片<br />
 <br />
 refresh_rate：设置动画刷新频率，默认为16，数值越大动画表现越慢，建议使用默认就好了<br />
 <br />
 degrees_interval_min：图片最小旋转角度，默认-30，取值范围为-360到360（注意取值小于或等于max）<br />
 <br />
 degrees_interval_max:图片最大旋转角度，默认30，取值范围为-360到360（注意取值大于或等于min）</p>
