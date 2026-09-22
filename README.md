# CashPrize Social Media Manager

Production-oriented Kotlin + Jetpack Compose Android app for managing Instagram, TikTok, YouTube, Facebook, X, and LinkedIn content.

Implemented:
- Material 3 dashboard and account connection screens
- Room database persistence for posts
- Repository and ViewModel layers
- OAuth browser flow entry points and deep-link callback
- Retrofit API contracts for YouTube and TikTok upload flows
- Publisher abstraction for all supported channels

## Launch

Open the repository in Android Studio, sync Gradle, and run the `app` configuration.

## Required production configuration

Replace OAuth client placeholders in `OAuthManager`, register `cashprize://oauth/callback` with each provider, and exchange authorization codes in a secure backend. Never ship provider client secrets or long-lived access tokens in the APK. Complete provider app review and configure the API base URLs and authenticated Retrofit clients before enabling live publishing.
