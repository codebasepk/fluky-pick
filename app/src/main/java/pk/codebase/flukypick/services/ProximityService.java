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

package pk.codebase.flukypick.services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import pk.codebase.flukypick.utils.AppGlobals;

public class ProximityService extends IntentService {

    public ProximityService() {
        super("fluckypick");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ((AppGlobals) getApplication()).startListening();
        return START_NOT_STICKY;
    }
}
