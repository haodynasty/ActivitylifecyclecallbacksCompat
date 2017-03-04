# ActivitylifecyclecallbacksCompat
Android官方从API=14引入了生命周期监听回调API：[ActivityLifecycleCallbacks APIs](http://developer.android.com/reference/android/app/Application.ActivityLifecycleCallbacks.html).
当前库兼容到Android 1+.
- 新特性：增加了Fragment的生命周期监听

[![License][licence_svg]][licence_url]
[![Download][bintray_svg]][bintray_url]
# 引用
将依赖增加到APP级别的build.gradle（你也可指定版本，用[![Download][bintray_svg]][bintray_url]替换latest.integration）
```
dependencies {
    compile 'com.blakequ.rsa:rsa:latest.integration'
}
```
maven
```
<dependency>
  <groupId>com.blakequ.rsa</groupId>
  <artifactId>rsa</artifactId>
  <version>latest.integration</version>
  <type>pom</type>
</dependency>
```

# 什么时候使用
对于经常使用的场景如下：
- 使用了友盟统计。用于统计APP的使用路径，使用习惯，停留时间等，需要监听Activity或者Fragment的生命周期，如果每个Activity或Fragment都去在onStart或者onStop中调用他们的代码，不仅造成维护困难，而且代码重复。
- 公共处理方法。如垃圾回收，资源是否（在Activity退出后View，监听器释放）
如果使用了生命周期监听，在Application中registerActivityLifecycleCallbacks，然后统一在生命周期处理即可，没有重复代码，非常容易维护。

具体使用可参考Demo实现

# 如何使用
1. Activity生命周期监听
- 使用LifecycleDispatcher.registerActivityLifecycleCallbacks(Application, ActivityLifecycleCallbacksCompat)，用于替换系统的lication.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks)方法
- 所有的Activity都需要继承LifecycleDispatchActivity，用于替换继承的AppCompatActivity(or Activity).
```
public class MainActivity extends Activity{...}
use:
public class MainActivity extends LifecycleDispatchActivity{...}
or you can extends LifecycleDispatchListActivity, LifecycleDispatchPreferenceActivity
```
- 如果你想实现自己的基类Activity，你可以如下实现(如使用了ActionBarSherlock)：==可选==
```
public class BaseActivity extends SherlockFragmentActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            LifecycleDispatcher.get().onActivityCreated(this, savedInstanceState);
        }

        @Override
        protected void onStart() {
            super.onStart();
            LifecycleDispatcher.get().onActivityStarted(this);
        }

        @Override
        protected void onResume() {
            super.onResume();
            LifecycleDispatcher.get().onActivityResumed(this);
        }

        @Override
        protected void onPause() {
            super.onPause();
            LifecycleDispatcher.get().onActivityPaused(this);
        }

        @Override
        protected void onStop() {
            super.onStop();
            LifecycleDispatcher.get().onActivityStopped(this);
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            LifecycleDispatcher.get().onActivitySaveInstanceState(this, outState);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            LifecycleDispatcher.get().onActivityDestroyed(this);
        }
```

2. Fragment支持API>=11
使用方法同Activity
- 使用LifecycleDispatcher.registerFragmentLifecycleCallbacks(Application, FragmentLifecycleCallbacksCompat)
- 所有的Fragment都需要继承LifecycleDispatchFragment，用于替换继承的Fragment.
```
public class MainFrgament extends Fragment{...}
use:
public class MainFrgament extends LifecycleDispatchFragment{...}
or you can extends LifecycleDispatchListFrgament
```

# 参考
- [BLOG](www.blakequ.com)
- [android-activitylifecyclecallbacks-compat](https://github.com/BoD/android-activitylifecyclecallbacks-compat)

[bintray_svg]: https://api.bintray.com/packages/haodynasty/maven/AndroidLifecycle/images/download.svg
[bintray_url]: https://bintray.com/haodynasty/maven/AndroidLifecycle/_latestVersion
[licence_svg]: https://img.shields.io/badge/license-Apache%202-green.svg
[licence_url]: https://www.apache.org/licenses/LICENSE-2.0