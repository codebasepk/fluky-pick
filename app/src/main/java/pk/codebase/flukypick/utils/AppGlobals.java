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

package pk.codebase.flukypick.utils;

import android.app.Application;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;

public class AppGlobals extends Application {

    private static final double DISTANCE_IDLE = 0.0;

    private SensorManager mSensorManager;
    private PowerManager.WakeLock mWakeLock;
    private PowerManager mPowerManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = getSystemService(SensorManager.class);
        mPowerManager = getSystemService(PowerManager.class);
    }

    public void startListening() {
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorManager.registerListener(mListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopListening() {
        mSensorManager.unregisterListener(mListener);
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }

    private SensorEventListener mListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float distance = sensorEvent.values[0];
            if (distance == DISTANCE_IDLE) {
                if (mWakeLock == null) {
                    mWakeLock = mPowerManager.newWakeLock
                            (PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "FlukyPick:");
                }
                if (!mWakeLock.isHeld()) {
                    mWakeLock.acquire(120000);
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
}
