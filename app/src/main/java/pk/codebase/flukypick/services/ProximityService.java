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
        startForeground(1, showNotification(intent, startId));
        ((AppGlobals) getApplication()).startListening();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification showNotification(Intent intent, int id) {
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String channelId = getPackageName();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
                channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_message_body))
                .setTicker(getString(R.string.app_name))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setChannelId(channelId)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setLargeIcon(bitmap);
        NotificationChannel channel = new NotificationChannel(channelId,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH);

        channel.setSound(null, null);
        channel.setDescription(getString(R.string.channel_description));
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }

        if (notificationManager != null) {
            notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
        }
        return notificationBuilder.build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((AppGlobals) getApplicationContext()).stopListening();
    }
}
