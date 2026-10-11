package com.example.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

data class TransgressionCategory(
    val name: String,
    val multiplier: Double,
    val emoji: String,
    val description: String,
    val isGoodDeedWarning: Boolean = false
)

data class AIJudge(
    val id: String,
    val name: String,
    val role: String,
    val avatarEmoji: String,
    val personality: String
)

data class RuleBookSection(
    val code: String,
    val title: String,
    val clause: String,
    val penaltyExample: String
)

object AITribunalEngine {

    val CATEGORIES = listOf(
        TransgressionCategory(
            name = "Kitchen / Chore Chaos",
            multiplier = 2.5,
            emoji = "🍳",
            description = "Microwave explosions, dish soap foam tsunamis, grease fires, burnt toast."
        ),
        TransgressionCategory(
            name = "Self-Inflicted Stupidity",
            multiplier = 2.0,
            emoji = "🤦",
            description = "Locking yourself out in underwear, walking into glass doors, DIY disasters."
        ),
        TransgressionCategory(
            name = "Drunken Blunders",
            multiplier = 2.2,
            emoji = "🍺",
            description = "Midnight philosophical rants to the fridge, waking roommates at 3 AM."
        ),
        TransgressionCategory(
            name = "Awkward Social Flub",
            multiplier = 1.8,
            emoji = "😬",
            description = "Waving back at stranger who wasn't waving at you, reply-all emails."
        ),
        TransgressionCategory(
            name = "Embarrassing",
            multiplier = 1.5,
            emoji = "😳",
            description = "Mismatched shoes in public, pants zipper open during zoom presentation."
        ),
        TransgressionCategory(
            name = "Non-Intentional Stupidity",
            multiplier = 1.2,
            emoji = "🤷",
            description = "Looking for phone while talking on it, glasses on forehead."
        ),
        TransgressionCategory(
            name = "Unwarranted Tech Destruction",
            multiplier = 2.8,
            emoji = "💻",
            description = "Spilling soda into roommate's keyboard, dropping router behind radiator."
        ),
        TransgressionCategory(
            name = "Good Deeds (0 Brownie Points)",
            multiplier = 0.0,
            emoji = "🧽",
            description = "Brown nosing clause! Washing dishes unasked yields 0 points and maximum mockery.",
            isGoodDeedWarning = true
        )
    )

    val JUDGES = listOf(
        AIJudge(
            id = "gigabyte",
            name = "Judge GigaByte 3000",
            role = "Chief AI Magistrate",
            avatarEmoji = "🤖",
            personality = "Ruthless algorithmic logic, zero sympathy for excuses, quotes obscure household codes."
        ),
        AIJudge(
            id = "supreme",
            name = "Supreme Nugget Arbiter",
            role = "Tribunal Grand Judge",
            avatarEmoji = "⚖️",
            personality = "High-court theatrics, delivers grandiose verdicts with poetic roast burns."
        ),
        AIJudge(
            id = "bailiff",
            name = "Bailiff Bot 9",
            role = "Enforcer & Penalty Assigner",
            avatarEmoji = "🚨",
            personality = "Specializes in creative domestic punishments (tiara wearing, dish cape duties)."
        )
    )

    val PRIVATE_RULEBOOK = listOf(
        RuleBookSection(
            code = "CODEX-1.4",
            title = "The Anti-Brown-Nosing Injunction",
            clause = "Good deeds done in an attempt to curry favor, cleanse guilt, or gain brownie points shall be granted EXACTLY 0.0 points. All roommates are legally required to mock the offender.",
            penaltyExample = "Zero score reduction + 5-minute verbal grilling during Sunday House Meeting."
        ),
        RuleBookSection(
            code = "CODEX-2.1",
            title = "Mandatory Eyewitness Verification Protocol",
            clause = "No transgression shall be entered into the Official House Ledger without co-signature and testimony from at least one verified household resident.",
            penaltyExample = "Unverified rumors remain in Pending Limbo until sworn testimony is provided."
        ),
        RuleBookSection(
            code = "CODEX-3.5",
            title = "The Microwave Plasma Clause",
            clause = "Placing foil, metal forks, or whole uncut eggs in the microwave triggers automatic 2.5x point multiplier and hazardous kitchen sanctions.",
            penaltyExample = "Must wear the Golden Nugget Tiara to all meals for 3 consecutive days."
        ),
        RuleBookSection(
            code = "CODEX-4.9",
            title = "Monthly Crowning & House Pot Forfeit",
            clause = "On the 1st/2nd of each month, the leader of the weekly/monthly ledger is officially christened 'TwatNugget of the Month' and must buy the house pizza from the $2 member pool.",
            penaltyExample = "Official entry in the Worldwide Sandbox Hall of Fame."
        ),
        RuleBookSection(
            code = "CODEX-5.2",
            title = "Apparel Brand Privilege (IQ200)",
            clause = "Gold Royalty subscribers maintaining unblemished tracking records unlock trademarked TwatNugget streetwear apparel and monthly swag crate.",
            penaltyExample = "Excluded members are barred from viewing the recorded instances landing page."
        )
    )

    val PUNISHMENT_LIST = listOf(
        "👑 Must wear the Golden Nugget Tiara during next House Meeting",
        "🧽 Wash all kitchen dishes while wearing the Cape of Shame for 5 days",
        "🍕 Pay for house pizza night using the $2 membership pool",
        "🎤 Sing a 2-minute apology ballad to the microwave in front of roommates",
        "🗑️ Take out all trash and recycling bins barefoot in winter for 1 week",
        "☕ Serve morning coffee/tea to all roommates with formal butler bow",
        "🧼 Scrub bathroom grout using a novelty toothbrush",
        "📢 Give a 3-minute PowerPoint presentation explaining 'Why I Am The TwatNugget'"
    )

    suspend fun evaluateIncident(
        offenderName: String,
        category: String,
        severity: Int,
        description: String,
        witnessName: String,
        witnessStatement: String
    ): Triple<String, String, String> = withContext(Dispatchers.IO) {
        val selectedJudge = JUDGES.random()
        val randomPunishment = PUNISHMENT_LIST.random()

        val prompt = """
            You are ${selectedJudge.name}, an AI Bot Judge on the official TwatNugget of the Month Tribunal panel.
            You judge household roommate transgressions using ruthless comedy, witty legal jargon, and the Official Private Rulebook.
            
            CASE DETAILS:
            - Offender: $offenderName
            - Category: $category (Severity: $severity/10)
            - Incident Description: $description
            - Eyewitness ($witnessName) Testimony: "$witnessStatement"
            - Rulebook Context: Good deeds give 0 brownie points (Rule 1.4). Every incident must be witnessed.
            
            Deliver a hilarious 2-sentence official tribunal verdict roast, citing a fictional codex rule and declaring guilt!
        """.trimIndent()

        val apiKey = BuildConfig.GEMINI_API_KEY
        var verdict = ""

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val url = URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                conn.connectTimeout = 10000
                conn.readTimeout = 10000

                val jsonBody = JSONObject().apply {
                    val contents = JSONArray().apply {
                        put(JSONObject().apply {
                            val parts = JSONArray().apply {
                                put(JSONObject().put("text", prompt))
                            }
                            put("parts", parts)
                        })
                    }
                    put("contents", contents)
                }

                OutputStreamWriter(conn.outputStream).use { writer ->
                    writer.write(jsonBody.toString())
                    writer.flush()
                }

                if (conn.responseCode == 200) {
                    val responseStr = conn.inputStream.bufferedReader().use { it.readText() }
                    val jsonResp = JSONObject(responseStr)
                    val candidates = jsonResp.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val content = candidates.getJSONObject(0).optJSONObject("content")
                        val parts = content?.optJSONArray("parts")
                        verdict = parts?.getJSONObject(0)?.optString("text")?.trim() ?: ""
                    }
                }
            } catch (_: Exception) {
                // Fallback to comedy engine
            }
        }

        if (verdict.isBlank()) {
            verdict = when {
                category.contains("Good Deeds", ignoreCase = true) ->
                    "GUILTY OF SHAMELESS BROWN-NOSING! Under Codex Section 1.4, good deeds award EXACTLY ZERO brownie points. The court sentences you to instant roommate mockery."
                category.contains("Kitchen", ignoreCase = true) ->
                    "GUILTY OF CULINARY ARSON! Codex Section 3.5 violated. Eyewitness $witnessName's testimony confirms utter appliance devastation. Multiplier strictly enforced."
                category.contains("Self-Inflicted", ignoreCase = true) ->
                    "VERDICT: MAXIMUM CONCENTRATION OF SELF-OWN! Under Codex Section 2.1, the Tribunal finds no outside perpetrator except your own questionable choices."
                else ->
                    "VERDICT CONFIRMED BY TRIBUNAL ALGORITHM! Offense recorded in the permanent ledger with full severity multiplier. No appeals shall be entertained."
            }
        }

        Triple(selectedJudge.name, verdict, randomPunishment)
    }
}
