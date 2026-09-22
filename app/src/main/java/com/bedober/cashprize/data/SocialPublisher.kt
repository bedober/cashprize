package com.bedober.cashprize.data

import kotlinx.coroutines.delay

interface SocialPublisher {
    val platform: Platform
    suspend fun publish(post: SocialPost): Result<String>
    suspend fun delete(remoteId: String): Result<Unit>
}

// Production code should replace these demo calls with approved OAuth-backed APIs.
class TikTokPublisher : SocialPublisher {
    override val platform = Platform.TIKTOK
    override suspend fun publish(post: SocialPost): Result<String> { delay(500); return Result.success("tiktok-demo-${post.id}") }
    override suspend fun delete(remoteId: String): Result<Unit> = Result.success(Unit)
}

class YouTubePublisher : SocialPublisher {
    override val platform = Platform.YOUTUBE
    override suspend fun publish(post: SocialPost): Result<String> { delay(500); return Result.success("youtube-demo-${post.id}") }
    override suspend fun delete(remoteId: String): Result<Unit> = Result.success(Unit)
}
