package com.viauapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.facebook.react.ReactApplication;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class ActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context,"recieved",Toast.LENGTH_SHORT).show();

        String action = intent.getStringExtra("action");
        if (action.equals("accept")) {
            ReactApplication rapp = (ReactApplication) context.getApplicationContext();
            performAction1(rapp.getReactNativeHost().getReactInstanceManager().getCurrentReactContext(), action, null);
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(intent.getIntExtra(MainActivity.NOTIFICATION_ID, 0));
            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            activityIntent.setAction(Intent.ACTION_VIEW);
            activityIntent.putExtra("type", "accept");
            context.startActivity(activityIntent);
        } else if (action.equals("reject")) {
            ReactApplication rapp = (ReactApplication) context.getApplicationContext();
            performAction1(rapp.getReactNativeHost().getReactInstanceManager().getCurrentReactContext(), action, null);
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(intent.getIntExtra(MainActivity.NOTIFICATION_ID, 0));

        }
        //This is used to close the notification tray
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

    public void performAction1(ReactContext reactContext, String eventName, WritableMap params) {
        if (reactContext != null) {
            reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        }

    }

    public void performAction2() {

    }
}
