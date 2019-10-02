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

package pk.codebase.flukypick;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import pk.codebase.flukypick.utils.Helpers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Helpers.ensurePhoneStateReadPermission(this);
    }
}
