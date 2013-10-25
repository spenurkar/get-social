package com.example.twitter.helper;

import java.util.Map;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.widget.Toast;

/**
 * 
 * @author Rishi
 * 
 */
public class TwitterHelper {

    private final static String CONSUMER_KEY = "Az84lgpfwglmWs0NYLQ";
    private final static String CONSUMER_SECRET = "NNrCjvBDMaT56pEoQB7MCxoDjxICBzCZfWzukIB0";



    //	private static final String CONSUMER_KEY = "gXLKVbqTSmrwh0VVOrvaAA";
//	private static final String CONSUMER_SECRET = "TCh3Ieh86iEVeTcyatVCUkTnOtk9kEpQKsWh7nK8";
	private TwitterApp mTwitter;
	private ProgressDialog progressDialog;
	private Activity context;

	public TwitterHelper(Activity context,
			TwitterApp.OnTwitterAuthListener twitterAuthListener) {
		this.context = context;
		this.mTwitter = new TwitterApp(context, CONSUMER_KEY, CONSUMER_SECRET);
		setOnTwitterAuthListener(twitterAuthListener);
	}

	public TwitterHelper(Activity context) {
		this.context = context;
		this.mTwitter = new TwitterApp(context, CONSUMER_KEY, CONSUMER_SECRET);
	}

	private void setOnTwitterAuthListener(
			TwitterApp.OnTwitterAuthListener twitterAuthListener) {
		mTwitter.setOnTwitterAuthListener(twitterAuthListener);
	}

    public void setOnTwitterTimelineListener(TwitterApp.OnTwitterTimelineListener twitterTimelineListener){
        mTwitter.setOnTwitterTimeLineListener(twitterTimelineListener);
    }

	public boolean hasAccessToken() {
		return mTwitter.hasAccessToken();
	}

	public void authorize() {
		mTwitter.authorize();
	}

    public TwitterHelper getTwitterHelper(){
        return this;
    }

    public void getTimeLine(String hashTag){
        mTwitter.getTimeLine(hashTag);
    }


	public String getUserName() {

		return mTwitter.getUsername();

	}

    public void updateStatus(String status) throws Exception {
        mTwitter.updateStatus(status);
    }


    public Map<String,String> getUserDetails(){
        return mTwitter.getUserDetails();
    }

    public AccessToken getAccessToken(){
        return mTwitter.getAccessToken();
    }

	public void sendDirectMessage(String recipients, String message)
			throws TwitterException {
		mTwitter.sendDirectMessage(recipients, message);
	}


	private void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	protected void showProgressDialog() {

		progressDialog = ProgressDialog.show(context,"","Please Wait", true, true,
                new OnCancelListener() {

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

}
