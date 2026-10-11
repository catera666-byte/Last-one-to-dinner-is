package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RoommateEntity
import com.example.ui.theme.BronzeThird
import com.example.ui.theme.CodexPurple
import com.example.ui.theme.GoldFirst
import com.example.ui.theme.NuggetGold
import com.example.ui.theme.ShameCrimson
import com.example.ui.theme.SilverSecond

@Composable
fun LeaderboardScreen(
    roommates: List<RoommateEntity>,
    wheelResult: String?,
    isWheelSpinning: Boolean,
    onSpinWheel: () -> Unit
) {
    var timeframeTab by remember { mutableIntStateOf(0) } // 0: Weekly Rankings, 1: Monthly All-Time
    val sortedRoommates = remember(roommates) {
        roommates.sortedByDescending { it.totalPenaltyPoints }
    }
    val reigningOffender = sortedRoommates.firstOrNull()

    val rotationAngle by animateFloatAsState(
        targetValue = if (isWheelSpinning) 1440f else 0f,
        animationSpec = tween(durationMillis = 1200),
        label = "wheel_spin"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            // Hall of Shame Crowning Podium
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Brush.linearGradient(listOf(NuggetGold, ShameCrimson)), RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "👑 TWATNUGGET OF THE MONTH",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp
                        ),
                        color = NuggetGold
                    )
                    Text(
                        text = "Official Throne of Household Shame (Crowned on 1st/2nd)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (reigningOffender != null) {
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(Brush.radialGradient(listOf(NuggetGold, ShameCrimson))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = reigningOffender.avatarEmoji, fontSize = 42.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = reigningOffender.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ShameCrimson.copy(alpha = 0.2f),
                            modifier = Modifier.padding(top = 6.dp)
                        ) {
                            Text(
                                text = "🏆 Leading with ${String.format("%.1f", reigningOffender.totalPenaltyPoints)} Penalty Pts",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ShameCrimson
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Text(
                            text = "Sentence: Must wear the Golden Nugget Tiara & fund house snacks",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Timeframe selector
        item {
            TabRow(
                selectedTabIndex = timeframeTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = timeframeTab == 0,
                    onClick = { timeframeTab = 0 },
                    text = { Text("Weekly Offender Rankings", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = timeframeTab == 1,
                    onClick = { timeframeTab = 1 },
                    text = { Text("Monthly House Ledger", fontWeight = FontWeight.Bold) }
                )
            }
        }

        // Leaderboard List
        itemsIndexed(sortedRoommates) { index, roommate ->
            val rankColor = when (index) {
                0 -> GoldFirst
                1 -> SilverSecond
                2 -> BronzeThird
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("leaderboard_item_$index"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = if (index == 0) androidx.compose.foundation.BorderStroke(1.5.dp, NuggetGold) else null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Rank Number / Medal
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(rankColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (index) {
                                    0 -> "🥇"
                                    1 -> "🥈"
                                    2 -> "🥉"
                                    else -> "#${index + 1}"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = if (index < 3) 18.sp else 14.sp,
                                color = rankColor
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${roommate.avatarEmoji} ${roommate.name}",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (roommate.isCurrentUser) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = CodexPurple.copy(alpha = 0.2f),
                                        modifier = Modifier.padding(start = 6.dp)
                                    ) {
                                        Text(
                                            text = "YOU",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = CodexPurple
                                            ),
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "${roommate.membershipTier} • ${roommate.streakMonths} mo streak",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Score
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${String.format("%.1f", roommate.totalPenaltyPoints)} pts",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = if (index == 0) ShameCrimson else NuggetGold
                            )
                        )
                        Text(
                            text = if (index == 0) "Top Offender" else "Rank #${index + 1}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Wheel of Punishments Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Casino,
                            contentDescription = null,
                            tint = NuggetGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "🎡 Wheel of Ultimate Punishments",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Spin to assign mandatory retribution to the weekly offenders!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Animated Wheel graphic
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Brush.sweepGradient(listOf(NuggetGold, ShameCrimson, CodexPurple, NuggetGold)))
                            .rotate(rotationAngle)
                            .border(3.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🎯", fontSize = 36.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (wheelResult != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ShameCrimson.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ShameCrimson),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "⚖️ PUNISHMENT DECREE:",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ShameCrimson
                                    )
                                )
                                Text(
                                    text = wheelResult,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }

                    Button(
                        onClick = onSpinWheel,
                        enabled = !isWheelSpinning,
                        colors = ButtonDefaults.buttonColors(containerColor = NuggetGold, contentColor = Color.Black),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("spin_wheel_button")
                    ) {
                        if (isWheelSpinning) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tribunal Spinning...")
                        } else {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("SPIN THE WHEEL", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
