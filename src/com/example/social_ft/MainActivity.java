package com.example.social_ft;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.twitter.helper.TwitterApp.OnTwitterAuthListener;
import com.example.twitter.helper.TwitterHelper;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

public class MainActivity extends FragmentActivity implements OnClickListener, OnTwitterAuthListener{

	Button btnTwitter, btnFacebook;
	UiLifecycleHelper uiHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		btnTwitter = (Button) findViewById(R.id.btnTwit);
		btnFacebook = (Button) findViewById(R.id.btnFacebook);
		
		btnTwitter.setOnClickListener(this);
		btnFacebook.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch(id){
			case R.id.btnTwit: 
				onClickTwit();
				break;
			case R.id.btnFacebook: 
				onClickFacebook();
				break;
		}
	}

	private void onClickFacebook() {
		FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
		FacebookFragment fbfrag = new FacebookFragment();
		fragmentTransaction.replace(R.id.container, fbfrag, "FacebookFragment");
		fragmentTransaction.commit();
	}

	private void onClickTwit() {
		TwitterHelper twitterHelper = new TwitterHelper(this,this);
		if (!twitterHelper.hasAccessToken()) {
			twitterHelper.authorize();
		}else{
			FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
			TwitterFragment twtfrag = new TwitterFragment();
			fragmentTransaction.replace(R.id.container, twtfrag, "TwitterFragment");
			fragmentTransaction.commit();
		}
	}

	@Override
	public void onTwitterAuthComplete(String result) {
		// TODO Auto-generated method stub
		FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
		TwitterFragment upfrag = new TwitterFragment();
		fragmentTransaction.replace(R.id.container, upfrag, "TwitterFragment");
		fragmentTransaction.commit();
	}

	@Override
	public void onTwitterAuthError(String value) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		uiHelper.onDestroy();
	}
	

	private void onSessionStateChanged(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.d("Snehal","Logged in...Access token" + session.getAccessToken());
		} else if (state.isClosed()) {
			Log.d("Snehal","Logged out...");
		}
	}
	

	private Session.StatusCallback callback = new StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChanged(session, state, exception);
		}
	};
	

}
