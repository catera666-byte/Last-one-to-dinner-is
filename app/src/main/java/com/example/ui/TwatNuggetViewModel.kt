package com.example.ui

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AITribunalEngine
import com.example.ai.TransgressionCategory
import com.example.audio.AudioJinglePlayer
import com.example.data.AppDatabase
import com.example.data.HouseSettingsEntity
import com.example.data.RoommateEntity
import com.example.data.TournamentEntity
import com.example.data.TransgressionEntity
import com.example.data.TwatNuggetRepository
import com.example.util.CsvExporter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TwatNuggetViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TwatNuggetRepository

    val allRoommates: StateFlow<List<RoommateEntity>>
    val allTransgressions: StateFlow<List<TransgressionEntity>>
    val tournaments: StateFlow<List<TournamentEntity>>
    val houseSettings: StateFlow<HouseSettingsEntity?>
    val currentUser: StateFlow<RoommateEntity?>

    // UI State variables
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _soundEnabled = MutableStateFlow(true)
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    private val _notificationAlert = MutableStateFlow<String?>(null)
    val notificationAlert: StateFlow<String?> = _notificationAlert.asStateFlow()

    private val _isEvaluatingAI = MutableStateFlow(false)
    val isEvaluatingAI: StateFlow<Boolean> = _isEvaluatingAI.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow<String?>("ALL")
    val selectedCategoryFilter: StateFlow<String?> = _selectedCategoryFilter.asStateFlow()

    private val _commercialPlaying = MutableStateFlow(false)
    val commercialPlaying: StateFlow<Boolean> = _commercialPlaying.asStateFlow()

    private val _commercialLyric = MutableStateFlow("👑 TwatNugget of the Month Commercial Ready!")
    val commercialLyric: StateFlow<String> = _commercialLyric.asStateFlow()

    private val _wheelResult = MutableStateFlow<String?>(null)
    val wheelResult: StateFlow<String?> = _wheelResult.asStateFlow()

    private val _isWheelSpinning = MutableStateFlow(false)
    val isWheelSpinning: StateFlow<Boolean> = _isWheelSpinning.asStateFlow()

    private val _shareToastMessage = MutableStateFlow<String?>(null)
    val shareToastMessage: StateFlow<String?> = _shareToastMessage.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = TwatNuggetRepository(
            database.roommateDao(),
            database.transgressionDao(),
            database.tournamentDao(),
            database.houseSettingsDao()
        )

        allRoommates = repository.allRoommates.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        allTransgressions = repository.allTransgressions.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        tournaments = repository.tournaments.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        houseSettings = repository.houseSettings.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

        currentUser = repository.currentUser.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
    }

    fun setTab(index: Int) {
        _selectedTab.value = index
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun toggleSound() {
        _soundEnabled.value = !_soundEnabled.value
    }

    fun setCategoryFilter(category: String?) {
        _selectedCategoryFilter.value = category
    }

    fun clearNotification() {
        _notificationAlert.value = null
    }

    fun clearShareToast() {
        _shareToastMessage.value = null
    }

    fun triggerVibrateAlert() {
        try {
            val context = getApplication<Application>()
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(180, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(180)
            }
        } catch (_: Exception) {}
    }

    fun logTransgression(
        title: String,
        offender: RoommateEntity,
        category: TransgressionCategory,
        severity: Int,
        description: String,
        witness: RoommateEntity,
        witnessStatement: String,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            _isEvaluatingAI.value = true
            val (judgeName, verdict, punishment) = AITribunalEngine.evaluateIncident(
                offenderName = offender.name,
                category = category.name,
                severity = severity,
                description = description,
                witnessName = witness.name,
                witnessStatement = witnessStatement
            )

            val totalPoints = severity * category.multiplier

            val transgression = TransgressionEntity(
                title = title.ifBlank { "Untitled Incident" },
                offenderId = offender.id,
                offenderName = offender.name,
                category = category.name,
                rawSeverity = severity,
                multiplier = category.multiplier,
                totalPoints = totalPoints,
                description = description,
                timestamp = System.currentTimeMillis(),
                witnessId = witness.id,
                witnessName = witness.name,
                witnessConfirmed = true,
                witnessStatement = witnessStatement.ifBlank { "Witnessed in person by ${witness.name}." },
                aiJudgeName = judgeName,
                aiTribunalVerdict = verdict,
                isOfficialLedger = true,
                punishmentAssigned = punishment
            )

            repository.insertTransgression(transgression)
            _isEvaluatingAI.value = false

            // Notification & Audio
            _notificationAlert.value = "📜 NEW TRANSGRESSION: ${offender.name} logged +${String.format("%.1f", totalPoints)} pts by witness ${witness.name}!"
            triggerVibrateAlert()
            if (_soundEnabled.value) {
                AudioJinglePlayer.playSoundEffect(viewModelScope, AudioJinglePlayer.SoundEffect.GAVEL_BANG)
            }
            onComplete()
        }
    }

    fun deleteTransgression(id: Long) {
        viewModelScope.launch {
            repository.deleteTransgression(id)
            _notificationAlert.value = "🗑️ Transgression expunged from ledger."
        }
    }

    fun spinWheelOfPunishment() {
        if (_isWheelSpinning.value) return
        _isWheelSpinning.value = true
        _wheelResult.value = null

        viewModelScope.launch {
            if (_soundEnabled.value) {
                AudioJinglePlayer.playSoundEffect(viewModelScope, AudioJinglePlayer.SoundEffect.WHEEL_CLICK)
            }
            kotlinx.coroutines.delay(1200)
            val punishment = AITribunalEngine.PUNISHMENT_LIST.random()
            _wheelResult.value = punishment
            _isWheelSpinning.value = false
            _notificationAlert.value = "⚖️ TRIBUNAL PUNISHMENT ASSIGNED: $punishment"
            if (_soundEnabled.value) {
                AudioJinglePlayer.playSoundEffect(viewModelScope, AudioJinglePlayer.SoundEffect.CROWN_FANFARE)
            }
        }
    }

    fun playCommercial() {
        if (_commercialPlaying.value) {
            AudioJinglePlayer.stopJingle()
            _commercialPlaying.value = false
            _commercialLyric.value = "Commercial paused."
            return
        }
        _commercialPlaying.value = true
        AudioJinglePlayer.playCommercialJingle(
            scope = viewModelScope,
            onNoteStep = { step, lyric ->
                _commercialLyric.value = lyric
            },
            onFinished = {
                _commercialPlaying.value = false
                _commercialLyric.value = "👑 Join TwatNugget for $1/mo or unlock Gold Royalty Apparel!"
            }
        )
    }

    fun voteTournament(tournament: TournamentEntity) {
        viewModelScope.launch {
            val updated = tournament.copy(votes = tournament.votes + 1)
            // Local update
            _notificationAlert.value = "🗳️ Vote cast for '${tournament.title}'! Leader: ${tournament.leaderName}"
            if (_soundEnabled.value) {
                AudioJinglePlayer.playSoundEffect(viewModelScope, AudioJinglePlayer.SoundEffect.CASH_REGISTER)
            }
        }
    }

    fun toggleDuesPaid(roommate: RoommateEntity) {
        viewModelScope.launch {
            val newStatus = !roommate.monthlyPaid
            repository.updateRoommate(roommate.copy(monthlyPaid = newStatus))
            _notificationAlert.value = if (newStatus) {
                "💸 $2.00 monthly household fee collected from ${roommate.name}!"
            } else {
                "⚠️ ${roommate.name} marked as unpaid for this month's $2 pool."
            }
            if (newStatus && _soundEnabled.value) {
                AudioJinglePlayer.playSoundEffect(viewModelScope, AudioJinglePlayer.SoundEffect.CASH_REGISTER)
            }
        }
    }

    fun upgradeMembership(tier: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: allRoommates.value.firstOrNull() ?: return@launch
            val updated = user.copy(membershipTier = tier)
            repository.updateRoommate(updated)
            _notificationAlert.value = "💎 Upgraded to $tier! Rewards & swag unlocked."
            if (_soundEnabled.value) {
                AudioJinglePlayer.playSoundEffect(viewModelScope, AudioJinglePlayer.SoundEffect.CROWN_FANFARE)
            }
        }
    }

    fun exportLedgerCsv(context: Context) {
        val list = allTransgressions.value
        val settings = houseSettings.value
        val houseName = settings?.houseName ?: "TwatNugget House"
        val csv = CsvExporter.exportToCsv(context, list)
        CsvExporter.shareCsvIntent(context, csv, houseName)
    }

    fun generateShareableInviteLink(context: Context): String {
        val code = houseSettings.value?.inviteSecretCode ?: "CHAOS-8820"
        val link = "https://twatnugget.app/join?house=$code&inviter=Dave"
        _shareToastMessage.value = "📋 Invite link copied: $link\n(Sign up with Google & Vault Encrypted Profile)"
        return link
    }
}
