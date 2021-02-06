package com.coral.android.background;

import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class JobSchedulerDemo {

    public static final int MY_BG_JOB = 0;

    public static void scheduleJob(Context context) {

        // 1. 使用 JobSchedule 连接到不按流量计费的网络时调度网络作业
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler js = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo job = new JobInfo.Builder(
                    MY_BG_JOB,
                    new ComponentName(context, MyJobService.class))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setRequiresCharging(true)
                    .build();
            js.schedule(job);
        }

    }

    public class MyJobService extends Service {

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
