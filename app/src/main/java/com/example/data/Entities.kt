package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roommates")
data class RoommateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val avatarColor: Long = 0xFFFFB300,
    val avatarEmoji: String = "👑",
    val googleEmail: String = "",
    val isCurrentUser: Boolean = false,
    val monthlyPaid: Boolean = true,
    val membershipTier: String = "Base Member ($1/mo)",
    val streakMonths: Int = 3,
    val totalPenaltyPoints: Double = 0.0,
    val isExcludedFromLedger: Boolean = false,
    val referralCode: String = "NUGGET-" + (1000..9999).random(),
    val referralEarnings: Double = 4.00,
    val isEncrypted: Boolean = true
)

@Entity(tableName = "transgressions")
data class TransgressionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val offenderId: Long,
    val offenderName: String,
    val category: String,
    val rawSeverity: Int, // 1 to 10
    val multiplier: Double,
    val totalPoints: Double,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val witnessId: Long,
    val witnessName: String,
    val witnessConfirmed: Boolean = true,
    val witnessStatement: String = "",
    val aiJudgeName: String = "Judge GigaByte 3000",
    val aiTribunalVerdict: String = "",
    val isOfficialLedger: Boolean = true,
    val punishmentAssigned: String = ""
)

@Entity(tableName = "tournaments")
data class TournamentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val division: String,
    val leaderName: String,
    val story: String,
    val votes: Int = 142,
    val prizePool: String = "$500 & The Golden Nugget Trophy",
    val aiJudgesNotes: String = "Approved under Clause 4.2 of the Private AI Codex."
)

@Entity(tableName = "house_settings")
data class HouseSettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val houseName: String = "221B Baker St Chaos Manor",
    val monthlyHouseholdFee: Double = 2.00,
    val housePotBalance: Double = 16.00,
    val nextDueDay: String = "1st of next month",
    val isDarkMode: Boolean = true,
    val soundEffectsEnabled: Boolean = true,
    val inviteSecretCode: String = "HOUSE-CHAOS-2026"
)
