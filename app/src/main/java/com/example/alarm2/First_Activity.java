package com.example.alarm2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.icu.text.SimpleDateFormat;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class First_Activity extends AppCompatActivity {

    private TextView tvTime;
    private Calendar calendar;
    private TextView setTime;
    public static String tmps;
    public static String clickTime=null;
    public static String nowTime=null;
    private static boolean flag= false;
    //手机震动
    private Vibrator vibrator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        TimeThread timeThread = new TimeThread();
        timeThread.start();
        init();//


    }



    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                     }
                catch (InterruptedException e) {
                    e.printStackTrace();
                     }
             } while (true);
        }
    }


    private void init() {

        vibrator= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);//


        setTime = (TextView) findViewById(R.id.setClick);
        tvTime = (TextView) findViewById(R.id.time);


        Button button1 = (Button) findViewById(R.id.new1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTime();
                flag=true;
            }
        });
    }

    private void initTime() {
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());//年份
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {


            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                clickTime = (format(hourOfDay) + ":" + format(minute)).toString();
                tmps = "设置闹钟时间为" + format(hourOfDay) + ":" + format(minute);
                setTime.setText(tmps.toString());
            }
        }, hour, minute, true).show();
    }
    //在主线程里面处理消息并更新UI界面

    protected Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:


                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日       HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());
                    tvTime.setText(simpleDateFormat.format(date).toString());
                    SimpleDateFormat simpleDateFormatTwo = new SimpleDateFormat("HH:mm");
                    nowTime = simpleDateFormatTwo.format(date).toString();
                    Log.i("TGA",nowTime);
                    if(flag==true){
                        if(nowTime.equals(clickTime)){
                            long[]pattern={100,400,100,400};
                            vibrator.vibrate(pattern,1);


                        }else {

                            if (vibrator!=null){
                                vibrator.cancel();
                            }

                        }
                    }
                    break;
                default:
                    break;

            }

        }
    };

    private String format(int time) {
        String str = "" + time;
        if (str.length() == 1) {
            str = "0" + str;
        }
        return str;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
