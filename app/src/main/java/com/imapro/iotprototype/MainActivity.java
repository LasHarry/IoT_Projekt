package com.imapro.iotprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.muddzdev.styleabletoast.StyleableToast;

public class MainActivity extends AppCompatActivity {

    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void startRating(View v) {
        checkConn();

        Intent intent = new Intent(MainActivity.this, ClientActivity.class);
        if (isConnected == true) {
            startActivity(intent);
        } else {
            StyleableToast.makeText(this, "No connection available!", R.style.errorToast).show();
        }
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


    }

    private void checkConn() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) ||
                (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
            isConnected = true;
        } else {
            isConnected = false;
        }
    }
}
