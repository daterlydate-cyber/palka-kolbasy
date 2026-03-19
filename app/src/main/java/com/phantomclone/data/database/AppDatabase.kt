package com.phantomclone.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.phantomclone.data.database.dao.InstalledAppDao
import com.phantomclone.data.database.dao.ProfileDao
import com.phantomclone.data.database.entity.InstalledAppEntity
import com.phantomclone.data.database.entity.ProfileEntity

/**
 * Main Room database for PhantomClone.
 *
 * Stores [ProfileEntity] and [InstalledAppEntity] records.
 * Migrations: increment [DATABASE_VERSION] and add a Migration object in [AppDatabase.MIGRATIONS]
 * whenever the schema changes.
 */
@Database(
    entities = [ProfileEntity::class, InstalledAppEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao
    abstract fun installedAppDao(): InstalledAppDao

    companion object {
        const val DATABASE_NAME = "phantom_clone_db"
        const val DATABASE_VERSION = 1
    }
}
