package com.example.test.dirtyservice

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MyJobService :JobService(){
    companion object{
        val TAG = "MyJobService"

        fun StartJob(context: Context) {
            val jobScheduler =
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            //        setPersisted 在设备重启依然执行
            val builder = JobInfo.Builder(
                10, ComponentName(
                    context
                        .getPackageName(), MyJobService::class.java
                        .name
                )
            ).setPersisted(true)
            //小于7.0
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                // 每隔1s 执行一次 job
                builder.setPeriodic(1000)
            } else {
                //延迟执行任务
                //大于7.0的机型发现定时无效果，所需需要进行延时轮询
                builder.setMinimumLatency(1000)
            }
            jobScheduler.schedule(builder.build())
        }
    }


    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.e(TAG, "开启job")
        //如果7.0以上 轮训
        //如果7.0以上 轮训
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StartJob(this)
        }
        val isLocalRun: Boolean = Utils.isRunningService(
            this,
            LocalService::class.java.name
        )
        val isRemoteRun: Boolean = Utils.isRunningService(
            this,
            RemoteService::class.java.name
        )
        //只有其中任何一个服务被干掉了，则进行start
        //只有其中任何一个服务被干掉了，则进行start
        if (!isLocalRun || !isRemoteRun) {
            startService(Intent(this, LocalService::class.java))
            startService(Intent(this, RemoteService::class.java))
        }
        return false
    }

}