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

1. ViewModel 功能与使用
ViewModel 若要看其实现，读了这篇博文，觉得非常简单：https://qingmei2.blog.csdn.net/article/details/84730135 。

ViewModel 是当前架构组件其中一个，重心在于对 **数据状态的维护** 。对于 ViewModel，其主要功能点在于：
1）保证数据不随屏幕旋转而销毁；
2）更方便 UI 组件进行通信。

问题点：
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



