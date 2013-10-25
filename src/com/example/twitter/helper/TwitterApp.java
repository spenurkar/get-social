package com.example.twitter.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import twitter4j.ResponseList;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;


public class TwitterApp {

	private Twitter mTwitter;
	private TwitterSession mSession;
	private AccessToken mAccessToken;
	private CommonsHttpOAuthConsumer mHttpOauthConsumer;
	private OAuthProvider mHttpOauthprovider;
	private String mConsumerKey;
	private String mSecretKey;
	private ProgressDialog mProgressDlg;
	private OnTwitterAuthListener twitterAuthListener;
    private OnTwitterTimelineListener twitterTimelineListener;
	private Activity context;

	public static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-twitter";
	public static final String OAUTH_CALLBACK_HOST = "callback";
	public static final String CALLBACK_URL = "twitterapp://connect"; 
	// public static final String CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://"
	// + OAUTH_CALLBACK_HOST;

	/*
	 * Request token URL https://api.twitter.com/oauth/request_token Authorize
	 * URL https://api.twitter.com/oauth/authorize Access token URL
	 * https://api.twitter.com/oauth/access_token
	 */
	private static final String TWITTER_ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
	private static final String TWITTER_REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	private static final String TWITTER_AUTHORZE_URL = "https://api.twitter.com/oauth/authorize";

	public TwitterApp(Activity context, String consumerKey, String secretKey) {
		this.context = context;

		mTwitter = new TwitterFactory().getInstance();
		mSession = new TwitterSession(context);
		mProgressDlg = new ProgressDialog(context);

		mProgressDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mConsumerKey = consumerKey;
		mSecretKey = secretKey;

		mHttpOauthConsumer = new CommonsHttpOAuthConsumer(mConsumerKey,
				mSecretKey);

		mHttpOauthprovider = new CommonsHttpOAuthProvider(TWITTER_REQUEST_URL,
				TWITTER_ACCESS_TOKEN_URL, TWITTER_AUTHORZE_URL);
		mAccessToken = mSession.getAccessToken();

		configureToken();
	}

	public void setOnTwitterAuthListener(
			OnTwitterAuthListener twitterAuthListener) {
		this.twitterAuthListener = twitterAuthListener;
	}

    public void setOnTwitterTimeLineListener(
            OnTwitterTimelineListener twitterTimelineListener) {
        this.twitterTimelineListener = twitterTimelineListener;
    }

    public Map<String,String> getUserDetails(){
        Map<String,String> userDetails = new HashMap<String, String>();

            userDetails.put("user_id",mSession.getUserId());
            userDetails.put("user_name",mSession.getUsername());
            userDetails.put("user_img",mSession.getUserImg());
            userDetails.put("user_screen_name",mSession.getUserScreenName());

        return userDetails;
    }


    private void configureToken() {
		if (mAccessToken != null) {
			mTwitter.setOAuthConsumer(mConsumerKey, mSecretKey);
			mTwitter.setOAuthAccessToken(mAccessToken);
		}
	}

	public boolean hasAccessToken() {
		return (mAccessToken == null) ? false : true;
	}

    public AccessToken getAccessToken() {
        return (mAccessToken);
    }


    public void resetAccessToken() {
		if (mAccessToken != null) {
			mSession.resetAccessToken();

			mAccessToken = null;
		}
	}

	public String getUsername() {
		return mSession.getUsername();
	}

	public void updateStatus(final String status) throws Exception {

        new Thread() {
            @Override
            public void run() {
                String authUrl = "";
                int what = 1;

                try {
                    mTwitter.updateStatus(status);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
//                mHandler.sendMessage(mHandler
//                        .obtainMessage(what, 3, 0));
            }
        }.start();
	}

    public void reTweetStatus(final String status) throws Exception {

        new Thread() {
            @Override
            public void run() {
                String authUrl = "";
                int what = 1;

                try {
                    mTwitter.updateStatus(status);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
//                mHandler.sendMessage(mHandler
//                        .obtainMessage(what, 3, 0));
            }
        }.start();
    }

	public void uploadPic(File file, String message) throws Exception {
		try {
			StatusUpdate status = new StatusUpdate(message);
			status.setMedia(file);
			mTwitter.updateStatus(status);
		} catch (TwitterException e) {
			throw e;
		}
	}

	public void follow(String screenName) throws Exception {
		try {
			mTwitter.createFriendship(screenName);
		} catch (TwitterException e) {
			throw e;
		}
	}

	public void sendDirectMessage(String recipients, String message)
			throws TwitterException {
		mTwitter.sendDirectMessage(recipients, message);
	}

	public void authorize() {
		mProgressDlg.setMessage("Initializing ...");
		mProgressDlg.show();

		new Thread() {
			@Override
			public void run() {
				String authUrl = "";
				int what = 1;

				try {
					authUrl = mHttpOauthprovider.retrieveRequestToken(
							mHttpOauthConsumer, CALLBACK_URL);
					what = 0;
				} catch (Exception e) {
					e.printStackTrace();
				}
				mHandler.sendMessage(mHandler
						.obtainMessage(what, 1, 0, authUrl));
			}
		}.start();
	}

	public void processToken(String callbackUrl) {
		mProgressDlg.setMessage("Finalizing ...");
		mProgressDlg.show();

		final String verifier = getVerifier(callbackUrl);

		new Thread() {
			@Override
			public void run() {
				int what = 1;

				try {
					mHttpOauthprovider.retrieveAccessToken(mHttpOauthConsumer,
							verifier);

					mAccessToken = new AccessToken(
							mHttpOauthConsumer.getToken(),
							mHttpOauthConsumer.getTokenSecret());

					configureToken();

					User user = mTwitter.verifyCredentials();

					mSession.storeAccessToken(mAccessToken, user.getId()+"",user.getName(),user.getScreenName(),user.getOriginalProfileImageURL());

					what = 0;
				} catch (Exception e) {
					e.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
			}
		}.start();
	}

	private String getVerifier(String callbackUrl) {
		String verifier = "";
//		Log.e("CHECK", "" + callbackUrl);
		try {
			if (callbackUrl.contains(CALLBACK_URL)) {
				callbackUrl = callbackUrl.replace(CALLBACK_URL, "");
				callbackUrl = callbackUrl.replace("?", "");
//				Log.e("CHECK", "" + callbackUrl);
				// URL url = new URL(callbackUrl);
				// String query = url.getQuery();
				// Log.e("CHECK", ""+query);
				// String array[] = query.split("&");
				String array[] = callbackUrl.split("&");

				for (String parameter : array) {
					String v[] = parameter.split("=");

					if (/* URLDecoder.decode( */v[0]
					/* ) */.equals(oauth.signpost.OAuth.OAUTH_VERIFIER)) {
						verifier = /* URLDecoder.decode( */v[1]/* ) */;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		Log.e("CHECK", "" + verifier);
		return verifier;
	}

	private void showLoginDialog(String url) {
		final OnTwitterAuthListener listener = new OnTwitterAuthListener() {
			@Override
			public void onTwitterAuthComplete(String result) {
				processToken(result);
			}

			@Override
			public void onTwitterAuthError(String value) {
				twitterAuthListener
						.onTwitterAuthError("Failed opening authorization page");
			}
		};

		new TwitterDialog(context, url, listener).show();

	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgressDlg.dismiss();

			if (msg.what == 1) {
				if (msg.arg1 == 1)
					twitterAuthListener
							.onTwitterAuthError("Error getting request token");
				else
					twitterAuthListener
							.onTwitterAuthError("Error getting access token");
			} else {
				if (msg.arg1 == 1)
					showLoginDialog((String) msg.obj);
				else
					twitterAuthListener.onTwitterAuthComplete("");
			}
		}
	};

//    public class TimelineAsync extends AsyncTask<Void,Void, QueryResult>{
//
//        private final String hashTag;
//
//        TimelineAsync(String hashtag){
//            this.hashTag = hashtag;
//        }
//        @Override
//        protected QueryResult doInBackground(Void... params) {
//
//            QueryResult result = null;
//            try {
//                Query query = new Query(hashTag);
//                result = mTwitter.search(query);
//            } catch (TwitterException e) {
//                Log.e("Twitter", e.toString());
//            }
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(QueryResult result) {
//            super.onPostExecute(result);
//
//            Log.e("Twitter TimeLine", "onPost");
//            twitterTimelineListener.onTwitterTimeLineFetch(result);
//        }
//    }


    public class GetStatusAsync extends AsyncTask<Void,Void, ResponseList<twitter4j.Status>>{

        private final String hashTag;

        GetStatusAsync(String hashtag){
            this.hashTag = hashtag;
        }
        @Override
        protected ResponseList<twitter4j.Status> doInBackground(Void... params) {

            ResponseList<twitter4j.Status> result = null;
            try {
                result =  mTwitter.getUserTimeline(hashTag);
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(ResponseList<twitter4j.Status> result) {
            super.onPostExecute(result);

            Log.e("Twitter TimeLine", "onPost");
            twitterTimelineListener.onTwitterTimeLineFetch(result);
        }
    }

	public interface OnTwitterAuthListener {
		public void onTwitterAuthComplete(String result);

		public void onTwitterAuthError(String value);
	}

    public interface OnTwitterTimelineListener {
        public void onTwitterTimeLineFetch(ResponseList<twitter4j.Status> result);
    }


    public void getTimeLine(String hashtag){
       new GetStatusAsync(hashtag).execute();
    }
}
