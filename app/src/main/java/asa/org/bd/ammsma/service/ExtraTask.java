package asa.org.bd.ammsma.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by Mahfujur Rahman Khan on 6/25/2018.
 */
public class ExtraTask {



    public enum TaskType
    {
        Assign(1), Reset(2), Stop(3), Kill(4);

        int taskTypeCode;

        TaskType(int taskTypeCode) {
            this.taskTypeCode = taskTypeCode;
        }
    }




    public void serviceTimerTaskReset(TaskType task, Context context)
    {

        if(TaskType.Assign.taskTypeCode == task.taskTypeCode)
        {
            Intent i = new Intent(context.getApplicationContext(),LogoutTimerService.class);
            context.startService(i);

            if(context.startService(new Intent(context.getApplicationContext(), LogoutTimerService.class)) != null) {
                try
                {
                    LogoutTimerService logoutTimerService = new LogoutTimerService();
                    logoutTimerService.timerStart(context);

                }
                catch (Exception e)
                {
                    Log.i("ServiceActivityError",e.getLocalizedMessage());
                }

            }
            else {
                serviceTimerTaskReset(TaskType.Assign,context);
            }
        }
        else if(TaskType.Reset.taskTypeCode == task.taskTypeCode)
        {

            if(context.startService(new Intent(context.getApplicationContext(), LogoutTimerService.class)) != null) {
                try
                {

                    LogoutTimerService logoutTimerService = new LogoutTimerService();
                    logoutTimerService.timerStart(context);
                }
                catch (Exception e)
                {
                    Log.i("ServiceActivityError",e.getLocalizedMessage());
                }

            }
            else {

                Intent i = new Intent(context.getApplicationContext(),LogoutTimerService.class);
                context.startService(i);
                serviceTimerTaskReset(TaskType.Reset,context);


            }
        }
        else if(TaskType.Stop.taskTypeCode == task.taskTypeCode)
        {
            try {
                LogoutTimerService logoutTimerService = new LogoutTimerService();
                logoutTimerService.timerCancel();
            }
            catch (Exception e)
            {
                Log.i("ServiceStopError",e.getMessage());
            }

        }
        else if(TaskType.Kill.taskTypeCode == task.taskTypeCode)
        {
            try {
                LogoutTimerService logoutTimerService = new LogoutTimerService();
                logoutTimerService.timerCancel();
            }
            catch (Exception e)
            {
                Log.i("ServiceStopError",e.getMessage());
            }

            try {
                context.stopService(new Intent(context.getApplicationContext(),LogoutTimerService.class));
            }
            catch (Exception e)
            {
                Log.i("ServiceStopError",e.getMessage());
            }
        }
    }
}
