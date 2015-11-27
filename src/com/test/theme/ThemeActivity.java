package com.test.theme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class ThemeActivity extends ActionBarActivity {

	/**
	 * 保存主题的sharedPreferences中的name
	 */
	public static final String SP_NAME = "theme";
	/**
	 * 当前所应用的主题保存在sharedPreferences中的key
	 */
	public static final String SP_KEY_THEME = "theme";
	
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//改变程序的theme，必须在setContentView()之前
		sp = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);  
		if(R.style.AppTheme == sp.getInt(SP_KEY_THEME, R.style.AppTheme)){
			this.setTheme(R.style.AppTheme);
		}else{
			this.setTheme(R.style.AppThemeLight);
		}
		
		setContentView(R.layout.activity_theme);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.theme, menu);
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
	public static class PlaceholderFragment extends Fragment {

		private Button btnSwitch;
		
		private ImageView ivImage;
		
		private SharedPreferences sp;
		
		public PlaceholderFragment() {
		}

		@SuppressLint("NewApi")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_theme,
					container, false);
			ivImage = (ImageView)rootView.findViewById(R.id.iv_image);
			/*获取当前主题下，播放按钮的ResourceID*/
			TypedArray array = getActivity().getTheme()
					.obtainStyledAttributes(new int[] {R.attr.drawableStyle});
			int drawableResId = array.getResourceId(0, 0);
			ivImage.setImageResource(drawableResId);
			
			btnSwitch = (Button) rootView.findViewById(R.id.btn_switch);
			btnSwitch.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//保存选择的主题到sp
					sp = getActivity().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
					
					if(R.style.AppTheme == sp.getInt(SP_KEY_THEME, R.style.AppTheme)){
						sp.edit().putInt(SP_KEY_THEME, R.style.AppThemeLight).commit();
					}else{
						sp.edit().putInt(SP_KEY_THEME, R.style.AppTheme).commit();
					}
					//重新创建Activity
					getActivity().recreate();
				}
			});
			return rootView;
		}
	}

}
