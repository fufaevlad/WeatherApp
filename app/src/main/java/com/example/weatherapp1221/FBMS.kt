package com.example.weatherapp1221

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FBMS: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//Обработка здесь
    }
}