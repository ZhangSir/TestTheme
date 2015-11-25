# TestTheme
这是一个通过安装主题apk，实现app主题动态切换的例子程序；

##该程序的核心代码是：
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