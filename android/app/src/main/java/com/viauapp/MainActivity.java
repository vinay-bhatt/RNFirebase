package com.viauapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends ReactActivity implements ReactInstanceManager.ReactInstanceEventListener {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    private Ringtone ringtone;

    @Override
    protected String getMainComponentName() {
        return "viauapp";
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
//         Log.e("+++++++++++", data.toString());


        //start
        if (!Settings.canDrawOverlays(this)) {
            Uri incoming_call_notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), incoming_call_notif);
            Log.e("notificationActivity", String.valueOf(getIntent().getIntExtra(NOTIFICATION_ID, -1)));
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.cancel(getIntent().getIntExtra(NOTIFICATION_ID, -1));
            ringtone.stop();
        }
        //end
    }


    public void performAction1(String eventName, WritableMap params) {
        ReactContext reactContext = ((MainApplication) getApplication()).getReactNativeHost().getReactInstanceManager().getCurrentReactContext();
        if (reactContext != null) {
            reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        }

    }

    @Override
    protected void onResume() {
        getReactInstanceManager().addReactInstanceEventListener(this);
        Intent intent = getIntent();
        if (intent.getAction().equals(Intent.ACTION_VIEW)) {
            performAction1("accept", null);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        getReactInstanceManager().removeReactInstanceEventListener(this);
        super.onPause();
    }

    public static PendingIntent getActionIntent(int notificationId, Uri uri, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        PendingIntent acceptIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return acceptIntent;
    }

    public static PendingIntent getAcceptActionIntent(int notificationId, Uri uri, Context context) {
        Intent intent = new Intent(context, ActionReceiver.class);
        intent.putExtra("action", "accept");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(NOTIFICATION_ID, notificationId);
//        PendingIntent acceptIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent acceptIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return acceptIntent;
    }

    @Override
    public void onReactContextInitialized(ReactContext context) {
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
//         Log.e("+++++++++++", data.toString());


        if (action.equals(Intent.ACTION_VIEW) && intent.getStringExtra("type").equals("accept")) {
            performAction1(intent.getStringExtra("type"), null);
        }
    }
}
