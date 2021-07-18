JetPack 官网网址：https://developer.android.com/jetpack

JetPack 官方 Demo: https://github.com/android/sunflower

JetPack 初学实战与教程：https://juejin.cn/post/6844903889574051848

JetPack 源码进阶：https://blog.csdn.net/mq2553299/category_9276155.html

一、源码内容
Architecture 系列：
1. Data Binding - Declaratively bind observable data to UI elements.
2. Lifecycles - Create a UI that automatically responds to lifecycle events.
3. LiveData - Build data objects that notify views when the underlying database changes.
4. Navigation - Handle everything needed for in-app navigation.
5. Paging -
6. Room - Access your app's SQLite database with in-app objects and compile-time checks.
7. ViewModel - Store UI-related data that isn't destroyed on app rotations. Easily schedule asynchronous tasks for optimal execution.
8. WorkManager - Manage your Android background jobs.


二、Navigation
简化 Android 导航的库和插件。用来管理 Fragment 的切换。
单 Activity 管理多个 Fragment，页面的切换由 Fragment 切换完成，而不是多 Activity 页面切换。

简言之：单容器，多组件。

集成步骤：
1. 添加模块依赖：
```
implementation "androidx.navigation:navigation-fragment-ktx:$navigation_version"
implementation "androidx.navigation:navigation-ui-ktx:$navigation_version"
```
若要使用 `SafeArgs` 插件，则配置插件 classPath 并且在模块下依赖插件：
```
classpath "androidx.navigation:navigation-safe-args-gradle-plugin:$navigation_version"

apply plugin: 'kotlin-android-extensions'
apply plugin: 'androidx.navigation.safeargs.kotlin'
```

2. 添加 Navigation 导航
- 创建资源目录，右键 New 一个 `Navigation source file` 如本项目：res/navigation/nav_garden.xml
- 可视化 xml 布局，创建 destination，配置 `app:startDestination` 默认起始 fragment 位置，<action> 中配置 app:destination 要导航的 fragment ；

3. 建立 `NavHostFragment` 。如本项目：activity_garden.xml 布局中进行配置该 Activity 具备 Fragment 导航能力。属性介绍如下：
- android:name	值必须是androidx.navigation.fragment.NavHostFragment，声明这是一个NavHostFragment
- app:navGraph	存放的是第二步建好导航的资源文件，也就是确定了Navigation Graph
- app:defaultNavHost="true"	与系统的返回按钮相关联

备注：NavHostFragment 作为 **导航界面的容器**。Activity 中展示的这一系列 Fragment，实际均是由其控制展示。

4. 界面跳转、参数传递以及动画。两种方式实现：
- 参数传递：
1）使用 `SafeArgs` 插件，传递参数 args 会在 nav.xml 中声明，接收时使用 safeArgs 获取；
2）若没有使用插件，则手动传递 args 。
- 界面跳转：
```
        // 方式一：插件生成代码进行导航
        private fun navigateToPlant(plantId: String, view: View) {
            val direction = HomeViewPagerFragmentDirections
                .actionHomeViewPagerFragmentToPlantDetailFragment(plantId)
            view.findNavController().navigate(direction)
        }
```

总结：Navigation 最基础的使用如上所示，暂因初学 kotlin，其他功能以及原理研究待花时间再学习。

三、ViewModel & LiveData & Lifecycle

官网使用：https://developer.android.com/codelabs/kotlin-android-training-view-model?index=..%2F..android-kotlin-fundamentals&hl=zh-cn#4

For comparison, here's how the GameFragment UI data is handled in the starter app before you add ViewModel, and after you add ViewModel:

Before you add ViewModel: When the app goes through a configuration change such as a screen rotation, the game fragment is destroyed and re-created. The data is lost.
After you add ViewModel and move the game fragment's UI data into the ViewModel: All the data that the fragment needs to display is now the ViewModel. When the app goes through a configuration change, the ViewModel survives, and the data is retained.

使用 ViewModel 之前，页面配置变化，如屏幕旋转，fragment 销毁重建了，数据也丢失了。
使用 ViewModel 之后并且把 UI 数据交由其处理，当屏幕旋转，ViewModel 还存在，所以数据也保留在。

1. ViewModel 功能与使用
ViewModel 若要看其实现，读了这篇博文，觉得非常简单：https://qingmei2.blog.csdn.net/article/details/84730135 。

ViewModel 是当前架构组件其中一个，重心在于对 **数据状态的维护** 。对于 ViewModel，其主要功能点在于：
1）保证数据不随屏幕旋转而销毁；
2）更方便 UI 组件进行通信。

相关问题点：
1）如何实现页面销毁重建，依然可以保存数据？
- 能想到最简单的方式便是「单例」，但是是应用声明周期的。
- ViewModel 的思想，根据博文，简单来说是从 Fragment.setRetainInstance(true) 这个保持 Activity 销毁是否保留 Fragment 实例的 API 方法学来的。
当设置为 true 时，Activity 重建，Fragment 依然会存活。如此，便可以将 ViewModel 的实例创建放在该 Fragment 进行，如此便实现了页面重建数据依然保留。

2）如何在实现 UI 组件之间的相互通信？
- ViewModel 的实例获取 API 调用如下：
```
XxxViewModel model = ViewModelProviders.of(getActivity()).get(XxxViewModel.class)
```
如何两个 Fragment 的宿主 Activity 是同一个，则通过上述方法可知，获取的是同一个 ViewModel 实例，意味着数据可以进行共享。
假设不可见 && setRetainInstance(true) 的 Fragment 为 HolderFragment，即使 Activity 重建，FragmentManager 中取出的还是之前的实例，如此在 HolderFragment 中创建的引用都还存在。

HolderFragment 中持有 `ViewModelStore` 容器的引用，该容器主要是存储该 Activity 组件下所支持的所有 ViewModel，根据 ViewModel.class 从容器 Map 中取值，存在就返回，不存在就创建。
如此便保证了同一宿主 Activity 下不同 Fragment 可通过获取同一 ViewModel 进行通信。

备注：
```
// 是 Java 库，里面一共 6个文件，当然还涉及到其他底层库的封装，最顶层暴露的 API 来自该库。
"androidx.lifecycle:lifecycle-viewmodel:2.2.0"

// 为 Kotlin 库，猜测类似以 "-ktx" 结尾的库都是原始库的扩展方法库，在不改变原 API 的基础上，对其进行扩展。
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"
```

2、LiveData

博文：https://qingmei2.blog.csdn.net/article/details/85316254

1）作用：数据改变时更新 UI（响应时、数据驱动视图）。本质：观察者模式
2）LiveData vs RxJava
- 更轻量，但没有链式调用及相关操作符。
- 可避免内存泄露。RxJava 需要借助三方库 RxLifecycle、AutoDispose 来解决。
3）API 调用：
- observer() 方法：必须主线程监听，数据更新后回调有两种方式，但最终回调一定是主线程。页面非活跃状态时，不会通知数据更新。
- observerForever() 方法：允许页面再后台非活跃状态时，依然可以更新视图。

相关问题点：
1）LiveData 如何避免内存泄露？
- 借助 Lifecycle 监听 Activity 生命周期变化，若 destroyed 了，则自动取消订阅。

2）数据更新后，如何通知到回调方法？
- LiveData 提供了两种通知数据更新的方法：setValue() 和 postValue() 。前者必须在主线程调用，后者则可以在子线程调用，适合耗时任务。
但最终都会 触发观察者并更新 UI。对于 postValue() 这种，更新 UI 是借助 Handler 实现的。

3）LiveData 数据变更了，一定会收到刷新通知？
- 并不是。当通过 observer() 方法监听时，只有页面处于活跃状态才会通知，也即是 onStart\onResume\onPause 这三个生命周期内，才会监听到，不活跃，源码直接 return 了。
如果想页面位于后台也可以监听，则使用 observerForever() 进行监听，可在整个生命周期内收到数据更新通知。

备注：
```
// 源码文件也比较少
"androidx.lifecycle:lifecycle-livedata:2.2.0"
"androidx.lifecycle:lifecycle-livedata-core:2.2.0"

implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0"
implementation "androidx.lifecycle:lifecycle-livedata-core-ktx:2.2.0"
```

*Tips: ViewModel & LiveData 实现均依赖于 Lifecycle 。整体实现比较复杂。*

3. Lifecycle




五、项目介绍

1. 页面功能介绍
- GardenActivity
- HomeViewPagerFragment
- GardenFragment
- PlanListFragment
- PlanDetailFragment
- GalleryFragment

2. 页面跳转
- GardenActivity / NavHostFragment + navigation/nav_garden.xml 导航
--- HomeViewPagerFragment -->  SunflowerAdapter ---> GardenFragment（我的花园列表） + PlanListFragment（植物目录列表）

按钮切换：
------ GardenFragment -> PlanListFragment（植物目录列表）  -- TAB 切换  （植物列表页 收藏到 我的花园列表）

导航切换：/navigation/nav_garden.xml
------ PlanListFragment -> PlanDetailFragment（植物详情） 【 HomeViewPagerFragment ->  PlanDetailFragment 】
------ PlanDetailFragment -> GalleryFragment（植物图片列表）


3. JetPack 组件案例

3.1 Navigation
1）导航文件：/navigation/nav_garden.xml
2）导航类：
- HomeViewPagerFragmentDirections.actionHomeViewPagerFragmentToPlantDetailFragment()   植物目录列表 -> 植物详情
- PlantDetailFragmentDirections.actionPlantDetailFragmentToGalleryFragment()           植物详情 -> 植物图片列表

3.2 ViewModel + LiveData + Lifecycle + Repository + Room


3.3 Paging


3.4 WorkManager


3.5 Room



