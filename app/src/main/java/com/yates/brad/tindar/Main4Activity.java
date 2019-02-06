/**
 * Main4Activity.java
 * Reponsible for the view that shows all your matches.
 *
 * @author  Brad Yates & Kennedey Oddom Sok
 * @version 1.0
 * @since   11/19/2018
 */

package com.yates.brad.tindar;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Main4Activity extends AppCompatActivity {
    private static final String TAG = "Main4Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            ArrayList<String> matches = getIntent().getExtras().getStringArrayList("matches");
            if(!matches.isEmpty()){
                LinearLayout matchLayout = findViewById(R.id.matchLayout);
                matchLayout.removeAllViews();
                for (String match : matches) {
                    TextView tv = new TextView(this);
                    tv.setText(match);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setPadding(25,25,0,25);
                    tv.setBackgroundResource(R.drawable.textlines);
                    matchLayout.addView(tv);
                }
            }
        }
    }
}
