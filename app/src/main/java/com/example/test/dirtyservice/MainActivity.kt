package com.example.test.dirtyservice

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(this, LocalService::class.java))
        startService(Intent(this, RemoteService::class.java))
        MyJobService.StartJob(this)
    }
}