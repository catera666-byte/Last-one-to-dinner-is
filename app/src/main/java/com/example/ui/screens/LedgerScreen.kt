package com.example.ui.screens

import android.content.Context
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.AITribunalEngine
import com.example.data.RoommateEntity
import com.example.data.TransgressionEntity
import com.example.ui.components.AddTransgressionDialog
import com.example.ui.theme.CodexPurple
import com.example.ui.theme.EmeraldWitness
import com.example.ui.theme.NuggetGold
import com.example.ui.theme.ShameCrimson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LedgerScreen(
    roommates: List<RoommateEntity>,
    transgressions: List<TransgressionEntity>,
    selectedCategoryFilter: String?,
    onCategoryFilterChange: (String?) -> Unit,
    onLogTransgression: (
        title: String,
        offender: RoommateEntity,
        category: com.example.ai.TransgressionCategory,
        severity: Int,
        description: String,
        witness: RoommateEntity,
        witnessStatement: String
    ) -> Unit,
    onDeleteTransgression: (Long) -> Unit,
    onExportCsv: (Context) -> Unit
) {
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredTransgressions = transgressions.filter {
        selectedCategoryFilter == null || selectedCategoryFilter == "ALL" || it.category.contains(selectedCategoryFilter, ignoreCase = true)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))
                // Ledger Header & Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "📜 Transgression Ledger",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Witness-verified roommate offenses & point multipliers",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // CSV Export for Monthly House Meetings
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CodexPurple.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .clickable { onExportCsv(context) }
                            .testTag("export_csv_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = "Export CSV",
                                tint = CodexPurple,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Export CSV",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = CodexPurple
                            )
                        }
                    }
                }
            }

            // Category Filter Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    item {
                        val isAll = selectedCategoryFilter == null || selectedCategoryFilter == "ALL"
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isAll) NuggetGold else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { onCategoryFilterChange("ALL") }
                        ) {
                            Text(
                                text = "All Offenses",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = if (isAll) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isAll) Color.Black else MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                    items(AITribunalEngine.CATEGORIES) { cat ->
                        val isSelected = selectedCategoryFilter == cat.name
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) CodexPurple else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { onCategoryFilterChange(cat.name) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(cat.emoji, fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${cat.name.split(" ").first()} (${cat.multiplier}x)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }
            }

            if (filteredTransgressions.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("😇", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No transgressions found!",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Either everyone is behaving or someone forgot to log a microwave crime.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredTransgressions, key = { it.id }) { item ->
                    TransgressionCard(
                        transgression = item,
                        onDelete = { onDeleteTransgression(item.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Floating Action Button to Log Transgression
        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = NuggetGold,
            contentColor = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("fab_add_transgression")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Add, contentDescription = "Log Transgression")
                Spacer(modifier = Modifier.width(6.dp))
                Text("Log Incident", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showAddDialog) {
        AddTransgressionDialog(
            roommates = roommates,
            onDismiss = { showAddDialog = false },
            onSubmit = { title, offender, category, severity, description, witness, witnessStatement ->
                onLogTransgression(
                    title,
                    offender,
                    category,
                    severity,
                    description,
                    witness,
                    witnessStatement
                )
                showAddDialog = false
            }
        )
    }
}

@Composable
fun TransgressionCard(
    transgression: TransgressionEntity,
    onDelete: () -> Unit
) {
    val dateFormat = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
    val dateString = dateFormat.format(Date(transgression.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("transgression_card_${transgression.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (transgression.multiplier == 0.0) ShameCrimson.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Row: Offender, Points Badge & Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🤡", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = transgression.offenderName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = dateString,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Points & Multiplier Pill
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (transgression.multiplier == 0.0) ShameCrimson.copy(alpha = 0.2f) else NuggetGold.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "+${String.format("%.1f", transgression.totalPoints)} pts (${transgression.rawSeverity} × ${transgression.multiplier}x)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = if (transgression.multiplier == 0.0) ShameCrimson else NuggetGold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Incident Title
            Text(
                text = transgression.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Category & Multiplier badge
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = CodexPurple.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "📁 ${transgression.category}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CodexPurple,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Description
            if (transgression.description.isNotBlank()) {
                Text(
                    text = transgression.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mandatory Witness Verification Section (Rule 2.1)
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = EmeraldWitness.copy(alpha = 0.1f),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldWitness.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Default.Verified,
                        contentDescription = "Witness Verified",
                        tint = EmeraldWitness,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Verified Witness: ${transgression.witnessName}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldWitness
                            )
                        )
                        if (transgression.witnessStatement.isNotBlank()) {
                            Text(
                                text = "\"${transgression.witnessStatement}\"",
                                style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // AI Tribunal Ruling
            if (transgression.aiTribunalVerdict.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Gavel,
                                contentDescription = null,
                                tint = NuggetGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${transgression.aiJudgeName} Ruling:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NuggetGold
                                )
                            )
                        }
                        Text(
                            text = transgression.aiTribunalVerdict,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        if (transgression.punishmentAssigned.isNotBlank()) {
                            Text(
                                text = "⚖️ Punishment: ${transgression.punishmentAssigned}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = ShameCrimson,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
