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

import android.app.Activity;
import android.content.Context;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class Helpers {

    private static final int ID_REQUEST_READ_PHONE_STATE = 2;

    public static boolean requiresPhoneStateReadPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, READ_PHONE_STATE) != PERMISSION_GRANTED;
    }

    private static void requestReadPhoneStatePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{READ_PHONE_STATE}, ID_REQUEST_READ_PHONE_STATE);
    }

    public static void ensurePhoneStateReadPermission(Activity activity) {
        if (requiresPhoneStateReadPermission(activity)) {
            requestReadPhoneStatePermission(activity);
        }
    }
}
