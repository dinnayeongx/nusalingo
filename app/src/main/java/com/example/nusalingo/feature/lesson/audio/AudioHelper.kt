package com.example.nusalingo.feature.lesson.audio

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast

fun playAudio(context: Context, audioName: String) {
    val resId = context.resources.getIdentifier(audioName, "raw", context.packageName)

    if (resId == 0) {
        Toast.makeText(context, "Audio belum tersedia", Toast.LENGTH_SHORT).show()
        return
    }

    val player = MediaPlayer.create(context, resId)
    player.setOnCompletionListener { it.release() }
    player.start()
}