package com.irzstudio.runningapp.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.irzstudio.runningapp.R
import com.irzstudio.runningapp.ui.activity.MainActivity
import com.irzstudio.runningapp.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped


@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {
    @ServiceScoped
    @Provides
    fun providesFusedLocationProviderClient(
        @ApplicationContext context: Context) = FusedLocationProviderClient(context)

    @ServiceScoped
    @Provides
    fun providesBasedNotifitcationBilder(
        @ApplicationContext context: Context, pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(context, Constant.NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.ic_directions_run)
        .setContentTitle("Running App")
        .setContentText("00:00:00")
        .setContentIntent(pendingIntent)

    @ServiceScoped
    @Provides
    fun provideActivityPendingIntent(
        @ApplicationContext context: Context
    ) = PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java).apply {
        action = Constant.ACTION_SHOW_TRACKING_FRAGMENT
    }, PendingIntent.FLAG_UPDATE_CURRENT)
}