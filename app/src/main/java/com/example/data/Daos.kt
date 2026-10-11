package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface RoommateDao {
    @Query("SELECT * FROM roommates ORDER BY totalPenaltyPoints DESC")
    fun getAllRoommates(): Flow<List<RoommateEntity>>

    @Query("SELECT * FROM roommates WHERE id = :id LIMIT 1")
    suspend fun getRoommateById(id: Long): RoommateEntity?

    @Query("SELECT * FROM roommates WHERE isCurrentUser = 1 LIMIT 1")
    fun getCurrentUser(): Flow<RoommateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoommate(roommate: RoommateEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(roommates: List<RoommateEntity>)

    @Update
    suspend fun updateRoommate(roommate: RoommateEntity)

    @Delete
    suspend fun deleteRoommate(roommate: RoommateEntity)
}

@Dao
interface TransgressionDao {
    @Query("SELECT * FROM transgressions ORDER BY timestamp DESC")
    fun getAllTransgressions(): Flow<List<TransgressionEntity>>

    @Query("SELECT * FROM transgressions WHERE isOfficialLedger = 1 ORDER BY timestamp DESC")
    fun getOfficialTransgressions(): Flow<List<TransgressionEntity>>

    @Query("SELECT * FROM transgressions WHERE offenderId = :roommateId ORDER BY timestamp DESC")
    fun getTransgressionsForRoommate(roommateId: Long): Flow<List<TransgressionEntity>>

    @Query("SELECT * FROM transgressions WHERE witnessConfirmed = 0 ORDER BY timestamp DESC")
    fun getPendingWitnessTransgressions(): Flow<List<TransgressionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransgression(transgression: TransgressionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transgressions: List<TransgressionEntity>)

    @Update
    suspend fun updateTransgression(transgression: TransgressionEntity)

    @Query("DELETE FROM transgressions WHERE id = :id")
    suspend fun deleteTransgressionById(id: Long)

    @Query("DELETE FROM transgressions")
    suspend fun clearAll()
}

@Dao
interface TournamentDao {
    @Query("SELECT * FROM tournaments ORDER BY votes DESC")
    fun getAllTournaments(): Flow<List<TournamentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTournament(tournament: TournamentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tournaments: List<TournamentEntity>)

    @Update
    suspend fun updateTournament(tournament: TournamentEntity)
}

@Dao
interface HouseSettingsDao {
    @Query("SELECT * FROM house_settings WHERE id = 1 LIMIT 1")
    fun getSettings(): Flow<HouseSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settings: HouseSettingsEntity)
}
