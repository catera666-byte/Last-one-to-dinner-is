package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.HouseSettingsEntity
import com.example.data.RoommateEntity
import com.example.ui.theme.CodexPurple
import com.example.ui.theme.EmeraldWitness
import com.example.ui.theme.NuggetGold
import com.example.ui.theme.ShameCrimson

@Composable
fun SubscriptionsScreen(
    roommates: List<RoommateEntity>,
    currentUser: RoommateEntity?,
    houseSettings: HouseSettingsEntity?,
    onToggleDues: (RoommateEntity) -> Unit,
    onUpgradeMembership: (String) -> Unit,
    onGenerateInviteLink: (Context) -> Unit
) {
    val context = LocalContext.current
    val potTotal = houseSettings?.housePotBalance ?: (roommates.count { it.monthlyPaid } * 2.0)
    val nextDue = houseSettings?.nextDueDay ?: "1st/2nd of month"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            // Household $2 Pool Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, NuggetGold, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "💰 HOUSEHOLD $2 MONTHLY POOL",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.1.sp
                                ),
                                color = NuggetGold
                            )
                            Text(
                                text = "Every roommate pays $2/mo. Dues collected 1st or 2nd.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = NuggetGold.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "$${String.format("%.2f", potTotal)} Pot",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = NuggetGold
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Monthly Dues Check-In (Tap to toggle):",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    roommates.forEach { r ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .clickable { onToggleDues(r) }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (r.monthlyPaid) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                    contentDescription = null,
                                    tint = if (r.monthlyPaid) EmeraldWitness else ShameCrimson,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${r.avatarEmoji} ${r.name}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = if (r.monthlyPaid) "PAID ($2.00)" else "DUE ($2.00)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (r.monthlyPaid) EmeraldWitness else ShameCrimson
                                )
                            )
                        }
                    }
                }
            }
        }

        // Shareable Link & Google Auth & Vault Encryption
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Security,
                                contentDescription = null,
                                tint = EmeraldWitness,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Secure Vault & Google Sign-In",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = EmeraldWitness.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "AES-256 ENCRYPTED 🔒",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = EmeraldWitness,
                                    fontWeight = FontWeight.ExtraBold
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = "User profiles, witness logs & house funds are cryptographically protected. Shareable invite links let roommates join anytime via their Google account.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Button(
                        onClick = { onGenerateInviteLink(context) },
                        colors = ButtonDefaults.buttonColors(containerColor = CodexPurple, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("generate_shareable_link_button")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Create Shareable Google Invite Link", fontWeight = FontWeight.Bold)
                    }

                    // Referral Bonus Box
                    val user = currentUser ?: roommates.firstOrNull()
                    if (user != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Your Referral Code: ${user.referralCode}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = NuggetGold
                                    )
                                    Text(
                                        text = "Earned $${String.format("%.2f", user.referralEarnings)} referral & sign-up bonuses",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = NuggetGold.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "BONUS ACTIVE",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = NuggetGold,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Trademarked Merch & Swag Showcase (IQ200 Gold Royalty)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, Brush.horizontalGradient(listOf(NuggetGold, CodexPurple)), RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.swag_apparel),
                            contentDescription = "Trademarked Apparel Collection",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "👕 TRADEMARKED APPAREL & SWAG (IQ200)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.1.sp
                                ),
                                color = NuggetGold
                            )
                            Text(
                                text = "Monthly print-on-demand streetwear hoodie, mug & mystery box",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
        }

        // Subscription Tiers
        item {
            Text(
                text = "💎 Membership Tiers & Exclusive Landing Page Access",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Note: If you leave your subscription, you cannot view the recorded instances landing page!",
                style = MaterialTheme.typography.bodySmall,
                color = ShameCrimson
            )
        }

        // Tier 1: $1 / Month Base Member
        item {
            SubscriptionTierCard(
                title = "Base House Member",
                price = "$1.00 / month",
                badge = "ESSENTIAL",
                badgeColor = NuggetGold,
                perks = listOf(
                    "Access to full recorded instances landing page",
                    "Official witness signoff & logging permissions",
                    "Participation in weekly house leaderboard",
                    "Export CSV for monthly house meetings"
                ),
                isCurrent = currentUser?.membershipTier?.contains("$1") == true,
                onSelect = { onUpgradeMembership("Base Member ($1/mo)") }
            )
        }

        // Tier 2: $4.99 / Month Silver Nugget
        item {
            SubscriptionTierCard(
                title = "Silver Nugget Subscriber",
                price = "$4.99 / month",
                badge = "POPULAR",
                badgeColor = CodexPurple,
                perks = listOf(
                    "All Base Member perks included",
                    "Custom notification sound alerts on new entries",
                    "Global tournament voting multiplier (2x vote weight)",
                    "Silver crown flair on household leaderboard"
                ),
                isCurrent = currentUser?.membershipTier?.contains("$4.99") == true,
                onSelect = { onUpgradeMembership("Silver Nugget ($4.99/mo)") }
            )
        }

        // Tier 3: $9.99 / Month Gold Royalty (IQ200 Apparel)
        item {
            SubscriptionTierCard(
                title = "Gold Royalty (IQ200 Status)",
                price = "$9.99 / month",
                badge = "ULTIMATE SWAG",
                badgeColor = ShameCrimson,
                perks = listOf(
                    "All Base & Silver perks included",
                    "Print-on-demand trademarked TwatNugget apparel box",
                    "Random custom branded swag delivered monthly",
                    "Access to Private AI Codex rulebook archives",
                    "Gold Crown avatar flair & priority AI tribunal review"
                ),
                isCurrent = currentUser?.membershipTier?.contains("$9.99") == true,
                onSelect = { onUpgradeMembership("Gold Royalty ($9.99/mo)") }
            )
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SubscriptionTierCard(
    title: String,
    price: String,
    badge: String,
    badgeColor: Color,
    perks: List<String>,
    isCurrent: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = if (isCurrent) androidx.compose.foundation.BorderStroke(2.dp, NuggetGold) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = price,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = NuggetGold
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = badgeColor.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = badgeColor,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            perks.forEach { perk ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = "✨", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = perk,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onSelect,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCurrent) EmeraldWitness else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (isCurrent) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isCurrent) "ACTIVE PLAN ✓" else "SELECT PLAN",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
