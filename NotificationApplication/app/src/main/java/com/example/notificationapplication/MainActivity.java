package com.example.notificationapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.widget.*;


public class MainActivity extends AppCompatActivity {

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    final Calendar myCalendar = Calendar.getInstance () ;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super .onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_main );
    }

    private void scheduleNotification (Notification notification, long delay) {
        //create a new intent to start the notification publisher

        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        //put some extra data in it: the notification's ID and the notification itself (built with notificationCompat)
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID, 1 );
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);
        //create a new pendingIntent to pass onto alarm manager; alarm manager will be able to use the data to send the notif
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //create a new alarm manager object
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        //send the notification using the specified delay; delay must be ms from now
        alarmManager.set(AlarmManager.RTC_WAKEUP, delay, pendingIntent);
    }

    //here we are returning the notification object to be sent to the user
    private Notification getNotification (String content) {
        //Gets the information by calling the methods
        Intent helloIntent = new Intent(this, HelloReceiver.class); //Change the broadcast receiver to be specific
        //helloIntent.setAction("Hello!");
        PendingIntent pintent = PendingIntent.getBroadcast(this, 0, helloIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Scheduled Notification" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable.ic_launcher_foreground) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId(NOTIFICATION_CHANNEL_ID) ;
        builder.addAction(R.drawable. ic_launcher_foreground , "Hello!" , pintent);
        return builder.build() ;
    }


    //when the user has finished selecting a date, set the the calendar variables to that date
    DatePickerDialog.OnDateSetListener setDateVariables = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet (DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year) ;
            myCalendar.set(Calendar.MONTH, monthOfYear) ;
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth) ;
        }
    };

    TimePickerDialog.OnTimeSetListener setTimeVariables = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            myCalendar.set(Calendar.HOUR, hour) ;
            myCalendar.set(Calendar.MINUTE, minute);
            myCalendar.set(Calendar.SECOND, 0);
            myCalendar.set(Calendar.MILLISECOND, 0);
            updateLabel();
        }
    };

    //create a datepicker dialog; use the setCalendarVariables listener; set the current date
    //to the current year, month, and day.
    public void setDate (View view) {
        new DatePickerDialog(MainActivity. this, setDateVariables,
                myCalendar.get(Calendar.YEAR ),
                myCalendar.get(Calendar.MONTH ),
                myCalendar.get(Calendar.DAY_OF_MONTH )
        ).show();
    }

    //do the same for the timepicker
    public void setTime(View view){

        new TimePickerDialog(MainActivity.this, setTimeVariables,
            myCalendar.get(Calendar.HOUR),
            myCalendar.get(Calendar.MINUTE),
            true

        ).show();

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy" ; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat , Locale. getDefault ());

        //this is the chosenTime from the user (TODO: need to get specified time from user)
        Log.d("notif-debug", "Cal: " + myCalendar.get(Calendar.MONTH));
        long chosenTime = myCalendar.getTimeInMillis();
        long currentTime = System.currentTimeMillis();
        long delay = chosenTime - currentTime;
        System.out.println(delay);
        String date = sdf.format(chosenTime);
        scheduleNotification(getNotification(date), System.currentTimeMillis() + delay);
    }
}