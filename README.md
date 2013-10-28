get-social
==========

Project gives options for connection social media.


For twitter, App includes twitter-4j library and requires external Facebook sdk library.

1) Create a twitter application.

2) Copy CONSUMER_KEY and CONSUMER_SECRET_KEY in application in TwitterHelper class

For facebook, you will need lot of prerequisites. First download the facebook sdk library from Facebook
Developer website 
https://developers.facebook.com/docs/android/getting-started/facebook-sdk-for-android/

After add this library in your project as external library. Then follow the below steps to get started,

1) Create a Facebook application. It will require a key_hash of your project.

2) After creating Facebook Application, you will get an App_Id.

3) Add app_id in your strings.xml as  \<string name="app_id"\>48xxxxxxxxxxx18\</string\>

4) Add metadata in your application tab  
  \<meta-data android:name="com.facebook.sdk.ApplicationId" android:value="@string/app_id" /\>

5) Register Facebook LoginActivity in manifest as follows \<activity android:name="com.facebook.LoginActivity" \>\</activity\>

6) Add Internet permission.  \<uses-permission android:name="android.permission.INTERNET" /\>

Rest functionality is provided in the app.
