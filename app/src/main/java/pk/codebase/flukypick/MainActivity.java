package pk.codebase.flukypick;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import pk.codebase.flukypick.utils.Helpers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // check READ_PHONE_STATE permission
        if (!Helpers.hasReadPhoneStatePermission(this)) {
            Helpers.requestReadPhoneStatePermission(MainActivity.this);
        }
    }

}
