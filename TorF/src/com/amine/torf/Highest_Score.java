package com.amine.torf;

import java.util.ArrayList;
import java.util.List;

import com.amine.torf.helpers.DataManager;
import com.amine.torf.helpers.DbHighestScore;
import com.amine.torf.pojo.Scoredata;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Highest_Score extends Activity {

	ListView lvscore;
	DbHighestScore db;
	private ArrayList<String> idlist= new ArrayList<String>();
	private ArrayList<String> namelist= new ArrayList<String>();
	private ArrayList<String> scorelist= new ArrayList<String>();
	String id, topscore, name;
	private AdView adView;
	Button btnback;
	  /* Your ad unit id. Replace with your actual ad unit id. */
	private static final String AD_UNIT_ID = DataManager.admobid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_highest__score);
		
		btnback = (Button)findViewById(R.id.btnback);
		lvscore = (ListView)findViewById(R.id.lvscore);
		db = new DbHighestScore(this);
		
		adView = new AdView(this);
		
	    adView.setAdSize(AdSize.BANNER);
	    adView.setAdUnitId(AD_UNIT_ID);

	    LinearLayout ll = (LinearLayout)findViewById(R.id.ad);
	    ll.addView(adView);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    
	    adView.loadAd(adRequest);
		getListitem();
		
		btnback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Highest_Score.this, MainActivity.class);
		    	finish();
		    	startActivity(i);
			}
		});
		
	}
	
	public void getListitem() {

		List<Scoredata> score = db.getAllContacts();
		
		
		
		for (Scoredata sc : score) {

			id = String.valueOf(sc.getId());
			name = sc.getName();
			topscore = sc.getScore(); 

			idlist.add(id);
			namelist.add(name);
			scorelist.add(topscore);
		}

		ImageAdapter img = new ImageAdapter(this);
		lvscore.setAdapter(img);
		
	}

	
	public class ImageAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public ImageAdapter(Context c) {
			mInflater = LayoutInflater.from(c);

		}

		@Override
		public int getCount() {
			return idlist.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.score_row, null);

				holder = new ViewHolder();
				holder.txtid = (TextView) convertView
						.findViewById(R.id.txtid);

				holder.txtname = (TextView) convertView
						.findViewById(R.id.txtname);
				
				holder.txtscore = (TextView) convertView
						.findViewById(R.id.txtscore);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.txtid.setText(""+(position+1));
			holder.txtname.setText(namelist.get(position));
			holder.txtscore.setText(scorelist.get(position));
			
			return convertView;
		}

		class ViewHolder {
			TextView txtid, txtname, txtscore;

		}

	}
	
	 @Override
	    public void onBackPressed() {
	      
	    	Intent i = new Intent(Highest_Score.this, MainActivity.class);
	    	finish();
	    	startActivity(i);
	      
	    }

}
