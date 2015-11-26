# TestTheme
***这是一个实现app主题动态切换的例子程序，示例了两种方式；
	1、通过切换app的Theme属性；
	2、通过安装主题apk包实现切换；
	
##通过切换app的Theme属性实现主题动态切换的核心逻辑：
	在Style中创建两套Theme（如appTheme和appThemeLight），在attr文件中创建需要的动态改变的属性（如backgroundStyle），
	然后在前面以创建的Theme（appTheme和appThemeLight）中使用自定义的属性（backgroundStyle）；
	最后在代码中通过context.setTheme(int resId)动态设置主题为自己创建某个主题（appTheme或appThemeLight）；
	注意：context.setTheme(int resId)必须在Activity的setContentView()之前调用;
	
##通过安装主题apk包实现切换核心代码逻辑是：
	1、查找所有与规定的主题apk包名相同的apk：
	PackageManager pm = getActivity().getPackageManager();
				List<PackageInfo> listPackages = pm.getInstalledPackages(PackageManager.PERMISSION_GRANTED);
				List<PackageInfo> listSkins = new ArrayList<PackageInfo>();
				for (PackageInfo packageInfo : listPackages) {
					if(packageInfo.packageName.startsWith(SKIN_PACKAGE_PREFIX)){
						listSkins.add(packageInfo);
					}
				}
	2、根据包名，获得包名所对应的apk的resource资源文件
	public void onThemeChanged(String newThemePackageName){
			if(TextUtils.isEmpty(newThemePackageName)) return;
			try {
				Context con = this.getActivity().createPackageContext(newThemePackageName, CONTEXT_IGNORE_SECURITY);
				Resources res = con.getResources();
				changeStyle(res);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	public void changeStyle(Resources res){
		mListView.setBackgroundColor(res.getColor(R.color.bg_listview));
	}
	
##由于该程序只是主程序，（主程序本身的resource可以理解为默认主题），并没有提供主题apk程序，特在此说明一下：
	主题apk不需要实现任何功能，只需要对照主程序的resource完成主题apk的resource；
	已color文件为例，在主程序的color文件中有bg_listview这个颜色（且主程序代码中以该颜色为listView的主题背景），
	那么在主题apk的color文件中同样指定一个名为bg_listview的颜色，那么在主程序将主题切换为该主题apk时，会自动
	读取该主题apk下的这个bg_listview作为listView的主题背景；
	
	
##程序截图：
![image1](https://github.com/ZhangSir/TestTheme/blob/master/Screenshot_2015-11-25-16-49-35.jpeg)
![image2](https://github.com/ZhangSir/TestTheme/blob/master/Screenshot_2015-11-25-16-49-44.jpeg)
![image3](https://github.com/ZhangSir/TestTheme/blob/master/Screenshot_2015-11-25-16-49-54.jpeg)