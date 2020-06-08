package com.example.test.dirtyservice

import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils


class Utils{
    companion object{
        fun isRunningService(context: Context, name: String?): Boolean {
            val am =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningServices =
                am.getRunningServices(100)
            for (info in runningServices) {
                if (TextUtils.equals(info.service.className, name)) {
                    return true
                }
            }
            return false
        }
    }

}