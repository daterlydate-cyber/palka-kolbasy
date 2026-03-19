package com.phantomclone.data.database.dao

import androidx.room.*
import com.phantomclone.data.database.entity.InstalledAppEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for [InstalledAppEntity].
 */
@Dao
interface InstalledAppDao {

    /** Observe all apps installed in a given profile. */
    @Query("SELECT * FROM installed_apps WHERE profileId = :profileId ORDER BY installedAt DESC")
    fun observeByProfile(profileId: Long): Flow<List<InstalledAppEntity>>

    /** Insert a new app record. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: InstalledAppEntity): Long

    /** Delete an app record by its profile and package name. */
    @Query("DELETE FROM installed_apps WHERE profileId = :profileId AND packageName = :packageName")
    suspend fun deleteByPackageName(profileId: Long, packageName: String)

    /** Delete an app record by its ID. */
    @Query("DELETE FROM installed_apps WHERE id = :id")
    suspend fun deleteById(id: Long)

    /** Delete all apps belonging to a profile (called when profile is deleted). */
    @Query("DELETE FROM installed_apps WHERE profileId = :profileId")
    suspend fun deleteByProfile(profileId: Long)
}
