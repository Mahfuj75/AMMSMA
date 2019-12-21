package asa.org.bd.ammsma.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import asa.org.bd.ammsma.activity.LoginAmmsActivity;

public class LogoutTimerService extends Service {
    public LogoutTimerService() {
    }

    public static CountDownTimer timer;

    @Override
    public void onCreate(){
        // TODO Auto-generated method stub
        super.onCreate();

    }

    public  void timerStart(final Context context)
    {

        try {
            if(timer!=null)
            {
                timer.cancel();
            }

        }
        catch (Exception e)
        {
            Log.i("ErrorService",e.getMessage());
        }
        timer = new CountDownTimer(1800000, 1000) {
            public void onTick(long millisUntilFinished) {
                /*Log.i("Timer", String.valueOf(millisUntilFinished));*/
            }

            public void onFinish() {
                Intent i= new Intent(context.getApplicationContext(), LoginAmmsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(i);
                try {
                    if(timer!=null)
                    {
                        timer.cancel();
                    }
                }
                catch (Exception e)
                {
                    Log.i("ServiceError",e.getMessage());
                }

                try {
                    context.stopService(new Intent(context.getApplicationContext(),LogoutTimerService.class));
                }
                catch (Exception e)
                {
                    Log.i("ServiceStopError", e.getMessage());
                }



            }
        }.start();
    }
    public  void timerCancel()
    {
        try {
            timer.cancel();
        }
        catch (Exception e)
        {
            Log.i("ErrorService",e.getMessage());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timerStart(LogoutTimerService.this);
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        timerStart(LogoutTimerService.this);

        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ServiceDestroy", "ServiceDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
