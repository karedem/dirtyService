package com.example.test.dirtyservice

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable


class LocalService:Service() {
    companion object{
        const val TAG:String = "Service"
    }

    lateinit var iBinder: MyBinder
    lateinit var serviceConnection: ServiceConnection

    inner class MyBinder:WxAidlInterface.Stub(){
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {
        }

    }

    override fun onBind(intent: Intent?): IBinder? {
        return iBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var flag = Service.START_FLAG_REDELIVERY
        return super.onStartCommand(intent, flag, startId);
    }

    override fun onCreate() {
        super.onCreate()
        iBinder = MyBinder()
        serviceConnection = ServiceConnection()
        //使Service变成前台服务
        //使Service变成前台服务
        var notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var channel = NotificationChannel("101", "test", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel)
        }
        startForeground(1,getNotification());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startService(Intent(this, InnnerService::class.java))
        }
    }

    private fun getNotification():Notification{
        var builder = Notification.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("测试服务")
            .setContentText("我正在运行");
        //设置Notification的ChannelID,否则不能正常显示
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("101");
        }
        var notification = builder.build();
        return notification
    }

    inner class ServiceConnection:android.content.ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e(TAG, "子进程可能被干掉了，拉活")
            //连接中断后回调
            //连接中断后回调
            startService(Intent(this@LocalService, RemoteService::class.java))
            bindService(
                Intent(this@LocalService, RemoteService::class.java), serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        }

    }

    class InnnerService : Service() {
        override fun onCreate() {
            super.onCreate()
            var notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                var channel = NotificationChannel("101", "test", NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
            startForeground(1,getNotification())
            stopSelf()
        }

        private fun getNotification():Notification{
            var builder = Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("测试服务")
                .setContentText("我正在运行");
            //设置Notification的ChannelID,否则不能正常显示
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId("101");
            }
            var notification = builder.build();
            return notification
        }

        @Nullable
        override fun onBind(intent: Intent): IBinder? {
            return null
        }
    }
}