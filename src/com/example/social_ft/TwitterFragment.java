package com.example.social_ft;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.twitter.helper.TwitterHelper;

public class TwitterFragment extends Fragment implements OnClickListener {

	private EditText txtMessage;
	private Button btnSend;
	private TwitterHelper twitterHelper;
	private ProgressDialog progressDialog;
	private Activity context;

	public static Fragment newInstance(Context context) {
		TwitterFragment f = new TwitterFragment();
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_twitter, null);

		txtMessage = (EditText) root.findViewById(R.id.txtMessage);
		btnSend = (Button) root.findViewById(R.id.btnSend);

		btnSend.setOnClickListener(this);

		return root;
	}

	protected void showProgressDialog() {

		progressDialog = ProgressDialog.show(getActivity(), "",
				"Fetching Tweets...", true, true,
				new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub

					}
				});

	}

	protected void closeProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	private void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onClick(View v) {
		if (v == btnSend) {

			if (txtMessage.getText().toString().trim().length() > 0) {

				twitterHelper = new TwitterHelper(getActivity());
				if (!twitterHelper.hasAccessToken()) {
					twitterHelper.authorize();
				}

				try {
					twitterHelper.updateStatus(txtMessage.getText().toString()
							+ " Have a look http://www.google.com");// getTimeLine("DubaiSC");
					showToast("Your message has been posted...");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					showToast("Some error occured.");
				}

			}else{
				showToast("Please enter your status.");
			}

		}
	}
}
