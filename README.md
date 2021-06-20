# BannerView
这是一个轻量简洁的自定义banner控件。欢迎大家提出意见或建议

BannerView在控件可见时自动播放，不可见时停止播放，不需要额外设置界面切换时的开始播放和暂停播放。

# Gradle

    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

     implementation 'com.github.lany192:BannerView:4.0.0'
     
# Usage
## xml layout
    <com.github.lany192.banner.BannerView
        android:id="@+id/banner_view"
        android:layout_width="match_parent"
        android:layout_height="180dp"/>

# Demo
[点击下载 download Demo apk](https://github.com/lany192/BannerView/raw/master/preview/sample-debug.apk)
# Preview
![image](https://github.com/lany192/BannerView/raw/master/preview/demo.webp)

## License


Copyright 2017 Y.G. Lan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

