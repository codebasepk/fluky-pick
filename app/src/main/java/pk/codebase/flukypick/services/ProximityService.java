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

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import pk.codebase.flukypick.R;
import pk.codebase.flukypick.utils.AppGlobals;

public class ProximityService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ((AppGlobals) getApplication()).startListening();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ((AppGlobals) getApplicationContext()).stopListening();
    }
}
