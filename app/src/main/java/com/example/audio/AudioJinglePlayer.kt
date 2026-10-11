package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

object AudioJinglePlayer {
    private var jingleJob: Job? = null
    private var isPlaying = false

    fun isJinglePlaying(): Boolean = isPlaying

    fun stopJingle() {
        jingleJob?.cancel()
        isPlaying = false
    }

    /**
     * Synthesizes and plays a fun, catchy, bouncy comedy jingle using AudioTrack PCM sine waves.
     * Tune: Bouncy comedy theme song for the TwatNugget Commercial!
     */
    fun playCommercialJingle(
        scope: CoroutineScope,
        onNoteStep: ((Int, String) -> Unit)? = null,
        onFinished: (() -> Unit)? = null
    ) {
        stopJingle()
        isPlaying = true
        jingleJob = scope.launch(Dispatchers.Default) {
            val sampleRate = 22050
            // Notes and lyrics steps for the viral commercial
            val melody = listOf(
                Pair(523.25, 200L), // C5 - "Who's"
                Pair(659.25, 200L), // E5 - "the"
                Pair(783.99, 250L), // G5 - "Twat-"
                Pair(880.00, 350L), // A5 - "NUGGET"
                Pair(0.0, 100L),
                Pair(783.99, 200L), // G5 - "of"
                Pair(659.25, 200L), // E5 - "the"
                Pair(523.25, 450L), // C5 - "Month?!"
                Pair(0.0, 150L),
                Pair(587.33, 200L), // D5 - "Did"
                Pair(587.33, 200L), // D5 - "you"
                Pair(659.25, 200L), // E5 - "burn"
                Pair(587.33, 200L), // D5 - "the"
                Pair(523.25, 300L), // C5 - "toast"
                Pair(440.00, 400L), // A4 - "again?"
                Pair(0.0, 150L),
                Pair(523.25, 180L), // C5 - "Pay"
                Pair(659.25, 180L), // E5 - "your"
                Pair(783.99, 200L), // G5 - "two"
                Pair(1046.50, 500L) // C6 - "BUCKS!"
            )

            val lyrics = listOf(
                "👑 Who's...",
                "👑 the...",
                "✨ TWAT-...",
                "🏆 NUGGET OF THE MONTH?!",
                "",
                "📜 Did you...",
                "🔥 microwave...",
                "⚡ aluminum foil again?!",
                "",
                "🧽 No brownie points...",
                "🤡 for the brown nosers!",
                "⚖️ AI Judges...",
                "🔨 have spoken!",
                "💸 Pay your $2 bucks...",
                "👑 And wear the Golden Tiara!"
            )

            for ((index, note) in melody.withIndex()) {
                if (!isActive) break
                val lyric = lyrics.getOrElse(index) { "🎶 TwatNugget Anthem!" }
                onNoteStep?.invoke(index, lyric)
                if (note.first > 0) {
                    playTone(note.first, note.second.toInt(), sampleRate)
                } else {
                    delay(note.second)
                }
            }
            isPlaying = false
            onFinished?.invoke()
        }
    }

    /**
     * Plays short sound effects
     */
    fun playSoundEffect(scope: CoroutineScope, effect: SoundEffect) {
        scope.launch(Dispatchers.Default) {
            val sampleRate = 22050
            when (effect) {
                SoundEffect.GAVEL_BANG -> {
                    playTone(150.0, 120, sampleRate)
                    delay(50)
                    playTone(130.0, 200, sampleRate)
                }
                SoundEffect.BROWN_NOSER_BUZZER -> {
                    playTone(280.0, 180, sampleRate)
                    playTone(260.0, 180, sampleRate)
                    playTone(220.0, 300, sampleRate)
                }
                SoundEffect.CROWN_FANFARE -> {
                    playTone(523.25, 120, sampleRate)
                    playTone(659.25, 120, sampleRate)
                    playTone(783.99, 150, sampleRate)
                    playTone(1046.50, 350, sampleRate)
                }
                SoundEffect.WHEEL_CLICK -> {
                    playTone(880.0, 30, sampleRate)
                }
                SoundEffect.CASH_REGISTER -> {
                    playTone(987.77, 100, sampleRate)
                    playTone(1318.51, 250, sampleRate)
                }
            }
        }
    }

    private fun playTone(freqHz: Double, durationMs: Int, sampleRate: Int) {
        try {
            val count = (sampleRate * (durationMs / 1000.0)).toInt()
            val samples = ShortArray(count)
            for (i in 0 until count) {
                val t = i.toDouble() / sampleRate
                // Add soft decay envelope to eliminate clicks
                val envelope = when {
                    i < 200 -> (i.toDouble() / 200)
                    i > count - 400 -> ((count - i).toDouble() / 400)
                    else -> 1.0
                }
                val sample = (sin(2.0 * Math.PI * freqHz * t) * 0.7 * envelope * Short.MAX_VALUE).toInt()
                samples[i] = sample.toShort()
            }

            val bufferSize = samples.size * 2
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(samples, 0, samples.size)
            audioTrack.play()
            Thread.sleep(durationMs.toLong())
            audioTrack.release()
        } catch (_: Exception) {
            // AudioTrack fallback
        }
    }

    enum class SoundEffect {
        GAVEL_BANG,
        BROWN_NOSER_BUZZER,
        CROWN_FANFARE,
        WHEEL_CLICK,
        CASH_REGISTER
    }
}
