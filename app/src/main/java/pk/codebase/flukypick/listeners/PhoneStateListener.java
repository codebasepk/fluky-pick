package pk.codebase.flukypick.listeners;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import pk.codebase.flukypick.services.ProximityService;
import pk.codebase.flukypick.utils.Helpers;

public class PhoneStateListener extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Helpers.hasReadPhoneStatePermission(context)) {
            return;
        }

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        KeyguardManager myKM = context.getSystemService(KeyguardManager.class);
        if (state == null || myKM == null) {
            return;
        }

        switch (state) {
            case "RINGING":
                if( myKM.inKeyguardRestrictedInputMode()) {
                        context.startForegroundService(
                                new Intent(context, ProximityService.class));

                }
                break;
            case "IDLE":
                System.out.println(isMyServiceRunning(context));
                if (isMyServiceRunning(context)) {
                    context.stopService(new Intent(context, ProximityService.class));
                }
                break;
            case "OFFHOOK":
                if (isMyServiceRunning(context)) {
                    context.stopService(new Intent(context, ProximityService.class));
                }
                break;
        }
    }

    // ActivityManager.getRunningServices method was deprecated in API level 26.
    // As of Build.VERSION_CODES.O, this method is no longer available to third party applications.
    // For backwards compatibility, it will still return the caller's own services.
    private boolean isMyServiceRunning(Context context) {
        ActivityManager manager = context.getSystemService(ActivityManager.class);
        for (ActivityManager.RunningServiceInfo service :
                manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ProximityService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



}
