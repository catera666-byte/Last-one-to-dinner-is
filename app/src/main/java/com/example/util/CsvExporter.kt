package com.example.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.data.TransgressionEntity
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CsvExporter {

    fun exportToCsv(context: Context, transgressions: List<TransgressionEntity>): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val csvHeader = "ID,Date,Offender,Category,Severity_1_to_10,Multiplier,Total_Points,Incident_Title,Description,Witness_Name,Witness_Statement,AI_Judge,AI_Verdict,Punishment\n"

        val sb = StringBuilder(csvHeader)
        for (t in transgressions) {
            val dateStr = dateFormat.format(Date(t.timestamp))
            val line = listOf(
                t.id.toString(),
                escapeCsv(dateStr),
                escapeCsv(t.offenderName),
                escapeCsv(t.category),
                t.rawSeverity.toString(),
                t.multiplier.toString(),
                t.totalPoints.toString(),
                escapeCsv(t.title),
                escapeCsv(t.description),
                escapeCsv(t.witnessName),
                escapeCsv(t.witnessStatement),
                escapeCsv(t.aiJudgeName),
                escapeCsv(t.aiTribunalVerdict),
                escapeCsv(t.punishmentAssigned)
            ).joinToString(",")
            sb.append(line).append("\n")
        }

        return sb.toString()
    }

    fun shareCsvIntent(context: Context, csvData: String, houseName: String) {
        try {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, "📜 $houseName - Monthly House Meeting Transgression Ledger")
                putExtra(Intent.EXTRA_TEXT, "Here is the official TwatNugget of the Month Ledger for our upcoming house meeting:\n\n$csvData")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "Share Monthly Ledger CSV")
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(shareIntent)
        } catch (_: Exception) {
            // Intent handling fallback
        }
    }

    private fun escapeCsv(value: String): String {
        var clean = value.replace("\"", "\"\"").replace("\n", " ").replace("\r", " ")
        if (clean.contains(",") || clean.contains("\"")) {
            clean = "\"$clean\""
        }
        return clean
    }
}
