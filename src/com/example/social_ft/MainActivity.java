package com.example.social_ft;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.twitter.helper.TwitterApp.OnTwitterAuthListener;
import com.example.twitter.helper.TwitterHelper;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

public class MainActivity extends FragmentActivity implements OnClickListener,
		OnTwitterAuthListener {

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

		switch (id) {
		case R.id.btnTwit:

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Title");

			// Set up the input
			final EditText input = new EditText(this);
			input.setHint("Enter your status here...");
			// Specify the type of input expected; this, for example, sets the
			// input as a password, and will mask the text
			/*input.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);*/
			builder.setView(input);

			// Set up the buttons
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String m_Text = input.getText().toString();
							if (m_Text.trim().length() > 0) {
								onClickTwit(m_Text);
							} else {
								showToast("Please enter your status.");
							}
						}
					});
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});

			builder.show();

			break;
		case R.id.btnFacebook:
			onClickFacebook();
			break;
		}
	}

	private void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	private void onClickFacebook() {
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		FacebookFragment fbfrag = new FacebookFragment();
		fragmentTransaction.replace(R.id.container, fbfrag, "FacebookFragment");
		fragmentTransaction.commit();
	}

	private void onClickTwit(String msg) {

		TwitterHelper twitterHelper = new TwitterHelper(this);
		if (!twitterHelper.hasAccessToken()) {
			twitterHelper.authorize();
		}

		try {
			twitterHelper.updateStatus(msg+ " Have a look http://www.google.com");
			showToast("Your message has been posted...");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showToast("Some error occured.");
		}

		/*
		 * TwitterHelper twitterHelper = new TwitterHelper(this,this); if
		 * (!twitterHelper.hasAccessToken()) { twitterHelper.authorize(); }else{
		 * FragmentTransaction fragmentTransaction =
		 * this.getSupportFragmentManager().beginTransaction(); TwitterFragment
		 * twtfrag = new TwitterFragment();
		 * fragmentTransaction.replace(R.id.container, twtfrag,
		 * "TwitterFragment"); fragmentTransaction.commit(); }
		 */
	}

	@Override
	public void onTwitterAuthComplete(String result) {
		// TODO Auto-generated method stub
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
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
			Log.d("Snehal",
					"Logged in...Access token" + session.getAccessToken());
		} else if (state.isClosed()) {
			Log.d("Snehal", "Logged out...");
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
