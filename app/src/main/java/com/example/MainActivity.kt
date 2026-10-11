package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.TwatNuggetViewModel
import com.example.ui.components.AppTopBar
import com.example.ui.components.NotificationAlertBanner
import com.example.ui.screens.AITribunalScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.LedgerScreen
import com.example.ui.screens.SubscriptionsScreen
import com.example.ui.screens.TournamentsScreen
import com.example.ui.theme.NuggetGold
import com.example.ui.theme.TwatNuggetTheme

class MainActivity : ComponentActivity() {
    private val viewModel: TwatNuggetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
            val soundEnabled by viewModel.soundEnabled.collectAsStateWithLifecycle()
            val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
            val roommates by viewModel.allRoommates.collectAsStateWithLifecycle()
            val transgressions by viewModel.allTransgressions.collectAsStateWithLifecycle()
            val tournaments by viewModel.tournaments.collectAsStateWithLifecycle()
            val houseSettings by viewModel.houseSettings.collectAsStateWithLifecycle()
            val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
            val notificationAlert by viewModel.notificationAlert.collectAsStateWithLifecycle()
            val categoryFilter by viewModel.selectedCategoryFilter.collectAsStateWithLifecycle()
            val commercialPlaying by viewModel.commercialPlaying.collectAsStateWithLifecycle()
            val commercialLyric by viewModel.commercialLyric.collectAsStateWithLifecycle()
            val wheelResult by viewModel.wheelResult.collectAsStateWithLifecycle()
            val isWheelSpinning by viewModel.isWheelSpinning.collectAsStateWithLifecycle()
            val shareToast by viewModel.shareToastMessage.collectAsStateWithLifecycle()

            val context = LocalContext.current

            LaunchedEffect(shareToast) {
                if (shareToast != null) {
                    Toast.makeText(context, shareToast, Toast.LENGTH_LONG).show()
                    viewModel.clearShareToast()
                }
            }

            TwatNuggetTheme(darkTheme = isDarkMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing,
                    topBar = {
                        AppTopBar(
                            houseName = houseSettings?.houseName ?: "TwatNugget Manor",
                            potBalance = houseSettings?.housePotBalance ?: (roommates.count { it.monthlyPaid } * 2.0),
                            isDarkMode = isDarkMode,
                            soundEnabled = soundEnabled,
                            onToggleDark = { viewModel.toggleDarkMode() },
                            onToggleSound = { viewModel.toggleSound() }
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 8.dp,
                            modifier = Modifier
                                .navigationBarsPadding()
                                .testTag("bottom_navigation_bar")
                        ) {
                            NavigationBarItem(
                                selected = selectedTab == 0,
                                onClick = { viewModel.setTab(0) },
                                icon = { Icon(Icons.Default.Description, contentDescription = "Ledger") },
                                label = { Text("Ledger") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = NuggetGold,
                                    selectedTextColor = NuggetGold,
                                    indicatorColor = NuggetGold.copy(alpha = 0.2f)
                                ),
                                modifier = Modifier.testTag("nav_tab_ledger")
                            )
                            NavigationBarItem(
                                selected = selectedTab == 1,
                                onClick = { viewModel.setTab(1) },
                                icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Leaderboard") },
                                label = { Text("Rankings") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = NuggetGold,
                                    selectedTextColor = NuggetGold,
                                    indicatorColor = NuggetGold.copy(alpha = 0.2f)
                                ),
                                modifier = Modifier.testTag("nav_tab_rankings")
                            )
                            NavigationBarItem(
                                selected = selectedTab == 2,
                                onClick = { viewModel.setTab(2) },
                                icon = { Icon(Icons.Default.Gavel, contentDescription = "AI Tribunal") },
                                label = { Text("AI Judges") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = NuggetGold,
                                    selectedTextColor = NuggetGold,
                                    indicatorColor = NuggetGold.copy(alpha = 0.2f)
                                ),
                                modifier = Modifier.testTag("nav_tab_tribunal")
                            )
                            NavigationBarItem(
                                selected = selectedTab == 3,
                                onClick = { viewModel.setTab(3) },
                                icon = { Icon(Icons.Default.Public, contentDescription = "Tournaments") },
                                label = { Text("Global") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = NuggetGold,
                                    selectedTextColor = NuggetGold,
                                    indicatorColor = NuggetGold.copy(alpha = 0.2f)
                                ),
                                modifier = Modifier.testTag("nav_tab_global")
                            )
                            NavigationBarItem(
                                selected = selectedTab == 4,
                                onClick = { viewModel.setTab(4) },
                                icon = { Icon(Icons.Default.Diamond, contentDescription = "Subscriptions & Swag") },
                                label = { Text("Swag & Dues") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = NuggetGold,
                                    selectedTextColor = NuggetGold,
                                    indicatorColor = NuggetGold.copy(alpha = 0.2f)
                                ),
                                modifier = Modifier.testTag("nav_tab_subscriptions")
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // Content by tab
                        when (selectedTab) {
                            0 -> LedgerScreen(
                                roommates = roommates,
                                transgressions = transgressions,
                                selectedCategoryFilter = categoryFilter,
                                onCategoryFilterChange = { viewModel.setCategoryFilter(it) },
                                onLogTransgression = { title, offender, cat, sev, desc, wit, witStmt ->
                                    viewModel.logTransgression(title, offender, cat, sev, desc, wit, witStmt) {}
                                },
                                onDeleteTransgression = { viewModel.deleteTransgression(it) },
                                onExportCsv = { viewModel.exportLedgerCsv(it) }
                            )
                            1 -> LeaderboardScreen(
                                roommates = roommates,
                                wheelResult = wheelResult,
                                isWheelSpinning = isWheelSpinning,
                                onSpinWheel = { viewModel.spinWheelOfPunishment() }
                            )
                            2 -> AITribunalScreen()
                            3 -> TournamentsScreen(
                                tournaments = tournaments,
                                commercialPlaying = commercialPlaying,
                                commercialLyric = commercialLyric,
                                onPlayCommercial = { viewModel.playCommercial() },
                                onVoteTournament = { viewModel.voteTournament(it) }
                            )
                            4 -> SubscriptionsScreen(
                                roommates = roommates,
                                currentUser = currentUser,
                                houseSettings = houseSettings,
                                onToggleDues = { viewModel.toggleDuesPaid(it) },
                                onUpgradeMembership = { viewModel.upgradeMembership(it) },
                                onGenerateInviteLink = { viewModel.generateShareableInviteLink(it) }
                            )
                        }

                        // Notification alert overlay
                        NotificationAlertBanner(
                            message = notificationAlert,
                            onDismiss = { viewModel.clearNotification() }
                        )
                    }
                }
            }
        }
    }
}
