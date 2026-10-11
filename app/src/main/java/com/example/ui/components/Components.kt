package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ai.AITribunalEngine
import com.example.ai.TransgressionCategory
import com.example.data.RoommateEntity
import com.example.ui.theme.CodexPurple
import com.example.ui.theme.EmeraldWitness
import com.example.ui.theme.NuggetGold
import com.example.ui.theme.NuggetGoldDark
import com.example.ui.theme.ShameCrimson

@Composable
fun AppTopBar(
    houseName: String,
    potBalance: Double,
    isDarkMode: Boolean,
    soundEnabled: Boolean,
    onToggleDark: () -> Unit,
    onToggleSound: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(NuggetGold, ShameCrimson)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👑", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "TWATNUGGET",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.2.sp
                            ),
                            color = NuggetGold
                        )
                        Text(
                            text = houseName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // House Pot Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NuggetGold.copy(alpha = 0.5f)),
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "💰 $", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NuggetGold)
                            Text(
                                text = String.format("%.2f", potBalance),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    // Sound Toggle
                    IconButton(
                        onClick = onToggleSound,
                        modifier = Modifier.size(36.dp).testTag("toggle_sound_button")
                    ) {
                        Icon(
                            imageVector = if (soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                            contentDescription = "Toggle Audio Jingle",
                            tint = if (soundEnabled) NuggetGold else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Late Night Dark Mode Toggle
                    IconButton(
                        onClick = onToggleDark,
                        modifier = Modifier.size(36.dp).testTag("toggle_dark_mode_button")
                    ) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.Brightness7 else Icons.Default.Brightness4,
                            contentDescription = "Toggle Late Night Mode",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationAlertBanner(
    message: String?,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = message != null,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        if (message != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, NuggetGold, RoundedCornerShape(12.dp)),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = NuggetGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddTransgressionDialog(
    roommates: List<RoommateEntity>,
    onDismiss: () -> Unit,
    onSubmit: (
        title: String,
        offender: RoommateEntity,
        category: TransgressionCategory,
        severity: Int,
        description: String,
        witness: RoommateEntity,
        witnessStatement: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedOffender by remember { mutableStateOf(roommates.firstOrNull()) }
    var selectedCategory by remember { mutableStateOf(AITribunalEngine.CATEGORIES.first()) }
    var severity by remember { mutableFloatStateOf(6f) }
    var description by remember { mutableStateOf("") }
    var selectedWitness by remember {
        val witnessCandidate = roommates.firstOrNull { it.id != selectedOffender?.id } ?: roommates.firstOrNull()
        mutableStateOf(witnessCandidate)
    }
    var witnessStatement by remember { mutableStateOf("") }

    val totalPoints = severity.toInt() * selectedCategory.multiplier

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚖️ Record Transgression",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = NuggetGold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title field
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Incident Summary") },
                    placeholder = { Text("e.g. Left burner on high all night") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("incident_title_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Offender Picker
                Text(
                    text = "Select Offender:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    items(roommates) { r ->
                        val isSelected = selectedOffender?.id == r.id
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) ShameCrimson else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clickable {
                                    selectedOffender = r
                                    if (selectedWitness?.id == r.id) {
                                        selectedWitness = roommates.firstOrNull { it.id != r.id }
                                    }
                                }
                                .padding(2.dp)
                        ) {
                            Text(
                                text = "${r.avatarEmoji} ${r.name.split(" ").first()}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Category Picker
                Text(
                    text = "Transgression Category:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    items(AITribunalEngine.CATEGORIES) { cat ->
                        val isSelected = selectedCategory.name == cat.name
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) CodexPurple else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clickable { selectedCategory = cat }
                                .padding(2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = cat.emoji)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${cat.name} (${cat.multiplier}x)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }

                // Good Deeds Brown Noser Warning!
                if (selectedCategory.isGoodDeedWarning) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ShameCrimson.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ShameCrimson),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = ShameCrimson,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Rule 1.4: Good deeds grant ZERO brownie points ya brown noser! Multiplier = 0.0x",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = ShameCrimson
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Severity Slider & Multiplier calculation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Severity (1 - 10): ${severity.toInt()}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Points: ${severity.toInt()} × ${selectedCategory.multiplier} = ${String.format("%.1f", totalPoints)} pts",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = NuggetGold
                        )
                    )
                }
                Slider(
                    value = severity,
                    onValueChange = { severity = it },
                    valueRange = 1f..10f,
                    steps = 8,
                    modifier = Modifier.fillMaxWidth()
                )

                // Mandatory Witness Selection
                Text(
                    text = "Mandatory Co-Signing Witness (Rule 2.1):",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmeraldWitness
                    )
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    val eligibleWitnesses = roommates.filter { it.id != selectedOffender?.id }
                    items(eligibleWitnesses) { w ->
                        val isSelected = selectedWitness?.id == w.id
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) EmeraldWitness else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clickable { selectedWitness = w }
                                .padding(2.dp)
                        ) {
                            Text(
                                text = "👁️ ${w.name.split(" ").first()}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // Witness Testimony statement
                OutlinedTextField(
                    value = witnessStatement,
                    onValueChange = { witnessStatement = it },
                    label = { Text("Witness Statement") },
                    placeholder = { Text("e.g. I saw the sparks flying with my own eyes.") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Detailed Incident Description") },
                    placeholder = { Text("Explain exactly what happened...") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = NuggetGold,
                        modifier = Modifier
                            .clickable {
                                val off = selectedOffender ?: return@clickable
                                val wit = selectedWitness ?: roommates.firstOrNull { it.id != off.id } ?: off
                                onSubmit(
                                    title,
                                    off,
                                    selectedCategory,
                                    severity.toInt(),
                                    description,
                                    wit,
                                    witnessStatement
                                )
                            }
                            .testTag("submit_transgression_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Gavel, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Submit to AI Tribunal",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
