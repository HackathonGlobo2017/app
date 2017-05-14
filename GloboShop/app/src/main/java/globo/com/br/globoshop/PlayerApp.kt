package io.clappr.player.app

import android.app.Application
import android.support.multidex.MultiDex
import io.clappr.player.Player

class PlayerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Player.initialize(this)
        MultiDex.install(this)

    }
}