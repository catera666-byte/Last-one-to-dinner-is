package com.example.data

import kotlinx.coroutines.flow.Flow

class TwatNuggetRepository(
    private val roommateDao: RoommateDao,
    private val transgressionDao: TransgressionDao,
    private val tournamentDao: TournamentDao,
    private val houseSettingsDao: HouseSettingsDao
) {
    val allRoommates: Flow<List<RoommateEntity>> = roommateDao.getAllRoommates()
    val currentUser: Flow<RoommateEntity?> = roommateDao.getCurrentUser()
    val allTransgressions: Flow<List<TransgressionEntity>> = transgressionDao.getAllTransgressions()
    val officialTransgressions: Flow<List<TransgressionEntity>> = transgressionDao.getOfficialTransgressions()
    val pendingWitnessTransgressions: Flow<List<TransgressionEntity>> = transgressionDao.getPendingWitnessTransgressions()
    val tournaments: Flow<List<TournamentEntity>> = tournamentDao.getAllTournaments()
    val houseSettings: Flow<HouseSettingsEntity?> = houseSettingsDao.getSettings()

    suspend fun insertRoommate(roommate: RoommateEntity) = roommateDao.insertRoommate(roommate)
    suspend fun updateRoommate(roommate: RoommateEntity) = roommateDao.updateRoommate(roommate)
    suspend fun deleteRoommate(roommate: RoommateEntity) = roommateDao.deleteRoommate(roommate)

    suspend fun insertTransgression(transgression: TransgressionEntity): Long {
        val id = transgressionDao.insertTransgression(transgression)
        // If official, add points to offender
        if (transgression.isOfficialLedger && transgression.witnessConfirmed) {
            val offender = roommateDao.getRoommateById(transgression.offenderId)
            if (offender != null) {
                val updatedPoints = offender.totalPenaltyPoints + transgression.totalPoints
                roommateDao.updateRoommate(offender.copy(totalPenaltyPoints = updatedPoints))
            }
        }
        return id
    }

    suspend fun updateTransgression(transgression: TransgressionEntity) {
        transgressionDao.updateTransgression(transgression)
    }

    suspend fun confirmWitness(transgressionId: Long, witnessStatement: String) {
        // Find and confirm
        // In full app flow, update transgression and recalculate points
    }

    suspend fun deleteTransgression(id: Long) = transgressionDao.deleteTransgressionById(id)

    suspend fun updateHouseSettings(settings: HouseSettingsEntity) =
        houseSettingsDao.insertOrUpdate(settings)

    suspend fun voteTournament(tournamentId: Long) {
        // helper
    }

    suspend fun recordMonthlyDuesPayment(roommateId: Long) {
        val r = roommateDao.getRoommateById(roommateId)
        if (r != null) {
            roommateDao.updateRoommate(r.copy(monthlyPaid = true))
        }
    }
}
