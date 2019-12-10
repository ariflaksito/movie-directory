package net.ariflaksito.moviedirectory.Alarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import android.widget.Toast;

import net.ariflaksito.moviedirectory.Entities.Movie;
import net.ariflaksito.moviedirectory.Loaders.MyAsyncTask;
import net.ariflaksito.moviedirectory.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static net.ariflaksito.moviedirectory.Alarm.NotificationUtils.ANDROID_CHANNEL_ID;

public class AlarmBroadcast extends BroadcastReceiver {

    private final int NOTIF_ID_INFO = 100;
    private final int NOTIF_ID_NOW = 101;
    public static final String TYPE_INFO = "InfoAlarm";
    public static final String TYPE_NOW = "NowAlarm";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";
    Context context;
    static String TAG = "net.ariflaksito.moviedirectory";

    int notifId;
    String title;

    public AlarmBroadcast() { }

    @Override
    public void onReceive(final Context context, Intent intent) {

        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        notifId = type.equalsIgnoreCase(TYPE_INFO) ? NOTIF_ID_INFO : NOTIF_ID_NOW;
        this.context = context;

        title = context.getResources().getString(R.string.app_name);

        if(notifId==NOTIF_ID_NOW){

            // CONNECT KE API SERVER UNTUK AMBIL DATA MOVIE
            new MyAsyncTask(new MyAsyncTask.AsyncResponse(){
                @Override
                public void processFinish(ArrayList<Movie> output) {
                    for(int i=0;i<output.size();i++){
                        Movie movie = output.get(i);
                        String infos = context.getResources().getString(R.string.new_movie)+ movie.getMovieTitle();

                        // VALIDASI RELEASE HARI INI
                        try {
                            Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(movie.getMovieDate());

                            // HARDCODE TGL UNTUL TESTING
                            // Date now = new SimpleDateFormat("yyyy-MM-dd").parse("2018-07-25");

                            Date now = new Date();

                            if(releaseDate.equals(now)){
                                showNotif(context, title, infos, notifId+i);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }).execute();

        }else
            showNotif(context, title, message, notifId);


    }

    private void showNotif(Context context, String title, String message, int notifId){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ANDROID_CHANNEL_ID)
                .setSmallIcon(R.drawable.movie_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationUtils notifUtils = new NotificationUtils(context);
            notifUtils.getManager().notify(notifId, builder.build());
        }else {
            manager.notify(notifId, builder.build());
        }

    }

    public void setInfoAlarm(Context context, String type, String time, String message){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcast.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        int notifId = type.equalsIgnoreCase(TYPE_INFO) ? NOTIF_ID_INFO : NOTIF_ID_NOW;

        String timeArray[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent =  PendingIntent.getBroadcast(context, notifId, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(context, context.getResources().getString(R.string.set_alarm), Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context, String type){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcast.class);

        int requestCode = type.equalsIgnoreCase(TYPE_INFO) ? NOTIF_ID_INFO : NOTIF_ID_NOW;


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(context, context.getResources().getString(R.string.off_alarm), Toast.LENGTH_SHORT).show();
    }

}
