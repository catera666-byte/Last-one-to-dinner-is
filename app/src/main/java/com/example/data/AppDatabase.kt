package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        RoommateEntity::class,
        TransgressionEntity::class,
        TournamentEntity::class,
        HouseSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roommateDao(): RoommateDao
    abstract fun transgressionDao(): TransgressionDao
    abstract fun tournamentDao(): TournamentDao
    abstract fun houseSettingsDao(): HouseSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "twatnugget_database"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(database: AppDatabase) {
            val roommateDao = database.roommateDao()
            val transgressionDao = database.transgressionDao()
            val tournamentDao = database.tournamentDao()
            val settingsDao = database.houseSettingsDao()

            val r1 = RoommateEntity(
                id = 1,
                name = "Dave (The Microwave Menace)",
                avatarColor = 0xFFFF5722,
                avatarEmoji = "🔥",
                googleEmail = "dave.sparks@gmail.com",
                isCurrentUser = true,
                monthlyPaid = true,
                membershipTier = "Gold Royalty ($9.99/mo)",
                streakMonths = 6,
                totalPenaltyPoints = 48.5,
                referralCode = "NUGGET-DAVE42",
                referralEarnings = 12.00
            )
            val r2 = RoommateEntity(
                id = 2,
                name = "Sarah (Sleepwalker Chef)",
                avatarColor = 0xFF9C27B0,
                avatarEmoji = "🍳",
                googleEmail = "sarah.chef@gmail.com",
                isCurrentUser = false,
                monthlyPaid = true,
                membershipTier = "Silver Nugget ($4.99/mo)",
                streakMonths = 4,
                totalPenaltyPoints = 32.0,
                referralCode = "NUGGET-SARAH99",
                referralEarnings = 4.00
            )
            val r3 = RoommateEntity(
                id = 3,
                name = "Kevin (Lord of Dropped Keys)",
                avatarColor = 0xFF2196F3,
                avatarEmoji = "🔑",
                googleEmail = "kevin.lockout@gmail.com",
                isCurrentUser = false,
                monthlyPaid = true,
                membershipTier = "Base Member ($1/mo)",
                streakMonths = 2,
                totalPenaltyPoints = 26.4,
                referralCode = "NUGGET-KEV123",
                referralEarnings = 2.00
            )
            val r4 = RoommateEntity(
                id = 4,
                name = "Chloe (Brown Noser Extraordinaire)",
                avatarColor = 0xFF4CAF50,
                avatarEmoji = "🧽",
                googleEmail = "chloe.cleaner@gmail.com",
                isCurrentUser = false,
                monthlyPaid = true,
                membershipTier = "Base Member ($1/mo)",
                streakMonths = 5,
                totalPenaltyPoints = 5.0,
                referralCode = "NUGGET-CHLOE",
                referralEarnings = 6.00
            )

            roommateDao.insertAll(listOf(r1, r2, r3, r4))

            val t1 = TransgressionEntity(
                id = 1,
                title = "Microwaved foil burrito for 4 minutes",
                offenderId = 1,
                offenderName = "Dave (The Microwave Menace)",
                category = "Kitchen / Chore Chaos",
                rawSeverity = 8,
                multiplier = 2.5,
                totalPoints = 20.0,
                description = "Dave attempted to warm a leftover burrito inside heavy aluminum foil. Sparks ignited, smoke alarms screamed at 2 AM, and the kitchen smelled like ozone for three days.",
                timestamp = System.currentTimeMillis() - 86400000L * 2,
                witnessId = 2,
                witnessName = "Sarah (Sleepwalker Chef)",
                witnessConfirmed = true,
                witnessStatement = "I literally had to tackle him with a tea towel while the turntable was shooting plasma bolts.",
                aiJudgeName = "Judge GigaByte 3000",
                aiTribunalVerdict = "GUILTY AS CHARGED. Violation of Thermodynamic Common Sense Codex 4.2. Penalty: Mandatory dishwashing cape for 10 days.",
                isOfficialLedger = true,
                punishmentAssigned = "Wear the Tiara of Shame during next Sunday House Meeting"
            )

            val t2 = TransgressionEntity(
                id = 2,
                title = "Put dish soap into the automatic dishwasher",
                offenderId = 1,
                offenderName = "Dave (The Microwave Menace)",
                category = "Self-Inflicted Stupidity",
                rawSeverity = 7,
                multiplier = 2.0,
                totalPoints = 14.0,
                description = "Used Dawn Ultra dish soap instead of detergent pods. Foam tsunami filled the hallway up to shin level.",
                timestamp = System.currentTimeMillis() - 86400000L * 5,
                witnessId = 3,
                witnessName = "Kevin (Lord of Dropped Keys)",
                witnessConfirmed = true,
                witnessStatement = "I slipped on bubble foam and broke a broom. Dave was floating on a sofa pillow.",
                aiJudgeName = "Supreme Tribunal Arbiter",
                aiTribunalVerdict = "FOAM FELONY FIRST DEGREE. Multiplier doubled for aquatic negligence.",
                isOfficialLedger = true,
                punishmentAssigned = "Deep clean hallway floors with own toothbrush"
            )

            val t3 = TransgressionEntity(
                id = 3,
                title = "Baked 3 AM cookies & forgot them till 7 AM",
                offenderId = 2,
                offenderName = "Sarah (Sleepwalker Chef)",
                category = "Embarrassing",
                rawSeverity = 6,
                multiplier = 1.5,
                totalPoints = 9.0,
                description = "Cookies turned into hardened carbon hockey pucks. One shattered a ceramic coaster.",
                timestamp = System.currentTimeMillis() - 86400000L * 7,
                witnessId = 1,
                witnessName = "Dave (The Microwave Menace)",
                witnessConfirmed = true,
                witnessStatement = "Woke up to charcoal aromatherapy and a crying oven timer.",
                aiJudgeName = "Bailiff Bot 9",
                aiTribunalVerdict = "Culinary homicide confirmed. No edible survivors.",
                isOfficialLedger = true,
                punishmentAssigned = "Provide fresh bakery donuts for whole house"
            )

            val t4 = TransgressionEntity(
                id = 4,
                title = "Washed all roommates' dishes without asking",
                offenderId = 4,
                offenderName = "Chloe (Brown Noser Extraordinaire)",
                category = "Good Deeds (0 Brownie Points)",
                rawSeverity = 10,
                multiplier = 0.0,
                totalPoints = 0.0,
                description = "Chloe cleaned everyone's frying pans hoping for forgiveness on the weekly leaderboard.",
                timestamp = System.currentTimeMillis() - 86400000L * 1,
                witnessId = 2,
                witnessName = "Sarah (Sleepwalker Chef)",
                witnessConfirmed = true,
                witnessStatement = "She smiled suspiciously while scrubbing. Total brown-nosing move.",
                aiJudgeName = "Judge GigaByte 3000",
                aiTribunalVerdict = "APPLICATION OF BROWNIE POINTS STRICTLY DENIED. Rule Book Article 1.4: Good deeds yield 0.0 points ya brown noser.",
                isOfficialLedger = true,
                punishmentAssigned = "Must listen to 5 minutes of house complaints"
            )

            transgressionDao.insertAll(listOf(t1, t2, t3, t4))

            val tourn1 = TournamentEntity(
                id = 1,
                title = "Global Dorm Disaster Championship",
                division = "North America Collegiate",
                leaderName = "Kyle 'Drywall Puncher' (Points: 182.4)",
                story = "Kyle tried to iron his dress shirt while still wearing it to save 30 seconds before a zoom interview. 2nd degree burn & viral TikTok.",
                votes = 2480,
                prizePool = "$1,000 Cash + Diamond Encrusted Nugget Trophy",
                aiJudgesNotes = "Supreme AI Panel awarded 9.9/10 style points for instantaneous self-own."
            )
            val tourn2 = TournamentEntity(
                id = 2,
                title = "World TwatNugget Invitational 2026",
                division = "Worldwide Heavyweight",
                leaderName = "Chad & The Roommate Rebellion (Points: 310.2)",
                story = "Left freezer open for a 3-week vacation in July. Bio-hazard freezer defrost engulfed apartment in mysterious sludge.",
                votes = 1940,
                prizePool = "$2,500 + Full Merch Apparel Box",
                aiJudgesNotes = "Certified bio-weapon grade negligence under Section 9.1."
            )

            tournamentDao.insertAll(listOf(tourn1, tourn2))

            settingsDao.insertOrUpdate(
                HouseSettingsEntity(
                    id = 1,
                    houseName = "221B Baker St Chaos Manor",
                    monthlyHouseholdFee = 2.00,
                    housePotBalance = 18.00,
                    nextDueDay = "1st of next month",
                    isDarkMode = true,
                    soundEffectsEnabled = true,
                    inviteSecretCode = "CHAOS-8820"
                )
            )
        }
    }
}
