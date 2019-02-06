/**
 * Main3Activity.java
 * Responsible for the chat message view.
 *
 * @author  Brad Yates & Kennedey Oddom Sok
 * @version 1.0
 * @since   11/19/2018
 */

package com.yates.brad.tindar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Bundle b = getIntent().getExtras();
        String name = "";
        if(b != null)
            name = b.getString("name");
        TextView chatName = findViewById(R.id.matches);
        chatName.setText(name);
    }

    public void OnClickSend(View view) {
        EditText chatMsg = findViewById(R.id.chatMsg);
        ScrollView scroll = findViewById(R.id.matchesScroll);
        String msg = chatMsg.getText().toString();
        if(msg.length() > 0){
            TextView tv = new TextView(this);
            tv.setText(msg);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            tv.setPadding(0,0,0,75);
            LinearLayout messageLayout = findViewById(R.id.messageLayout);
            messageLayout.addView(tv);
            chatMsg.setText("");
            scroll.post(new Runnable() {
                @Override
                public void run() {
                    scroll.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    }
}
