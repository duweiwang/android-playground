//package com.example.wangduwei.demos.notification;
//
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//
//import com.github.chaohong.applicationdemo.R;
//
//public class NotificationSecondActivity extends AppCompatActivity {
//
//    private static final int NOTIFICATION_ID = 123;
//
//    private static final String TAG = "NotificationSecondActiv";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notification_second);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//        int number = getIntent().getIntExtra("number",0);
//        long time = getIntent().getLongExtra("time",0);
//        TextView numberView = (TextView) findViewById(R.id.number);
//        numberView.setText("number:" + number);
//        TextView timeView = (TextView) findViewById(R.id.time);
//        timeView.setText("time:" + time);
//        Log.v(TAG, "number:" + number + ",time:" + time);
///*        if( number > 0 ) {
//            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
//            managerCompat.cancel(getNotificationId(number));
//        }*/
//    }
//
//    public void closePage(View view) {
//        this.finish();
//    }
//
//    private int getNotificationId(int number) {
//        return NOTIFICATION_ID*1000 + number;
//    }
//
//}
