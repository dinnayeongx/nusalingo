package com.example.nusalingo.feature.lesson.audio

import android.content.Context
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Handler
import android.os.Looper
import android.widget.Toast

private const val AUDIO_SEQUENCE_PLAYBACK_SPEED = 1.35f
private const val AUDIO_SEQUENCE_GAP_MILLIS = 90L

fun playAudio(context: Context, audioName: String) {
    val resId = context.resources.getIdentifier(audioName, "raw", context.packageName)

    if (resId == 0) {
        Toast.makeText(context, "Audio belum tersedia", Toast.LENGTH_SHORT).show()
        return
    }

    MediaPlayer.create(context, resId)?.apply {
        setOnCompletionListener { it.release() }
        start()
    } ?: Toast.makeText(context, "Audio belum tersedia", Toast.LENGTH_SHORT).show()
}

class AudioSequencePlayer(context: Context) {
    private val appContext = context.applicationContext
    private val handler = Handler(Looper.getMainLooper())

    private var audioNames: List<String> = emptyList()
    private var currentIndex = 0
    private var mediaPlayer: MediaPlayer? = null
    private var pendingAudio: Runnable? = null

    fun play(audioNames: List<String>) {
        stop()
        if (audioNames.isEmpty()) return

        this.audioNames = audioNames
        playCurrentAudio()
    }

    fun stop() {
        pendingAudio?.let(handler::removeCallbacks)
        pendingAudio = null
        audioNames = emptyList()
        currentIndex = 0

        mediaPlayer?.let { player ->
            mediaPlayer = null
            player.setOnCompletionListener(null)
            if (player.isPlaying) player.stop()
            player.release()
        }
    }

    private fun playCurrentAudio() {
        if (currentIndex >= audioNames.size) return

        val audioName = audioNames[currentIndex]
        val resId = appContext.resources.getIdentifier(audioName, "raw", appContext.packageName)
        if (resId == 0) {
            Toast.makeText(appContext, "Audio belum tersedia", Toast.LENGTH_SHORT).show()
            stop()
            return
        }

        val player = MediaPlayer.create(appContext, resId)
        if (player == null) {
            Toast.makeText(appContext, "Audio belum tersedia", Toast.LENGTH_SHORT).show()
            stop()
            return
        }

        mediaPlayer = player
        player.setPlaybackParams(
            PlaybackParams()
                .setSpeed(AUDIO_SEQUENCE_PLAYBACK_SPEED)
                .setPitch(1f)
        )
        player.setOnCompletionListener { completedPlayer ->
            completedPlayer.setOnCompletionListener(null)
            completedPlayer.release()
            if (mediaPlayer === completedPlayer) mediaPlayer = null

            currentIndex++
            if (currentIndex < audioNames.size) {
                val nextAudio = Runnable {
                    pendingAudio = null
                    playCurrentAudio()
                }
                pendingAudio = nextAudio
                handler.postDelayed(nextAudio, AUDIO_SEQUENCE_GAP_MILLIS)
            } else {
                audioNames = emptyList()
                currentIndex = 0
            }
        }
        player.start()
    }
}
