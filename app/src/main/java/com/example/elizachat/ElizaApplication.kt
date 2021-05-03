package com.example.elizachat

import android.app.Application

class ElizaApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        ConversationRepository.initialize(this)
    }
}