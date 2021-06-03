package com.irzstudio.runningapp.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.irzstudio.runningapp.db.RunDatabase
import com.irzstudio.runningapp.util.Constant
import com.irzstudio.runningapp.util.Constant.Companion.KEY_FIRST_TIME_TOGGLE
import com.irzstudio.runningapp.util.Constant.Companion.KEY_NAME
import com.irzstudio.runningapp.util.Constant.Companion.KEY_WEIGHT
import com.irzstudio.runningapp.util.Constant.Companion.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAppDb(app: Application): RunDatabase{
        return Room.databaseBuilder(app, RunDatabase::class.java, Constant.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRunDao(app: Application) =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideName(sharedPreferences: SharedPreferences) =
        sharedPreferences.getString(KEY_NAME, "") ?: ""

    @Singleton
    @Provides
    fun provideWeight(sharedPreferences: SharedPreferences) =
        sharedPreferences.getFloat(KEY_WEIGHT, 80f)

    @Singleton
    @Provides
    fun provideFirstTimeToggle(sharedPreferences: SharedPreferences) =
        sharedPreferences.getBoolean(KEY_FIRST_TIME_TOGGLE, true)

}