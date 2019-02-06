/**
 * MainActivity.java
 * Reponsible for the main splash screen on load.
 *
 * @author  Brad Yates & Kennedey Oddom Sok
 * @version 1.0
 * @since   11/19/2018
 */
package com.yates.brad.tindar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }
}
