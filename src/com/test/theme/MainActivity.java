package com.test.theme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	/**
	 * 皮肤包apk的前缀
	 */
	public static final String SKIN_PACKAGE_PREFIX = "com.test.theme";
	
	/**
	 * 保存主题的sharedPreferences中的name
	 */
	public static final String SP_NAME = "skin";
	/**
	 * 当前所应用的主题保存在sharedPreferences中的key
	 */
	public static final String SP_KEY_SKIN = "skin";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements OnSharedPreferenceChangeListener{

		private ListView mListView;
		
		private SkinAdapter mAdapter;
		
		private SharedPreferences sp;
		
		private List<PackageInfo> listPackages = null;
		
		/**搜索皮肤异步线程结束时的回调标示*/
		public static final int FLAG_SEARCH_SKIN = 1001;
		
		private Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				if(msg.what == FLAG_SEARCH_SKIN){
					if(null != msg.obj){
						listPackages = (List<PackageInfo>) msg.obj;
						mAdapter.refreshData(listPackages);
						mAdapter.notifyDataSetChanged();
					}
				}
			}
			
		};
		
		/**用来搜索皮肤的runnable*/
		private Runnable searchSkinRannable = new Runnable() {
			
			@Override
			public void run() {
				PackageManager pm = getActivity().getPackageManager();
				List<PackageInfo> listPackages = pm.getInstalledPackages(PackageManager.PERMISSION_GRANTED);
				List<PackageInfo> listSkins = new ArrayList<PackageInfo>();
				for (PackageInfo packageInfo : listPackages) {
					if(packageInfo.packageName.startsWith(SKIN_PACKAGE_PREFIX)){
						listSkins.add(packageInfo);
					}
				}
				Message msg = handler.obtainMessage();
				msg.what = FLAG_SEARCH_SKIN;
				msg.obj = listSkins;
				handler.sendMessage(msg);
			}
		};
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			sp = getActivity().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);  
	        sp.registerOnSharedPreferenceChangeListener(this);  
			
			
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			mListView = (ListView) rootView.findViewById(R.id.listview);
			
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					//保存选择的主题到sp
					 sp.edit().putString(SP_KEY_SKIN, ((PackageInfo)mAdapter.getItem(position)).packageName).commit();  
				}
			});
			
			if (listPackages == null) {
				listPackages = new ArrayList<PackageInfo>();
			}
			mAdapter = new SkinAdapter(getActivity(), listPackages);
			mListView.setAdapter(mAdapter);
			
			/*初始化主题，没有保存记录时，使用当前应用的默认主题*/
			onThemeChanged(sp.getString(SP_KEY_SKIN, getActivity().getPackageName()));
			
			new Thread(searchSkinRannable).start();
			return rootView;
		}
		
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
		
		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			if(key.equals(SP_KEY_SKIN)){
				 System.out.println("监听到主题修改");  
				onThemeChanged(sharedPreferences.getString(key, null));
			}
			
		}
		
		class SkinAdapter extends BaseAdapter{

			private Context mContext;
			private List<PackageInfo> listSkins;
			
			public SkinAdapter(Context context, List<PackageInfo> listPackages){
				this.mContext = context;
				this.listSkins = listPackages;
				if(null == this.listSkins){
					this.listSkins = new ArrayList<PackageInfo>();
				}
			}
			
			public void refreshData(List<PackageInfo> listPackages){
				this.listSkins = listPackages;
				if(null == this.listSkins){
					this.listSkins = new ArrayList<PackageInfo>();
				}
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return this.listSkins.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return this.listSkins.get(position);
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				CourseHolder holder = null;
				if (convertView == null) {
					holder = new CourseHolder();
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.layout_skin_list_item, parent, false);
					holder.ivIcon = (ImageView) convertView
							.findViewById(R.id.iv_icon);
					holder.tvName = (TextView) convertView
							.findViewById(R.id.tv_name);
					convertView.setTag(holder);
				} else {
					holder = (CourseHolder) convertView.getTag();
				}

				PackageInfo skin = listSkins.get(position);
				holder.ivIcon.setImageDrawable(skin.applicationInfo.loadIcon(mContext.getPackageManager()));
				holder.tvName.setText(skin.applicationInfo.loadLabel(mContext.getPackageManager())
						+ "(" + skin.applicationInfo.loadDescription(mContext.getPackageManager()) + "）");
					
				return convertView;
			}

			class CourseHolder {
				ImageView ivIcon;
				TextView tvName;
			}
		}

	}

}
