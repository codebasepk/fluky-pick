package pk.codebase.flukypick.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.core.app.NotificationCompat;

import pk.codebase.flukypick.R;

public class ProximityService extends Service {
    private SensorManager sensorManager;
    private PowerManager.WakeLock mWakeLock;
    private PowerManager mPowerManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, showNotification(intent, startId));
        sensorManager = getApplicationContext().getSystemService(SensorManager.class);
        Sensor sensor = null;
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
        mPowerManager = (PowerManager) getApplicationContext()
                .getSystemService(Context.POWER_SERVICE);
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        return START_STICKY;
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

    private SensorEventListener listener = new SensorEventListener() {
        @SuppressLint("WakelockTimeout")
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float distance = sensorEvent.values[0];
            // sensor value is 0.0 when mobile is in pocket or something's covering up the
            // proximity sensor
            if (distance == 0.0) {
                if (mWakeLock == null) {
                    mWakeLock = mPowerManager.newWakeLock
                            (PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "FlukyPick:");
                }
                if (!mWakeLock.isHeld()) {
                    mWakeLock.acquire();
                }
            } else {
                if (mWakeLock != null && mWakeLock.isHeld()) {
                    mWakeLock.release();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {}
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(listener);
    }
}
