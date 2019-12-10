package net.ariflaksito.moviedirectory.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import net.ariflaksito.moviedirectory.Alarm.AlarmBroadcast;
import net.ariflaksito.moviedirectory.R;

public class SetNotifActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    AlarmBroadcast alarmBroadcast;
    static String TAG = "net.ariflaksito.moviedirectory";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notif);

        dialog = new ProgressDialog(this);
        Button btnOn = (Button)findViewById(R.id.btn_set_alarm);
        Button btnOff = (Button)findViewById(R.id.btn_off_alarm);

        alarmBroadcast = new AlarmBroadcast();

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmBroadcast.setInfoAlarm(SetNotifActivity.this, AlarmBroadcast.TYPE_INFO,"05:00", getResources().getString(R.string.message));
                alarmBroadcast.setInfoAlarm(SetNotifActivity.this, AlarmBroadcast.TYPE_NOW,"06:00", null);
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmBroadcast.cancelAlarm(SetNotifActivity.this, AlarmBroadcast.TYPE_NOW);
                alarmBroadcast.cancelAlarm(SetNotifActivity.this, AlarmBroadcast.TYPE_INFO);
            }
        });
    }
}
