///////////////////////////////////////////////////////////////////////////////
//
//   Fluky Pick - https://github.com/codebasepk/fluky-pick
//
//   Copyright (c) Codebase PK
//
//   Licensed under the MIT License.
//   http://www.opensource.org/licenses/mit-license.php
//
///////////////////////////////////////////////////////////////////////////////

package pk.codebase.flukypick.listeners;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import java.util.Objects;

import pk.codebase.flukypick.services.ProximityService;
import pk.codebase.flukypick.utils.Helpers;

public class PhoneStateListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Objects.equals(intent.getAction(), "android.intent.action.PHONE_STATE")) {
            return;
        }
        if (Helpers.requiresPhoneStateReadPermission(context)) {
            return;
        }

        KeyguardManager keyguardManager = context.getSystemService(KeyguardManager.class);

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (state == null || keyguardManager == null) {
            return;
        }

        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            if (keyguardManager.inKeyguardRestrictedInputMode()) {
                context.startForegroundService(new Intent(context.getApplicationContext(),
                        ProximityService.class));
            }
        } else {
            context.stopService(new Intent(context.getApplicationContext(),
                    ProximityService.class));
        }
    }
}
