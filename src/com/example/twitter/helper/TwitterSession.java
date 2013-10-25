package com.example.twitter.helper;

import twitter4j.auth.AccessToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TwitterSession {
	private SharedPreferences sharedPref;
	private Editor editor;

	private static final String TWEET_AUTH_KEY = "auth_key";
	private static final String TWEET_AUTH_SECRET_KEY = "auth_secret_key";
	private static final String TWEET_USER_NAME = "user_name";
    private static final String TWEET_USER_ID = "user_id";
    private static final String TWEET_USER_SCREEN_NAME = "user_screen_name";
    private static final String TWEET_USER_IMG = "user_img";
	private static final String SHARED = "Twitter_Preferences";

	// private static final String TWEET_AUTH_KEY = "";
	// private static final String TWEET_AUTH_SECRET_KEY = "";
	// private static final String TWEET_USER_NAME = "";
	// private static final String SHARED = "Twitter_Preferences";

	public TwitterSession(Context context) {
		sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);

		editor = sharedPref.edit();
	}

	public void storeAccessToken(AccessToken accessToken,String userid, String username,String userscreenname, String img) {
		editor.putString(TWEET_AUTH_KEY, accessToken.getToken());
		editor.putString(TWEET_AUTH_SECRET_KEY, accessToken.getTokenSecret());
		editor.putString(TWEET_USER_ID, userid);
        editor.putString(TWEET_USER_NAME, username);
        editor.putString(TWEET_USER_SCREEN_NAME, userscreenname);
        editor.putString(TWEET_USER_IMG, img);

		editor.commit();
	}

	public void resetAccessToken() {
		editor.putString(TWEET_AUTH_KEY, null);
		editor.putString(TWEET_AUTH_SECRET_KEY, null);
		editor.putString(TWEET_USER_NAME, null);

		editor.commit();
	}

	public String getUsername() {
		return sharedPref.getString(TWEET_USER_NAME, "");
	}

    public String getUserId() {
        return sharedPref.getString(TWEET_USER_ID, "");
    }

    public String getUserScreenName() {
        return sharedPref.getString(TWEET_USER_SCREEN_NAME, "");
    }
    public String getUserImg() {
        return sharedPref.getString(TWEET_USER_IMG, "");
    }


    public AccessToken getAccessToken() {
		String token = sharedPref.getString(TWEET_AUTH_KEY, null);
		String tokenSecret = sharedPref.getString(TWEET_AUTH_SECRET_KEY, null);

		if (token != null && tokenSecret != null)
			return new AccessToken(token, tokenSecret);
		else
			return null;
	}
}
