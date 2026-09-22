package com.bedober.cashprize.auth

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.bedober.cashprize.data.Platform

/** Starts provider OAuth. Register these redirect URIs and client IDs in each developer console. */
class OAuthManager(private val context: Context) {
    fun connect(platform: Platform) {
        val url = when (platform) {
            Platform.YOUTUBE -> "https://accounts.google.com/o/oauth2/v2/auth?response_type=code&access_type=offline&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fyoutube.upload&redirect_uri=cashprize%3A%2F%2Foauth%2Fcallback&client_id=REPLACE_WITH_GOOGLE_CLIENT_ID"
            Platform.TIKTOK -> "https://www.tiktok.com/v2/auth/authorize/?client_key=REPLACE_WITH_TIKTOK_CLIENT_KEY&response_type=code&scope=user.info.basic%2Cvideo.upload&redirect_uri=cashprize%3A%2F%2Foauth%2Fcallback"
            else -> "https://cashprize.example.com/oauth/start/${platform.name.lowercase()}"
        }
        CustomTabsIntent.Builder().build().launchUrl(context, Uri.parse(url))
    }
}
