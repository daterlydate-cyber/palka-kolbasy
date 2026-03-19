package com.phantomclone.data.database.dao

import androidx.room.*
import com.phantomclone.data.database.entity.ProfileEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for [ProfileEntity].
 *
 * All queries return [Flow] to enable reactive UI updates.
 */
@Dao
interface ProfileDao {

    /** Observe all profiles ordered by creation date (newest first). */
    @Query("SELECT * FROM profiles ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<ProfileEntity>>

    /** Get a single profile by its [id]. */
    @Query("SELECT * FROM profiles WHERE id = :id")
    suspend fun getById(id: Long): ProfileEntity?

    /** Insert a new profile and return its auto-generated ID. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ProfileEntity): Long

    /** Update an existing profile. */
    @Update
    suspend fun update(entity: ProfileEntity)

    /** Delete a profile by [id]. */
    @Query("DELETE FROM profiles WHERE id = :id")
    suspend fun deleteById(id: Long)

    /** Update the lastUsedAt timestamp for a profile. */
    @Query("UPDATE profiles SET lastUsedAt = :timestamp WHERE id = :id")
    suspend fun updateLastUsed(id: Long, timestamp: Long)

    /** Update the isActive flag for a profile. */
    @Query("UPDATE profiles SET isActive = :active WHERE id = :id")
    suspend fun updateActive(id: Long, active: Boolean)

    /** Search profiles by name (case-insensitive). */
    @Query("SELECT * FROM profiles WHERE name LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun search(query: String): Flow<List<ProfileEntity>>
}
