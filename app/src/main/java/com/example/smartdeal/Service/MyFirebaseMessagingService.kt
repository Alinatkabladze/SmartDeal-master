package com.example.smartdeal.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.core.app.TaskStackBuilder
import androidx.media.app.NotificationCompat
import com.example.smartdeal.R
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        Log.d("TOKEN_UPDATE", p0!!)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage!!.notification != null) {
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            var url: String? = ""
            if (remoteMessage.data != null)
                url = remoteMessage.data["image"]

            if (!TextUtils.isEmpty(url)) {
                val finalUrl = url
                Handler(Looper.getMainLooper()).post {
                    Picasso.get()
                        .load(finalUrl)
                        .placeholder(R.mipmap.ic_launcher_round)
                        .into(object : Target {
                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                            }

                            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

                            }

                            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                                showNotification(
                                    this@MyFirebaseMessagingService,
                                    title,
                                    body,
                                    null,
                                    bitmap
                                )
                            }


                        })


                }

            } else {
                showNotification(this@MyFirebaseMessagingService, title, body, null, null)
            }
        } else {
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]
            var url: String? = remoteMessage.data["image"]

            if (!TextUtils.isEmpty(url)) {
                val finalUrl = url
                Handler(Looper.getMainLooper()).post {
                    Picasso.get()
                        .load(finalUrl)
                        .placeholder(R.mipmap.ic_launcher_round)
                        .into(object : Target {
                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                            }

                            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

                            }

                            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                                showNotification(
                                    this@MyFirebaseMessagingService,
                                    title,
                                    body,
                                    null,
                                    bitmap
                                )
                            }


                        })


                }

            } else {
                showNotification(this@MyFirebaseMessagingService, title, body, null, null)
            }
        }
    }


    private fun showNotification(context: Context, title: String?, body: String?, intent: Intent?, bitmap: Bitmap?) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = Random().nextInt()
        val channelId = "edmtdev-9113"
        val channelName = "EDMTDevKotlin"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        val builder: androidx.core.app.NotificationCompat.Builder
        if (bitmap != null)
            builder = androidx.core.app.NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(bitmap)
                .setContentTitle(title)
                .setContentText(body)
        else
            builder = androidx.core.app.NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)

        if (intent != null) {
            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addNextIntent(intent)
            val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentIntent(resultPendingIntent)
        }
        notificationManager.notify(notificationId, builder.build())

    }
}