package com.example.social_ft;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class FacebookFragment extends Fragment implements OnClickListener{

	private Button btnSend;
	private Session session;
	
	public static Fragment newInstance(Context context) {
		FacebookFragment f = new FacebookFragment();
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_facebook, null);
		
		btnSend = (Button) root.findViewById(R.id.btnSend);

		btnSend.setOnClickListener(this);
		
		
		session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			// if the session is already open,
			// try to show the selection fragment
			Log.d("Snehal","FacebooK Logged In : " + session.getAccessToken());
			session.closeAndClearTokenInformation();
		} else {
			// otherwise present the splash screen
			// and ask the person to login.
			Log.d("Snehal","FacebooK Not Logged In");
		}
		
		return root;
	}
	
	@Override
	public void onClick(View v) {
		if(v == btnSend){
			if(session.isOpened()){
				publishFeedDialog();
			}else{
				Toast.makeText(getActivity(), "Login to Facebook first.", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	/**
	 * Publishes post which includes a image, a url and some description.
	 */
	private void publishFeedDialog() {
		Bundle params = new Bundle();
		params.putString("name", "Facebook SDK for Android");
		params.putString("caption","Build great social apps and get more installs.");
		params.putString("description",
				"The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
		params.putString("link", "https://developers.facebook.com/android");
		params.putString("picture","https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(getActivity(),
				Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(getActivity(),
										"Posted story, id: " + postId,
										Toast.LENGTH_SHORT).show();
							} else {
								// User clicked the Cancel button
								Toast.makeText(
										getActivity().getApplicationContext(),
										"Publish cancelled", Toast.LENGTH_SHORT)
										.show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							Toast.makeText(
									getActivity().getApplicationContext(),
									"Publish cancelled", Toast.LENGTH_SHORT)
									.show();
						} else {
							// Generic, ex: network error
							Toast.makeText(
									getActivity().getApplicationContext(),
									"Error posting story", Toast.LENGTH_SHORT)
									.show();
						}
					}

				}).build();
		feedDialog.show();
	}
}
