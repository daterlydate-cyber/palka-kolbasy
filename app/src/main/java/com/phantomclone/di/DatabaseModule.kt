package com.phantomclone.di

import android.content.Context
import androidx.room.Room
import com.phantomclone.data.database.AppDatabase
import com.phantomclone.data.database.dao.InstalledAppDao
import com.phantomclone.data.database.dao.ProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

/**
 * Hilt module providing database-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideProfileDao(database: AppDatabase): ProfileDao =
        database.profileDao()

    @Provides
    fun provideInstalledAppDao(database: AppDatabase): InstalledAppDao =
        database.installedAppDao()

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }
}
