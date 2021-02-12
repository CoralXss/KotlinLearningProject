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

三、ViewModel & LiveData
TODO



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



